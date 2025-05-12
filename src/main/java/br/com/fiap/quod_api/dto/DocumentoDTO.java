package br.com.fiap.quod_api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentoDTO {

    @NotBlank(message = "O tipo de documento é obrigatório")
    private String tipo;

    @NotBlank(message = "O número do documento é obrigatório")
    private String numero;

    @NotBlank(message = "O órgão emissor é obrigatório")
    private String orgaoEmissor;

    @NotBlank(message = "A UF do emissor é obrigatória")
    private String ufEmissor;

    @NotNull(message = "A data de emissão é obrigatória")
    private LocalDate dataEmissao;

    @NotBlank(message = "A imagem do documento é obrigatória")
    private String imagemDocumento;

    @NotBlank(message = "A imagem da face é obrigatória")
    private String imagemFace;

    @NotNull(message = "A data de captura é obrigatória")
    private LocalDateTime dataCaptura;

    private String tipoFraude;

    @NotBlank(message = "O nome da pessoa é obrigatório")
    private String nomePessoa;

    @NotBlank(message = "O CPF da pessoa é obrigatório")
    private String cpfPessoa;

    @NotNull(message = "A data de nascimento é obrigatória")
    private LocalDate dataNascimento;

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