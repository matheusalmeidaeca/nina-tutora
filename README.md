# Nina Tutora

Avatar tutora de estudos (Android nativo, Kotlin). Sem IA nesta versão: as aulas e os quizzes são conteúdo pronto.

- Avatar desenhada em código (pisca, respira, mexe a boca quando fala)
- Voz do próprio Android (TextToSpeech pt-BR)
- Aulas e quiz: Contabilidade, Administração, Economia
- Foco Pomodoro (25/5)

## Compilar pelo GitHub

```
cd nina-tutora
git init
git add .
git commit -m "Nina Tutora v0.1"
git branch -M main
git remote add origin https://github.com/SEU_USUARIO/nina-tutora.git
git push -u origin main
```

Depois: GitHub > Actions > "Build APK" > baixe o artefato `nina-tutora-apk`.

## Onde mexer

- `Content.kt`: aulas e perguntas (adicione assuntos aqui)
- `AvatarView.kt`: visual e animação da avatar
- `MainActivity.kt`: telas e fluxo
