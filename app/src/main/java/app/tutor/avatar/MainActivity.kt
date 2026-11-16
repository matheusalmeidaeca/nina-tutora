package app.tutor.avatar

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import java.util.Locale

class MainActivity : Activity(), TextToSpeech.OnInitListener {

    private lateinit var avatar: AvatarView
    private lateinit var bubble: TextView
    private lateinit var info: TextView
    private lateinit var panel: LinearLayout

    private var tts: TextToSpeech? = null
    private var ttsInitDone = false
    private var ttsReady = false
    private var voiceOn = true
    private var pendingSpeech: String? = null

    private val handler = Handler(Looper.getMainLooper())
    private val stopTalking = Runnable { avatar.talking = false }

    private var timer: CountDownTimer? = null
    private var timerText: TextView? = null

    private val match = ViewGroup.LayoutParams.MATCH_PARENT
    private val wrap = ViewGroup.LayoutParams.WRAP_CONTENT

    // ---------------------------------------------------------------- ciclo de vida

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this)
        root.orientation = LinearLayout.VERTICAL
        root.setBackgroundColor(Color.parseColor("#EEF3FA"))

        avatar = AvatarView(this)
        root.addView(avatar, LinearLayout.LayoutParams(match, dp(230)))

        bubble = TextView(this)
        bubble.textSize = 17f
        bubble.setTextColor(Color.parseColor("#1F2937"))
        bubble.setPadding(dp(16), dp(14), dp(16), dp(14))
        bubble.minLines = 3
        bubble.background = rounded(Color.WHITE, 20)
        val bl = LinearLayout.LayoutParams(match, wrap)
        bl.setMargins(dp(16), dp(4), dp(16), dp(4))
        root.addView(bubble, bl)

        info = TextView(this)
        info.textSize = 13f
        info.setTextColor(Color.parseColor("#6B7280"))
        info.gravity = Gravity.CENTER
        root.addView(info, LinearLayout.LayoutParams(match, wrap))

        val scroll = ScrollView(this)
        panel = LinearLayout(this)
        panel.orientation = LinearLayout.VERTICAL
        scroll.addView(panel, ViewGroup.LayoutParams(match, wrap))
        root.addView(scroll, LinearLayout.LayoutParams(match, 0, 1f))

        setContentView(root)

        tts = TextToSpeech(this, this)
        showMenu()
    }

    override fun onInit(status: Int) {
        ttsInitDone = true
        if (status == TextToSpeech.SUCCESS) {
            val engine = tts
            if (engine != null) {
                val r = engine.setLanguage(Locale("pt", "BR"))
                ttsReady = r != TextToSpeech.LANG_MISSING_DATA && r != TextToSpeech.LANG_NOT_SUPPORTED
                engine.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {
                        runOnUiThread {
                            handler.removeCallbacks(stopTalking)
                            avatar.talking = true
                        }
                    }

                    override fun onDone(utteranceId: String?) {
                        runOnUiThread { avatar.talking = false }
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                        runOnUiThread { avatar.talking = false }
                    }
                })
            }
        }
        val pending = pendingSpeech
        pendingSpeech = null
        if (pending != null) speak(pending)
    }

    override fun onDestroy() {
        timer?.cancel()
        tts?.stop()
        tts?.shutdown()
        super.onDestroy()
    }

    // ---------------------------------------------------------------- fala

    private fun say(text: String) {
        bubble.text = text
        speak(text)
    }

    private fun speak(text: String) {
        handler.removeCallbacks(stopTalking)
        tts?.stop()
        avatar.talking = false
        if (!voiceOn) return
        if (!ttsInitDone) {
            pendingSpeech = text
            return
        }
        val engine = tts
        if (ttsReady && engine != null) {
            engine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "u" + System.nanoTime())
        } else {
            // sem voz instalada: anima a boca por um tempo estimado
            avatar.talking = true
            handler.postDelayed(stopTalking, text.length * 60L)
        }
    }

    private fun silence() {
        handler.removeCallbacks(stopTalking)
        tts?.stop()
        avatar.talking = false
    }

    // ---------------------------------------------------------------- telas

    private fun showMenu(greet: Boolean = true) {
        stopAll()
        avatar.mood = AvatarView.Mood.FELIZ
        info.text = ""
        if (greet) say("Oi! Eu sou a Nina, sua tutora de estudos. O que vamos fazer hoje?")

        val v = mutableListOf<View>()
        for (s in Content.subjects) {
            v.add(btn(s.icon + "  " + s.name) { showSubject(s) })
        }
        v.add(btn("🍅  Foco Pomodoro", "#D9534F") { showPomodoro() })
        val voiceLabel = if (voiceOn) "🔊  Voz: ligada" else "🔇  Voz: desligada"
        v.add(btn(voiceLabel, "#6B7280") {
            voiceOn = !voiceOn
            if (!voiceOn) silence()
            showMenu(false)
        })
        setPanel(v)
    }

    private fun showSubject(s: Subject) {
        stopAll()
        info.text = s.name
        avatar.mood = AvatarView.Mood.FELIZ
        say("${s.name}. Qual assunto você quer estudar?")
        val v = mutableListOf<View>()
        for (t in s.topics) {
            v.add(btn(t.title) { lessonStep(t, 0) })
        }
        v.add(btn("◀  Voltar", "#9CA3AF") { showMenu() })
        setPanel(v)
    }

    private fun lessonStep(t: Topic, i: Int) {
        info.text = t.title + " · passo " + (i + 1) + " de " + t.steps.size
        avatar.mood = AvatarView.Mood.FELIZ
        say(t.steps[i])

        val v = mutableListOf<View>()
        if (i < t.steps.size - 1) {
            v.add(btn("Próximo ▶") { lessonStep(t, i + 1) })
        } else {
            v.add(btn("Concluir aula ▶") { lessonEnd(t) })
        }
        v.add(btn("🔁  Repetir", "#6B7280") { lessonStep(t, i) })
        if (i > 0) v.add(btn("◀  Passo anterior", "#6B7280") { lessonStep(t, i - 1) })
        v.add(btn("Menu", "#9CA3AF") { showMenu() })
        setPanel(v)
    }

    private fun lessonEnd(t: Topic) {
        info.text = t.title
        avatar.mood = AvatarView.Mood.FELIZ
        say(
            "Aula concluída! Agora o método Feynman: explique esse assunto em voz alta, " +
                "com suas palavras, como se estivesse ensinando alguém. Onde travar, é o que precisa rever. " +
                "Depois faça o quiz."
        )
        val v = mutableListOf<View>()
        v.add(btn("📝  Fazer o quiz") { quiz(t, 0, 0) })
        v.add(btn("🔁  Rever a aula", "#6B7280") { lessonStep(t, 0) })
        v.add(btn("Menu", "#9CA3AF") { showMenu() })
        setPanel(v)
    }

    private fun quiz(t: Topic, i: Int, score: Int) {
        if (i >= t.quiz.size) {
            quizResult(t, score)
            return
        }
        val q = t.quiz[i]
        info.text = "Quiz · pergunta " + (i + 1) + " de " + t.quiz.size
        avatar.mood = AvatarView.Mood.PENSANDO
        say(q.q)

        val v = mutableListOf<View>()
        for ((idx, opt) in q.options.withIndex()) {
            v.add(btn(opt) { answer(t, i, score, idx) })
        }
        setPanel(v)
    }

    private fun answer(t: Topic, i: Int, score: Int, chosen: Int) {
        val q = t.quiz[i]
        val ok = chosen == q.correct
        avatar.mood = if (ok) AvatarView.Mood.FELIZ else AvatarView.Mood.PENSANDO
        val right = q.options[q.correct]
        say((if (ok) "Isso mesmo! " else "Quase. A resposta certa é: $right. ") + q.why)

        val v = mutableListOf<View>()
        val next = score + (if (ok) 1 else 0)
        v.add(btn("Continuar ▶") { quiz(t, i + 1, next) })
        setPanel(v)
    }

    private fun quizResult(t: Topic, score: Int) {
        val n = t.quiz.size
        info.text = "Resultado do quiz"
        avatar.mood = AvatarView.Mood.FELIZ
        val msg = if (score == n) "Perfeito!" else "Vale rever a aula e tentar de novo."
        say("Você acertou $score de $n. $msg")
        val v = mutableListOf<View>()
        v.add(btn("🔁  Refazer o quiz") { quiz(t, 0, 0) })
        v.add(btn("📖  Rever a aula", "#6B7280") { lessonStep(t, 0) })
        v.add(btn("Menu", "#9CA3AF") { showMenu() })
        setPanel(v)
    }

    // ---------------------------------------------------------------- pomodoro

    private fun showPomodoro() {
        stopAll()
        info.text = "Foco Pomodoro"
        avatar.mood = AvatarView.Mood.FELIZ
        say("Vamos focar? Escolha 25 minutos de foco ou 5 minutos de pausa.")

        val clock = TextView(this)
        clock.textSize = 48f
        clock.gravity = Gravity.CENTER
        clock.setTextColor(Color.parseColor("#1F2937"))
        clock.text = "25:00"
        timerText = clock

        val v = mutableListOf<View>()
        v.add(clock)
        v.add(btn("🍅  Foco 25 min", "#D9534F") { startTimer(25 * 60, true) })
        v.add(btn("☕  Pausa 5 min", "#2E9E6B") { startTimer(5 * 60, false) })
        v.add(btn("Menu", "#9CA3AF") { showMenu() })
        setPanel(v)
    }

    private fun startTimer(seconds: Int, focus: Boolean) {
        timer?.cancel()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        avatar.mood = if (focus) AvatarView.Mood.PENSANDO else AvatarView.Mood.FELIZ
        say(
            if (focus) "Foco iniciado. Deixe o celular de lado e faça uma coisa só."
            else "Pausa iniciada. Levante, beba água e descanse os olhos."
        )
        timer = object : CountDownTimer(seconds * 1000L, 1000L) {
            override fun onTick(ms: Long) {
                timerText?.text = fmt(ms / 1000)
            }

            override fun onFinish() {
                timerText?.text = "00:00"
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                avatar.mood = AvatarView.Mood.FELIZ
                say(
                    if (focus) "Acabou o foco! Muito bem. Agora uma pausa de 5 minutos."
                    else "Pausa terminada. Vamos para o próximo bloco de foco?"
                )
            }
        }.start()
    }

    private fun fmt(total: Long): String {
        val m = total / 60
        val s = total % 60
        return (if (m < 10) "0" else "") + m + ":" + (if (s < 10) "0" else "") + s
    }

    // ---------------------------------------------------------------- utilidades

    private fun stopAll() {
        timer?.cancel()
        timer = null
        timerText = null
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        silence()
    }

    private fun setPanel(views: List<View>) {
        panel.removeAllViews()
        for (x in views) panel.addView(x)
    }

    private fun btn(label: String, color: String = "#2F6FED", onClick: () -> Unit): Button {
        val b = Button(this)
        b.text = label
        b.setAllCaps(false)
        b.textSize = 16f
        b.setTextColor(Color.WHITE)
        b.background = rounded(Color.parseColor(color), 16)
        b.setOnClickListener { onClick() }
        val lp = LinearLayout.LayoutParams(match, wrap)
        lp.setMargins(dp(16), dp(6), dp(16), dp(6))
        b.layoutParams = lp
        return b
    }

    private fun rounded(color: Int, radiusDp: Int): GradientDrawable {
        val d = GradientDrawable()
        d.setColor(color)
        d.cornerRadius = dp(radiusDp).toFloat()
        return d
    }

    private fun dp(v: Int): Int = (v * resources.displayMetrics.density).toInt()
}
