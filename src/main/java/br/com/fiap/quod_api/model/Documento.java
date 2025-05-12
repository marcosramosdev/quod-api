package br.com.fiap.quod_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "documentos")
public class Documento {

    @Id
    private String id;

    private String tipo;
    private String numero;
    private String orgaoEmissor;
    private String ufEmissor;
    private LocalDate dataEmissao;

    private String imagemDocumento;
    private String imagemFace;

    private LocalDateTime dataCaptura;
    private String tipoFraude;

    private String nomePessoa;
    private String cpfPessoa;
    private LocalDate dataNascimento;

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