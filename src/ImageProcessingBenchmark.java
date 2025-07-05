package com.example;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.photo.Photo;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ImageProcessingBenchmark extends JFrame {

    // Carrega a biblioteca nativa do OpenCV
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    // --- Variáveis de Estado ---
    private Mat originalImageMat;
    private Mat finalImageMat;

    // --- Componentes da UI ---
    private final JButton loadButton;
    private final JButton executeButton;
    private final JButton saveButton;
    private final JSlider intensitySlider;
    private final JLabel originalImageLabel;
    private final JLabel processedImageLabel;
    private final JLabel latencyPrepLabel;
    private final JLabel latencyDetLabel;
    private final JLabel latencyTotalLabel;
    private final JLabel fpsLabel;

    public ImageProcessingBenchmark() {
        // --- Configuração da Janela Principal ---
        setTitle("Benchmark de Pipeline PDI - Java/Swing Edition");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- PAINEL DE CONTROLES (Esquerda) ---
        JPanel controlsPanel = new JPanel();
        controlsPanel.setLayout(new BoxLayout(controlsPanel, BoxLayout.Y_AXIS));
        controlsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        controlsPanel.setPreferredSize(new Dimension(250, 0));

        JLabel titleLabel = new JLabel("Controles do Pipeline");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        loadButton = new JButton("Carregar Imagem");
        executeButton = new JButton("Executar Pipeline");
        saveButton = new JButton("Salvar Resultado");
        intensitySlider = new JSlider(1, 30, 10);

        // Configuração do Slider
        intensitySlider.setMajorTickSpacing(5);
        intensitySlider.setPaintTicks(true);
        intensitySlider.setPaintLabels(true);
        intensitySlider.setBorder(new TitledBorder("Intensidade do Filtro"));
        
        // Alinhamento e tamanho dos componentes de controle
        Dimension buttonSize = new Dimension(200, 30);
        for (JButton btn : List.of(loadButton, executeButton, saveButton)) {
            btn.setAlignmentX(Component.CENTER_ALIGNMENT);
            btn.setMaximumSize(buttonSize);
        }
        intensitySlider.setAlignmentX(Component.CENTER_ALIGNMENT);
        intensitySlider.setMaximumSize(new Dimension(220, 100));
        
        // Adiciona os componentes ao painel de controle
        controlsPanel.add(titleLabel);
        controlsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        controlsPanel.add(loadButton);
        controlsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlsPanel.add(intensitySlider);
        controlsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlsPanel.add(executeButton);
        controlsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        controlsPanel.add(saveButton);
        
        controlsPanel.add(Box.createVerticalGlue()); // Empurra o relatório para baixo

        // --- PAINEL DE RELATÓRIO DE DESEMPENHO ---
        JPanel reportPanel = new JPanel();
        reportPanel.setLayout(new BoxLayout(reportPanel, BoxLayout.Y_AXIS));
        reportPanel.setBorder(new TitledBorder("Relatório de Desempenho"));
        
        latencyPrepLabel = new JLabel("Latência Pré-proc.: -- ms");
        latencyDetLabel = new JLabel("Latência Detecção: -- ms");
        latencyTotalLabel = new JLabel("Latência Total: -- ms");
        latencyTotalLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        fpsLabel = new JLabel("FPS Estimado: --");
        fpsLabel.setFont(new Font("SansSerif", Font.BOLD, 12));

        reportPanel.add(latencyPrepLabel);
        reportPanel.add(latencyDetLabel);
        reportPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        reportPanel.add(latencyTotalLabel);
        reportPanel.add(fpsLabel);
        reportPanel.setMaximumSize(new Dimension(220, 120));
        controlsPanel.add(reportPanel);


        // --- PAINEL DE IMAGENS (Centro) ---
        JPanel imagePanel = new JPanel(new GridLayout(1, 2, 10, 10));
        imagePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel originalPanel = new JPanel(new BorderLayout());
        originalPanel.setBorder(new TitledBorder("Imagem Original"));
        originalImageLabel = new JLabel("Carregue uma imagem", SwingConstants.CENTER);
        originalPanel.add(originalImageLabel, BorderLayout.CENTER);

        JPanel processedPanel = new JPanel(new BorderLayout());
        processedPanel.setBorder(new TitledBorder("Resultado do Pipeline"));
        processedImageLabel = new JLabel("O resultado aparecerá aqui", SwingConstants.CENTER);
        processedPanel.add(processedImageLabel, BorderLayout.CENTER);

        imagePanel.add(originalPanel);
        imagePanel.add(processedPanel);

        // Adiciona os paineis principais à janela
        add(controlsPanel, BorderLayout.WEST);
        add(imagePanel, BorderLayout.CENTER);

        // --- AÇÕES DOS BOTÕES ---
        setupActions();

        // Estado inicial dos botões
        executeButton.setEnabled(false);
        saveButton.setEnabled(false);
    }

    private void setupActions() {
        loadButton.addActionListener(this::loadImage);
        executeButton.addActionListener(this::executePipeline);
        saveButton.addActionListener(this::saveResult);
    }

    private void loadImage(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Escolha uma imagem");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Arquivos de Imagem", "jpg", "jpeg", "png", "bmp"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            originalImageMat = Imgcodecs.imread(file.getAbsolutePath());
            if (!originalImageMat.empty()) {
                displayImage(originalImageMat, originalImageLabel);
                processedImageLabel.setIcon(null);
                processedImageLabel.setText("Pronto para processar");
                executeButton.setEnabled(true);
                saveButton.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(this, "Não foi possível carregar a imagem.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Inicia o processamento da imagem em uma thread de trabalho para não congelar a UI.
     */
    private void executePipeline(ActionEvent e) {
        if (originalImageMat == null) {
            JOptionPane.showMessageDialog(this, "Por favor, carregue uma imagem primeiro.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Desabilita botões para evitar cliques múltiplos durante o processamento
        executeButton.setEnabled(false);
        saveButton.setEnabled(false);
        processedImageLabel.setText("Processando...");

        // Cria e executa o SwingWorker para processamento em segundo plano
        PipelineWorker worker = new PipelineWorker(originalImageMat.clone(), intensitySlider.getValue());
        worker.execute();
    }
    
    private void saveResult(ActionEvent e) {
        if (finalImageMat == null) return;
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar resultado como...");
        fileChooser.setSelectedFile(new File("resultado.png"));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("PNG", "png"));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("JPEG", "jpg"));
        
        if(fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            Imgcodecs.imwrite(file.getAbsolutePath(), finalImageMat);
            JOptionPane.showMessageDialog(this, "Imagem salva com sucesso em:\n" + file.getAbsolutePath(), "Sucesso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Converte uma Mat do OpenCV para uma BufferedImage do Java AWT para exibição.
     */
    private BufferedImage matToBufferedImage(Mat mat) {
        int type = (mat.channels() > 1) ? BufferedImage.TYPE_3BYTE_BGR : BufferedImage.TYPE_BYTE_GRAY;
        int bufferSize = mat.channels() * mat.cols() * mat.rows();
        byte[] buffer = new byte[bufferSize];
        mat.get(0, 0, buffer);
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
        return image;
    }

    /**
     * Redimensiona e exibe a imagem em um JLabel.
     */
    private void displayImage(Mat mat, JLabel label) {
        // Redimensiona a imagem para caber no label, mantendo a proporção
        int maxWidth = label.getWidth() - 20;
        int maxHeight = label.getHeight() - 20;
        if (maxWidth <= 0) maxWidth = 550; // Fallback se o label não estiver renderizado ainda
        if (maxHeight <= 0) maxHeight = 550;

        Size originalSize = mat.size();
        double scale = Math.min((double) maxWidth / originalSize.width, (double) maxHeight / originalSize.height);
        Size newSize = new Size(originalSize.width * scale, originalSize.height * scale);
        
        Mat resizedMat = new Mat();
        Imgproc.resize(mat, resizedMat, newSize, 0, 0, Imgproc.INTER_AREA);

        BufferedImage bufferedImage = matToBufferedImage(resizedMat);
        label.setIcon(new ImageIcon(bufferedImage));
        label.setText("");
    }
    
    // Classe interna para encapsular o resultado do pipeline
    private static class PipelineResult {
        final Mat processedImage;
        final double prepLatency;
        final double detLatency;

        PipelineResult(Mat processedImage, double prepLatency, double detLatency) {
            this.processedImage = processedImage;
            this.prepLatency = prepLatency;
            this.detLatency = detLatency;
        }
    }

    /**
     * Classe SwingWorker para executar o pipeline de imagem em uma thread separada.
     * Isso evita que a interface do usuário congele durante operações demoradas.
     */
    private class PipelineWorker extends SwingWorker<PipelineResult, Void> {
        private final Mat imageToProcess;
        private final int filterIntensity;

        PipelineWorker(Mat imageToProcess, int filterIntensity) {
            this.imageToProcess = imageToProcess;
            this.filterIntensity = filterIntensity;
        }

        @Override
        protected PipelineResult doInBackground() throws Exception {
            // Etapa 2: Pré-processamento (Redução de ruído)
            long startTimePrep = System.nanoTime();
            Mat denoisedImage = new Mat();
            Photo.fastNlMeansDenoisingColored(imageToProcess, denoisedImage, filterIntensity, filterIntensity, 7, 21);
            long endTimePrep = System.nanoTime();
            double prepLatency = (endTimePrep - startTimePrep) / 1_000_000.0; // para milissegundos

            // Etapa 3: Aumento de Contraste
            long startTimeDet = System.nanoTime();
            Mat contrastedImage = new Mat();

            double alpha = 1.8; // Contraste (1.0 = normal, >1 = mais contraste)
            double beta = 0;    // Brilho (0 = normal)

            denoisedImage.convertTo(contrastedImage, -1, alpha, beta);
            long endTimeDet = System.nanoTime();
            double detLatency = (endTimeDet - startTimeDet) / 1_000_000.0;

            return new PipelineResult(contrastedImage, prepLatency, detLatency);

        }

        @Override
        protected void done() {
            try {
                PipelineResult result = get(); // Obtém o resultado do doInBackground()
                finalImageMat = result.processedImage;

                // Exibe o resultado e atualiza o relatório
                displayImage(finalImageMat, processedImageLabel);
                
                double totalLatency = result.prepLatency + result.detLatency;
                double fps = (totalLatency > 0) ? 1000.0 / totalLatency : Double.POSITIVE_INFINITY;

                latencyPrepLabel.setText(String.format("Latência Pré-proc.: %.2f ms", result.prepLatency));
                latencyDetLabel.setText(String.format("Latência Detecção: %.2f ms", result.detLatency));
                latencyTotalLabel.setText(String.format("Latência Total: %.2f ms", totalLatency));
                fpsLabel.setText(String.format("FPS Estimado: %.2f", fps));
                
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(ImageProcessingBenchmark.this,
                        "Ocorreu um erro durante o processamento:\n" + e.getMessage(),
                        "Erro de Processamento", JOptionPane.ERROR_MESSAGE);
            } finally {
                // Reabilita os botões após a conclusão (com sucesso ou erro)
                executeButton.setEnabled(true);
                saveButton.setEnabled(true);
            }
        }
    }

    public static void main(String[] args) {
        // Executa a UI na Event Dispatch Thread (EDT) para segurança de thread
        SwingUtilities.invokeLater(() -> {
            ImageProcessingBenchmark app = new ImageProcessingBenchmark();
            app.setVisible(true);
        });
    }
}
