# **Arquitetura de Sistemas de Captura e Processamento de Imagens em Dispositivos Digitais**

## **Visão Geral do Projeto**

Este projeto, desenvolvido no âmbito da disciplina de Arquitetura de Computadores do curso de Bacharelado Interdisciplinar em Ciência e Tecnologia da UFMA, tem como objetivo principal investigar os elementos que compõem os sistemas digitais de captura e processamento de imagens. O foco é aprofundar o entendimento sobre a conversão analógico-digital, o pipeline de processamento e as unidades computacionais especializadas que integram os dispositivos modernos.

Para colocar a teoria em prática, foi desenvolvida uma aplicação de benchmark em Java com a biblioteca OpenCV. Este sistema permite a análise de performance de um pipeline de processamento de imagem, medindo métricas como latência e FPS, e correlacionando-as com conceitos de arquitetura de computadores.

**Repositório do Projeto:** [https://github.com/wesleygatinho/Projeto-Arquitetura-Cameras](https://github.com/wesleygatinho/Projeto-Arquitetura-Cameras)

## **Equipe**

### **Alunos Responsáveis:**
* Ana Patrícia Garros Viegas
* Andre Vitor Abreu Moreira
* João Felipe Pereira Campos
* João José Penha Sousa
* Wesley dos Santos Gatinho


## **Estrutura do Repositório**

O projeto está organizado da seguinte forma:

* **/Documentação:** Contém todos os relatórios e a documentação gerada ao longo do projeto.
    * **RELATÓRIO 1 - TAP:** O Termo de Abertura do Projeto, que define o escopo inicial, objetivos e restrições.
    * **RELATÓRIO 2 - Planejamento:** Apresenta a Estrutura Analítica do Projeto (EAP) e o planejamento de riscos.
    * **RELATÓRIO 3 - Relatório de Pesquisa:** Detalha a revisão de literatura, o escopo aprofundado e os estudos iniciais.
    * **RELATÓRIO 4 - Relatório de Engenharia:** Um relatório técnico sobre a execução e planejamento do projeto.
    * **relatorio\_final\_tep:** O relatório final consolidado, com a metodologia, resultados de benchmark e conclusões.
* **/src:** Contém o código-fonte da aplicação de benchmark.
    * **ImageProcessingBenchmark.java:** A classe principal da aplicação Java/Swing que implementa a interface e o pipeline de processamento.

## **Fundamentação Teórica**

O projeto se baseia em três pilares teóricos principais:

1.  **Processamento Digital de Imagens (PDI):** Envolve a manipulação de imagens por meio de algoritmos para extrair informações ou melhorar a qualidade visual. O pipeline implementado inclui etapas como pré-processamento (remoção de ruído) e realce (ajuste de contraste).

2.  **Sensores e Conversores:** A captura de imagens se inicia nos **sensores CMOS**, que convertem luz em sinais elétricos. Estes sinais analógicos são então transformados em dados digitais pelos **Conversores Analógico-Digitais (ADCs)**, um passo fundamental que impacta diretamente a qualidade e a velocidade da captura.

3.  **Arquitetura de Computadores:** A performance do PDI está intrinsecamente ligada à arquitetura subjacente. Componentes como **CPU, memória e barramentos** são cruciais. A velocidade do fluxo de dados entre esses componentes, seguindo o modelo da arquitetura de von Neumann, determina a latência e a eficiência do processamento, especialmente em sistemas embarcados com recursos limitados.

## **Aplicação de Benchmark**

Foi desenvolvida uma ferramenta de software para simular e analisar o desempenho de um pipeline de PDI.

### **Arquitetura do Software**
* **Linguagem:** Java
* **Interface Gráfica:** Swing
* **Processamento de Imagem:** Biblioteca OpenCV
* **Concorrência:** A classe `SwingWorker` é utilizada para executar o processamento de imagem em uma thread separada, garantindo que a interface do usuário permaneça responsiva durante operações intensivas.

### **Pipeline Implementado**
O pipeline executa as seguintes etapas sequenciais:
1.  **Carregamento da Imagem:** O usuário seleciona uma imagem do sistema de arquivos.
2.  **Pré-processamento:** É aplicado um filtro de remoção de ruído (*fastNlMeansDenoisingColored*). A intensidade do filtro pode ser ajustada pelo usuário.
3.  **Ajuste de Contraste:** A imagem tem seu contraste e brilho ajustados para melhorar a visualização.

### **Como Executar o Projeto**

1.  **Pré-requisitos:**
    * Java Development Kit (JDK) instalado.
    * Biblioteca OpenCV configurada no seu ambiente de desenvolvimento. É necessário ter o arquivo `.jar` da biblioteca no classpath e o arquivo da biblioteca nativa (ex: `opencv_javaXXX.dll` ou `.so`) no `java.library.path`.

2.  **Compilação e Execução:**
    * Compile o arquivo `ImageProcessingBenchmark.java`.
    * Execute a classe `ImageProcessingBenchmark`. Uma janela gráfica será exibida.

3.  **Utilização:**
    * Clique em **"Carregar Imagem"** para selecionar um arquivo.
    * Ajuste a **"Intensidade do Filtro"** no slider.
    * Clique em **"Executar Pipeline"** para processar a imagem.
    * O painel **"Relatório de Desempenho"** exibirá a latência de cada etapa, a latência total e o FPS estimado em tempo real.
    * Clique em **"Salvar Resultado"** para exportar a imagem processada.

## **Resultados e Análise**

O benchmarking demonstrou que a etapa de remoção de ruído é a mais custosa computacionalmente, impactando significativamente a latência total. A análise dos resultados permitiu correlacionar diretamente o desempenho do software com a arquitetura de hardware, evidenciando como a velocidade da memória e dos barramentos são gargalos potenciais no fluxo de dados de um pipeline de PDI.

## **Trabalhos Futuros**

Como próximos passos, o projeto pode evoluir para:
* **Testes em Hardware Real:** Implementar o pipeline em plataformas embarcadas como Raspberry Pi ou NVIDIA Jetson para validar o desempenho em um cenário prático.
* **Integração com Sensores:** Substituir o carregamento de arquivos estáticos pela captura de imagens em tempo real a partir de um sensor CMOS.
* **Evolução do Pipeline:** Incorporar algoritmos mais avançados baseados em redes neurais para tarefas como detecção de objetos, explorando o uso de NPUs e outras unidades de processamento especializado.
