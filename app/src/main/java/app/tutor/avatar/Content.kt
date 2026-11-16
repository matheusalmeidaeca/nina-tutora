package app.tutor.avatar

class Question(
    val q: String,
    val options: List<String>,
    val correct: Int,
    val why: String
)

class Topic(
    val title: String,
    val steps: List<String>,
    val quiz: List<Question>
)

class Subject(
    val name: String,
    val icon: String,
    val topics: List<Topic>
)

object Content {

    val subjects: List<Subject> = listOf(
        Subject(
            "Contabilidade", "📒", listOf(
                Topic(
                    "Patrimônio: ativo, passivo e PL",
                    listOf(
                        "Patrimônio é o conjunto de bens, direitos e obrigações de uma pessoa ou empresa. É o ponto de partida da contabilidade.",
                        "Ativo são os bens e direitos: dinheiro em caixa, estoque, máquinas e valores a receber. É o que a empresa tem e que pode gerar benefício no futuro.",
                        "Passivo são as obrigações: fornecedores a pagar, empréstimos, salários e impostos a pagar. É o que a empresa deve.",
                        "Patrimônio líquido é o que sobra para os donos: ativo menos passivo. Inclui o capital social e os lucros acumulados.",
                        "A equação fundamental é: ativo igual a passivo mais patrimônio líquido. O balanço sempre fecha.",
                        "Exemplo: uma loja tem cem mil reais em ativos e deve quarenta mil. O patrimônio líquido dela é sessenta mil."
                    ),
                    listOf(
                        Question(
                            "Qual é a equação fundamental da contabilidade?",
                            listOf("Ativo = Passivo + Patrimônio Líquido", "Ativo = Passivo − Patrimônio Líquido", "Passivo = Ativo + Patrimônio Líquido"),
                            0,
                            "Tudo o que a empresa tem, o ativo, foi financiado por dívidas, o passivo, ou por dinheiro dos donos, o patrimônio líquido."
                        ),
                        Question(
                            "O estoque de mercadorias de uma loja é classificado como:",
                            listOf("Passivo", "Ativo", "Patrimônio líquido"),
                            1,
                            "Estoque é um bem que a loja possui e que vai gerar dinheiro quando for vendido. Por isso é ativo."
                        ),
                        Question(
                            "Uma loja tem 100 mil de ativo e 40 mil de passivo. Qual o patrimônio líquido?",
                            listOf("140 mil", "40 mil", "60 mil"),
                            2,
                            "Patrimônio líquido é ativo menos passivo: cem menos quarenta, igual a sessenta mil."
                        )
                    )
                ),
                Topic(
                    "Débito e crédito",
                    listOf(
                        "No método das partidas dobradas, todo fato contábil afeta pelo menos duas contas, e o total de débitos é sempre igual ao total de créditos.",
                        "Débito e crédito não significam bom ou ruim. São apenas os dois lados do lançamento: esquerda e direita.",
                        "Contas de ativo e de despesa aumentam com débito e diminuem com crédito.",
                        "Contas de passivo, patrimônio líquido e receita aumentam com crédito e diminuem com débito.",
                        "Exemplo: compra de mercadoria à vista. Debita estoque e credita caixa. Um ativo aumentou e outro diminuiu.",
                        "Outro exemplo: compra de mercadoria a prazo. Debita estoque e credita fornecedores. O ativo aumentou e o passivo também."
                    ),
                    listOf(
                        Question(
                            "Compra de mercadoria à vista. Qual o lançamento?",
                            listOf("Débito em Estoque, crédito em Caixa", "Débito em Caixa, crédito em Estoque", "Débito em Fornecedores, crédito em Estoque"),
                            0,
                            "O estoque aumenta, e ativo aumenta com débito. O caixa diminui, e ativo diminui com crédito."
                        ),
                        Question(
                            "Uma conta de receita aumenta com:",
                            listOf("Débito", "Crédito", "Nenhum dos dois"),
                            1,
                            "Receita aumenta com crédito, assim como passivo e patrimônio líquido."
                        ),
                        Question(
                            "Nas partidas dobradas, o total de débitos é:",
                            listOf("Sempre igual ao total de créditos", "Sempre maior que os créditos", "Sempre menor que os créditos"),
                            0,
                            "Esse equilíbrio é a base do método. Se não fecha, existe erro de lançamento."
                        )
                    )
                ),
                Topic(
                    "DRE: lucro ou prejuízo",
                    listOf(
                        "A DRE, Demonstração do Resultado do Exercício, mostra se a empresa teve lucro ou prejuízo em um período.",
                        "Ela começa na receita bruta. Tirando as deduções, como impostos sobre vendas, chegamos à receita líquida.",
                        "Receita líquida menos o custo das mercadorias vendidas dá o lucro bruto.",
                        "Do lucro bruto saem as despesas operacionais, como salários, aluguel e marketing.",
                        "Por fim, considerando o resultado financeiro e os impostos sobre o lucro, chega-se ao lucro líquido.",
                        "Diferença importante: o balanço patrimonial é uma foto de um momento. A DRE é um filme de um período."
                    ),
                    listOf(
                        Question(
                            "Receita líquida menos o custo das mercadorias vendidas é:",
                            listOf("Lucro líquido", "Lucro bruto", "Patrimônio líquido"),
                            1,
                            "Esse é o lucro bruto, ainda antes das despesas operacionais."
                        ),
                        Question(
                            "Qual a diferença entre balanço e DRE?",
                            listOf(
                                "O balanço mostra um momento, a DRE mostra um período",
                                "O balanço mostra um período, a DRE mostra um momento",
                                "Não há diferença"
                            ),
                            0,
                            "O balanço é a foto da posição em uma data. A DRE acumula o resultado de um intervalo de tempo."
                        )
                    )
                )
            )
        ),
        Subject(
            "Administração", "📊", listOf(
                Topic(
                    "As quatro funções: PODC",
                    listOf(
                        "As quatro funções clássicas da administração são planejar, organizar, dirigir e controlar. O ciclo é conhecido pela sigla PODC.",
                        "Planejar é definir objetivos e o caminho para chegar até eles.",
                        "Organizar é distribuir tarefas, recursos e responsabilidades entre as pessoas.",
                        "Dirigir é liderar, motivar e se comunicar para colocar o plano em ação.",
                        "Controlar é comparar o resultado com o planejado e corrigir os desvios. Isso reinicia o ciclo.",
                        "Exemplo no estoque de uma loja: planejar é prever a demanda, organizar é definir prateleiras e responsáveis, dirigir é orientar a equipe, controlar é conferir o inventário."
                    ),
                    listOf(
                        Question(
                            "Comparar o resultado com o planejado e corrigir desvios é:",
                            listOf("Planejar", "Dirigir", "Controlar"),
                            2,
                            "Essa é a função de controle. Ela fecha o ciclo e alimenta o próximo planejamento."
                        ),
                        Question(
                            "Definir objetivos e o caminho para alcançá-los é:",
                            listOf("Planejar", "Organizar", "Controlar"),
                            0,
                            "Planejamento vem primeiro: sem objetivo definido, não há o que organizar nem controlar."
                        ),
                        Question(
                            "A sigla PODC significa:",
                            listOf(
                                "Pesquisar, Operar, Divulgar, Concluir",
                                "Planejar, Organizar, Dirigir, Controlar",
                                "Produzir, Organizar, Distribuir, Cobrar"
                            ),
                            1,
                            "São as quatro funções administrativas clássicas."
                        )
                    )
                ),
                Topic(
                    "Análise SWOT (FOFA)",
                    listOf(
                        "A análise SWOT, chamada de FOFA em português, é uma ferramenta para avaliar uma empresa ou um projeto.",
                        "Forças e fraquezas são fatores internos: o que a empresa controla, como equipe, marca e processos.",
                        "Oportunidades e ameaças são fatores externos: mercado, concorrência, economia e leis.",
                        "Depois de listar tudo, cruze as informações: use as forças para aproveitar oportunidades e corrija as fraquezas que deixam a empresa vulnerável às ameaças."
                    ),
                    listOf(
                        Question(
                            "Um concorrente novo abriu na mesma rua. Na análise SWOT, isso é:",
                            listOf("Força", "Fraqueza", "Ameaça"),
                            2,
                            "É um fator externo e negativo, portanto uma ameaça."
                        ),
                        Question(
                            "Uma equipe bem treinada é:",
                            listOf("Força", "Oportunidade", "Ameaça"),
                            0,
                            "É um fator interno e positivo, portanto uma força."
                        ),
                        Question(
                            "Quais são os fatores internos da SWOT?",
                            listOf("Oportunidades e ameaças", "Forças e fraquezas", "Forças e oportunidades"),
                            1,
                            "Forças e fraquezas estão dentro da empresa. Oportunidades e ameaças vêm de fora."
                        )
                    )
                )
            )
        ),
        Subject(
            "Economia", "🌎", listOf(
                Topic(
                    "Oferta e demanda",
                    listOf(
                        "Demanda é quanto os consumidores querem e podem comprar. Oferta é quanto os produtores querem vender.",
                        "Quando o preço sobe, em geral a demanda cai e a oferta aumenta. Quando o preço cai, acontece o contrário.",
                        "O preço de equilíbrio é o ponto em que oferta e demanda se encontram.",
                        "Se a demanda é maior que a oferta, falta produto e o preço tende a subir. Se a oferta é maior, sobra produto e o preço tende a cair.",
                        "Exemplo: uma geada destrói parte da safra de café. A oferta diminui e o preço tende a subir."
                    ),
                    listOf(
                        Question(
                            "Uma geada reduz a safra de café. O preço tende a:",
                            listOf("Subir", "Cair", "Ficar igual"),
                            0,
                            "A oferta diminuiu e a demanda continua, então o preço tende a subir."
                        ),
                        Question(
                            "Quando há excesso de oferta, o preço tende a:",
                            listOf("Subir", "Cair", "Não muda"),
                            1,
                            "Com produto sobrando, os vendedores baixam o preço para escoar o estoque."
                        ),
                        Question(
                            "O preço de equilíbrio é onde:",
                            listOf("O governo define", "A oferta é zero", "Oferta e demanda se igualam"),
                            2,
                            "No equilíbrio, a quantidade que os consumidores querem comprar é igual à que os produtores querem vender."
                        )
                    )
                ),
                Topic(
                    "Inflação e juros",
                    listOf(
                        "Inflação é o aumento generalizado e contínuo dos preços. Com ela, o mesmo dinheiro compra menos com o passar do tempo.",
                        "No Brasil, o índice oficial de inflação é o IPCA, calculado pelo IBGE.",
                        "A taxa Selic é a taxa básica de juros da economia. Ela é definida pelo Copom, o Comitê de Política Monetária do Banco Central.",
                        "Para conter a inflação, o Banco Central costuma subir os juros. O crédito fica mais caro, o consumo esfria e os preços sobem menos.",
                        "Para estimular a economia, ele costuma baixar os juros.",
                        "Por isso, quem investe precisa comparar o rendimento com a inflação. O ganho acima da inflação é o rendimento real."
                    ),
                    listOf(
                        Question(
                            "Qual é o índice oficial de inflação no Brasil?",
                            listOf("IPCA", "PIB", "CDI"),
                            0,
                            "O IPCA é medido pelo IBGE e é a referência para a meta de inflação."
                        ),
                        Question(
                            "Quem define a taxa Selic?",
                            listOf("O Copom, do Banco Central", "O IBGE", "O Congresso"),
                            0,
                            "O Copom se reúne periodicamente para decidir a meta da taxa Selic."
                        ),
                        Question(
                            "O Banco Central costuma subir os juros para:",
                            listOf("Conter a inflação", "Aumentar o consumo", "Baixar o dólar a qualquer custo"),
                            0,
                            "Juros mais altos encarecem o crédito e esfriam a demanda, o que ajuda a segurar os preços."
                        )
                    )
                )
            )
        )
    )
}
