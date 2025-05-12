package br.com.fiap.quod_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidacaoBiometriaResultadoDTO {
    private boolean valido;
    private String biometriaId;
    private String mensagem;

    @Builder.Default
    private List<String> erros = new ArrayList<>();

    private String tipoFraude;
    private Double scoreFraude;
    private Double scoreConfianca;
    private Double scoreQualidade;
}