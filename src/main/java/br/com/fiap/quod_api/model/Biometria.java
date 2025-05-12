package br.com.fiap.quod_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "biometrias")
public class Biometria {

    @Id
    private String transacaoId;
    private String tipo;
    private String imagem;
    private LocalDateTime dataCaptura;
    private String tipoFraude;
    private Dispositivo dispositivo;
    private List<String> canalNotificacao;
    private String notificadoPor;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Dispositivo {
        private String fabricante;
        private String modelo;
        private String sistemaOperacional;
    }
}