# **Projeto P3: Arquitetura de Computadores - UFMA**
Este repositório contém o Projeto P3 desenvolvido para a disciplina de Arquitetura de Computadores da Universidade Federal do Maranhão (UFMA).

**Sobre o Projeto**
Este projeto é uma aplicação desktop desenvolvida em Java Swing, focada em processamento digital de imagens (PDI) utilizando a biblioteca OpenCV. Ele implementa um pipeline de processamento que inclui etapas de pré-processamento (redução de ruído) e aprimoramento (aumento de contraste). A aplicação permite ao usuário carregar imagens, ajustar a intensidade do filtro de ruído através de um slider, executar o pipeline de processamento e salvar a imagem resultante. Um dos objetivos centrais do projeto é demonstrar e benchmarkar o desempenho do pipeline, exibindo métricas como latência de pré-processamento, latência de detecção e FPS estimado, garantindo que a interface do usuário permaneça responsiva durante as operações intensivas de processamento através do uso de SwingWorker.

**Tecnologias Utilizadas**
Linguagem de Programação: Java

**Bibliotecas/Frameworks: OpenCV (para processamento de imagens)**

**Ambiente de Desenvolvimento (IDE): IntelliJ IDEA**

**Estrutura do Projeto**
src/: Contém os arquivos fonte do projeto.

com.example/ImageProcessingBenchmark.java: A classe principal da aplicação, responsável pela interface gráfica (UI) em Swing, carregamento/salvamento de imagens, interação com o usuário e exibição dos resultados e métricas de desempenho. A classe interna PipelineWorker dentro deste arquivo implementa a lógica de processamento de imagem em segundo plano usando SwingWorker para evitar o congelamento da UI durante as operações do OpenCV, como denoising e ajuste de contraste.

out/: Diretório de saída para os arquivos compilados.

.idea/: Arquivos de configuração específicos do IntelliJ IDEA.

lib/: (Opcional, mas comum) Se você tiver bibliotecas externas que não são gerenciadas por um sistema de build como Maven/Gradle, elas poderiam estar aqui. No seu caso, o JAR do OpenCV está sendo referenciado diretamente.

Como Compilar e Rodar
Para compilar e executar este projeto, você precisará ter o Java Development Kit (JDK) e o IntelliJ IDEA instalados, além das bibliotecas OpenCV configuradas.

Pré-requisitos
JDK 24 (Oracle OpenJDK 24.0.1) ou versão compatível.

IntelliJ IDEA (versão Community ou Ultimate).

OpenCV 4.1.0 (ou a versão correspondente ao opencv-4110.jar que você está usando).

Configuração no IntelliJ IDEA
Clone o Repositório:

git clone [URL_DO_SEU_REPOSITORIO]
cd Imagem

Abra o Projeto no IntelliJ IDEA:

No IntelliJ, selecione File > Open e navegue até a pasta Imagem do projeto clonado.

Adicionar Dependência do OpenCV (JAR):

Vá em File > Project Structure... (Ctrl+Alt+Shift+S).

No menu lateral, selecione Modules.

Selecione o módulo Imagem.

Vá para a aba Dependencies.

Clique no botão + e selecione JARs or Directories....

Navegue até o local do seu arquivo opencv-4110.jar (ex: C:\Users\SeuUsuario\Downloads\opencv\build\java\opencv-4110.jar) e selecione-o. Clique OK.

Certifique-se de que o escopo esteja como Compile.

Clique Apply e OK.

Configurar o Caminho da Biblioteca Nativa (VM Options):

Vá em Run > Edit Configurations....

No painel esquerdo, selecione ImageProcessingBenchmark sob Application. (Se não existir, clique em + e crie uma nova Application para a classe ImageProcessingBenchmark).

Clique em Modify options e selecione Add VM options.

No campo VM options que apareceu, adicione a seguinte linha, substituindo o caminho pela localização da sua pasta x64 (ou x86) que contém a DLL nativa do OpenCV (opencv_java4110.dll):

-Djava.library.path="C:\Users\Windows Lite BR\Downloads\opencv\build\java\x64"

No campo Main class, clique no botão ... e selecione com.example.ImageProcessingBenchmark.

Clique Apply e OK.

Reconstruir o Projeto:

Vá em Build > Rebuild Project.

Executar o Projeto:

Clique no botão Run (o triângulo verde) na barra de ferramentas ou no menu Run > Run 'ImageProcessingBenchmark'.

Contribuição
Este projeto foi desenvolvido como parte de um trabalho acadêmico e não está aberto para contribuições externas no momento.

**Autores**
Este trabalho foi desenvolvido em grupo por:

João Felipe

Ana Patrícia

Wesley

André Vitor

José

**Licença**
MIT License

Copyright (c) [Ano] [Nome(s) do(s) detentor(es) dos direitos autorais]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
