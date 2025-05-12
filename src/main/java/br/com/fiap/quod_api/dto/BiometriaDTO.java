package br.com.fiap.quod_api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BiometriaDTO {

    @NotBlank(message = "O tipo de biometria é obrigatório")
    private String tipo;

    @NotBlank(message = "A imagem é obrigatória")
    private String imagem;

    @NotNull(message = "A data de captura é obrigatória")
    private LocalDateTime dataCaptura;

    private String tipoFraude;

    @Valid
    private DispositivoDTO dispositivo;

    private List<String> canalNotificacao;

    private String notificadoPor;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DispositivoDTO {
        private String fabricante;
        private String modelo;
        private String sistemaOperacional;
    }
}