package br.com.fiap.quod_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidacaoResultadoDTO {

    @NotBlank(message = "O ID da transação é obrigatório")
    private String transacaoId;

    @NotBlank(message = "O tipo de biometria é obrigatório")
    private String tipoBiometria;

    @NotNull(message = "A data de captura é obrigatória")
    private LocalDateTime dataCaptura;

    @NotBlank(message = "O campo notificadoPor é obrigatório")
    private String notificadoPor;
}