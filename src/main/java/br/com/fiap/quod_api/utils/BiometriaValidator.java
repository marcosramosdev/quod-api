package br.com.fiap.quod_api.utils;

public class BiometriaValidator {


    public static boolean possuiFaceDetectavel(String imagem) {
        if (imagem == null || imagem.isEmpty()) {
            return false;
        }

        // Simulação de análise de detecção facial
        return !imagem.toLowerCase().contains("sem_face") &&
                !imagem.toLowerCase().contains("no_face") &&
                !imagem.toLowerCase().contains("face_borrada");
    }


    public static boolean possuiIluminacaoAdequada(String imagem) {
        if (imagem == null || imagem.isEmpty()) {
            return false;
        }

        // Simulação de análise de iluminação
        return !imagem.toLowerCase().contains("escuro") &&
                !imagem.toLowerCase().contains("dark") &&
                !imagem.toLowerCase().contains("baixa_iluminacao") &&
                !imagem.toLowerCase().contains("superexposto") &&
                !imagem.toLowerCase().contains("overexposed");
    }


    public static boolean possuiPosicionamentoAdequado(String imagem) {
        if (imagem == null || imagem.isEmpty()) {
            return false;
        }

        // Simulação de análise de posicionamento
        return !imagem.toLowerCase().contains("rotacao") &&
                !imagem.toLowerCase().contains("inclinada") &&
                !imagem.toLowerCase().contains("lateral") &&
                !imagem.toLowerCase().contains("perfil");
    }


    public static boolean isDetectadoSpoofing(String imagem) {
        if (imagem == null || imagem.isEmpty()) {
            return false;
        }

        // Simulação de detecção de spoofing
        return imagem.toLowerCase().contains("spoofing") ||
                imagem.toLowerCase().contains("mascara") ||
                imagem.toLowerCase().contains("foto_impressa") ||
                imagem.toLowerCase().contains("printed");
    }


    public static boolean isDetectadoDeepfake(String imagem) {
        if (imagem == null || imagem.isEmpty()) {
            return false;
        }

        // Simulação de detecção de deepfake
        return imagem.toLowerCase().contains("deepfake") ||
                imagem.toLowerCase().contains("face_modificada") ||
                imagem.toLowerCase().contains("ia_gerada") ||
                imagem.toLowerCase().contains("ai_generated");
    }

    public static double calcularQualidadeBiometrica(String imagem) {
        if (imagem == null || imagem.isEmpty()) {
            return 0.0;
        }

        double qualidade = 1.0;

        // Fatores que diminuem a qualidade
        if (!possuiFaceDetectavel(imagem)) qualidade -= 0.6;
        if (!possuiIluminacaoAdequada(imagem)) qualidade -= 0.4;
        if (!possuiPosicionamentoAdequado(imagem)) qualidade -= 0.3;
        if (ImageValidator.isImagemBaixaQualidade(imagem)) qualidade -= 0.3;

        // Simulação com base no tamanho da string (representando resolução)
        if (imagem.length() < 1000) qualidade -= 0.3;
        else if (imagem.length() < 3000) qualidade -= 0.1;

        // Garantir que o valor está entre 0.0 e 1.0
        return Math.min(1.0, Math.max(0.0, qualidade));
    }
}