package br.com.fiap.quod_api.utils;

import java.util.Arrays;
import java.util.List;

public class ImageValidator {

    private static final List<String> SUPPORTED_FORMATS = Arrays.asList("jpg", "jpeg", "png");

    public static boolean isFormatoValido(String nomeArquivo) {
        String extensao = nomeArquivo.substring(nomeArquivo.lastIndexOf(".") + 1).toLowerCase();
        return SUPPORTED_FORMATS.contains(extensao);
    }

    public static boolean isTamanhoValido(byte[] imagemBytes, int maxBytes) {
        return imagemBytes.length <= maxBytes;
    }

    public static boolean isImagemBaixaQualidade(String imagem) {
        if (imagem == null || imagem.isEmpty()) {
            return true; // Imagem inexistente é considerada de baixa qualidade
        }

        // Simulação de análise de qualidade de imagem
        return imagem.toLowerCase().contains("baixa_qualidade") ||
                imagem.toLowerCase().contains("baixa_resolucao") ||
                imagem.toLowerCase().contains("blur") ||
                imagem.toLowerCase().contains("borrada") ||
                imagem.length() < 1000; // Simulando imagem muito pequena (em tamanho de bytes)
    }

    public static boolean isFraudeSimulada(String nomeArquivo) {
        String nome = nomeArquivo.toLowerCase();
        return nome.contains("fraude") || nome.contains("deepfake") || nome.contains("mascara");
    }

    public static boolean imagensIguais(String imagem1, String imagem2) {
        return imagem1 != null && imagem1.equals(imagem2);
    }

    public static String detectarTipoFraudeSimulada(String nomeArquivo) {
        if (nomeArquivo.toLowerCase().contains("deepfake")) return "deepfake";
        if (nomeArquivo.toLowerCase().contains("mascara")) return "mascara";
        if (nomeArquivo.toLowerCase().contains("foto")) return "foto de foto";
        if (nomeArquivo.toLowerCase().contains("fraude")) return "documento falso";
        return null;
    }
}
