package br.com.fiap.quod_api.service;

import br.com.fiap.quod_api.dto.DocumentoDTO;
import br.com.fiap.quod_api.dto.ValidacaoDocumentoResultadoDTO;
import br.com.fiap.quod_api.model.Documento;
import br.com.fiap.quod_api.repository.DocumentoRepository;
import br.com.fiap.quod_api.utils.ImageValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentoService {

    private static final Logger logger = LoggerFactory.getLogger(DocumentoService.class);

    private final DocumentoRepository repository;

    @Autowired
    public DocumentoService(DocumentoRepository repository) {
        this.repository = repository;
    }

    public Documento salvar(DocumentoDTO dto) {
        Documento documento = converterParaEntidade(dto);

        // Validação do formato da imagem
        if (!ImageValidator.isFormatoValido(dto.getImagemDocumento()) ||
                !ImageValidator.isFormatoValido(dto.getImagemFace())) {
            logger.error("Formato de imagem inválido para documento: {}", dto.getNumero());
            throw new IllegalArgumentException("Formato de imagem inválido. Formatos aceitos: jpg, jpeg, png");
        }

        // Detecção de fraude usando ImageValidator
        if (ImageValidator.isFraudeSimulada(dto.getImagemDocumento()) ||
                ImageValidator.isFraudeSimulada(dto.getImagemFace())) {

            String tipoFraude = null;

            if (ImageValidator.isFraudeSimulada(dto.getImagemDocumento())) {
                tipoFraude = ImageValidator.detectarTipoFraudeSimulada(dto.getImagemDocumento());
            } else {
                tipoFraude = ImageValidator.detectarTipoFraudeSimulada(dto.getImagemFace());
            }

            documento.setTipoFraude(tipoFraude);
            logger.warn("Fraude detectada para documento: {} - Tipo: {}", dto.getNumero(), tipoFraude);

            // Verificação de imagens idênticas (possível duplicação)
            if (ImageValidator.imagensIguais(dto.getImagemDocumento(), dto.getImagemFace())) {
                logger.warn("Possível fraude: imagens de documento e face idênticas no documento: {}", dto.getNumero());
                if (tipoFraude == null) {
                    documento.setTipoFraude("duplicação de imagem");
                }
            }

            // Simulação do envio de notificação
            if (documento.getCanalNotificacao() != null && !documento.getCanalNotificacao().isEmpty()) {
                logger.info("Enviando notificação de fraude para os canais: {}",
                        documento.getCanalNotificacao());
            }
        }

        return repository.save(documento);
    }

    public Optional<Documento> buscarPorId(String id) {
        return repository.findById(id);
    }

    public List<Documento> listarTodos() {
        return repository.findAll();
    }

    private Documento converterParaEntidade(DocumentoDTO dto) {
        Documento documento = new Documento();

        // Campos do documento
        documento.setTipo(dto.getTipo());
        documento.setNumero(dto.getNumero());
        documento.setOrgaoEmissor(dto.getOrgaoEmissor());
        documento.setUfEmissor(dto.getUfEmissor());
        documento.setDataEmissao(dto.getDataEmissao());

        // Campos de imagem e captura
        documento.setImagemDocumento(dto.getImagemDocumento());
        documento.setImagemFace(dto.getImagemFace());
        documento.setDataCaptura(dto.getDataCaptura());

        // Campos da pessoa
        documento.setNomePessoa(dto.getNomePessoa());
        documento.setCpfPessoa(dto.getCpfPessoa());
        documento.setDataNascimento(dto.getDataNascimento());

        // Outros campos
        documento.setNotificadoPor(dto.getNotificadoPor());
        documento.setCanalNotificacao(dto.getCanalNotificacao());

        // Converter dispositivo se existir
        if (dto.getDispositivo() != null) {
            Documento.Dispositivo dispositivo = new Documento.Dispositivo(
                    dto.getDispositivo().getFabricante(),
                    dto.getDispositivo().getModelo(),
                    dto.getDispositivo().getSistemaOperacional()
            );
            documento.setDispositivo(dispositivo);
        }

        // O tipoFraude é definido pela validação, não vem do DTO

        return documento;
    }

    public ValidacaoDocumentoResultadoDTO validarDocumento(String id) {
        Optional<Documento> documentoOpt = repository.findById(id);

        if (documentoOpt.isEmpty()) {
            return ValidacaoDocumentoResultadoDTO.builder()
                    .valido(false)
                    .mensagem("Documento não encontrado")
                    .build();
        }

        Documento documento = documentoOpt.get();
        ValidacaoDocumentoResultadoDTO.ValidacaoDocumentoResultadoDTOBuilder resultadoBuilder =
                ValidacaoDocumentoResultadoDTO.builder()
                        .documentoId(documento.getId());

        List<String> erros = new ArrayList<>();
        boolean valido = true;
        double scoreAutenticidade = 1.0;

        // Validação do formato da imagem
        if (!ImageValidator.isFormatoValido(documento.getImagemDocumento())) {
            erros.add("Formato de imagem do documento inválido");
            valido = false;
            scoreAutenticidade -= 0.2;
        }

        if (!ImageValidator.isFormatoValido(documento.getImagemFace())) {
            erros.add("Formato de imagem da face inválido");
            valido = false;
            scoreAutenticidade -= 0.2;
        }

        // Validação da qualidade da imagem
        if (ImageValidator.isImagemBaixaQualidade(documento.getImagemDocumento())) {
            erros.add("Imagem do documento com baixa qualidade");
            scoreAutenticidade -= 0.1;
        }

        if (ImageValidator.isImagemBaixaQualidade(documento.getImagemFace())) {
            erros.add("Imagem da face com baixa qualidade");
            scoreAutenticidade -= 0.1;
        }

        // Verificação de fraude
        boolean temFraude = false;
        double scoreFraude = 0.0;
        String tipoFraude = null;

        if (documento.getTipoFraude() != null) {
            temFraude = true;
            tipoFraude = documento.getTipoFraude();
            scoreFraude = 0.8;
            valido = false;
            erros.add("Fraude detectada: " + tipoFraude);
        }

        if (ImageValidator.imagensIguais(documento.getImagemDocumento(), documento.getImagemFace())) {
            erros.add("Possível fraude: imagens de documento e face idênticas");
            if (tipoFraude == null) {
                tipoFraude = "duplicação de imagem";
            }
            scoreFraude = Math.max(scoreFraude, 0.7);
            valido = false;
        }

        // Validação dados do documento
        if (!validarDocumentoPorTipo(documento, erros)) {
            scoreAutenticidade -= 0.2;
            valido = false;
        }

        // Verificação de idade compatível com documento
        if (!isIdadeCompativel(documento)) {
            erros.add("Idade incompatível com data de emissão do documento");
            scoreAutenticidade -= 0.15;
        }

        // Verificação de consistência dos dados
        if (!isDadosConsistentes(documento)) {
            erros.add("Dados inconsistentes");
            scoreAutenticidade -= 0.15;
        }

        // Ajusta scores
        scoreFraude = Math.min(1.0, Math.max(0.0, scoreFraude));
        scoreAutenticidade = Math.min(1.0, Math.max(0.0, scoreAutenticidade));

        String mensagem = valido ?
                "Documento válido" :
                "Documento inválido. Verificar erros para mais detalhes.";

        return resultadoBuilder
                .valido(valido)
                .mensagem(mensagem)
                .erros(erros)
                .tipoFraude(tipoFraude)
                .scoreFraude(scoreFraude)
                .scoreAutenticidade(scoreAutenticidade)
                .build();
    }

    // Validações auxiliares
    private boolean validarDocumentoPorTipo(Documento documento, List<String> erros) {
        boolean valido = true;
        switch (documento.getTipo().toUpperCase()) {
            case "RG":
                if (!isRgValido(documento.getNumero())) {
                    erros.add("Número de RG inválido");
                    valido = false;
                }
                break;
            case "CNH":
                if (!isCnhValida(documento.getNumero())) {
                    erros.add("Número de CNH inválido");
                    valido = false;
                }
                break;
            case "PASSAPORTE":
                if (!isPassaporteValido(documento.getNumero())) {
                    erros.add("Número de passaporte inválido");
                    valido = false;
                }
                break;
            default:
                erros.add("Tipo de documento não suportado para validação");
                valido = false;
        }
        return valido;
    }

    private boolean isRgValido(String numero) {
        // Implementação simplificada - em produção teria uma validação mais completa
        numero = numero.replaceAll("[^0-9]", "");
        return numero.length() >= 7 && numero.length() <= 12;
    }

    private boolean isCnhValida(String numero) {
        numero = numero.replaceAll("[^0-9]", "");
        return numero.length() == 11;
    }

    private boolean isPassaporteValido(String numero) {
        // Passaporte brasileiro geralmente tem 2 letras seguidas de 6 números
        return numero.matches("[A-Z]{2}[0-9]{6}");
    }

    private boolean isIdadeCompativel(Documento documento) {
        if (documento.getDataNascimento() == null || documento.getDataEmissao() == null) {
            return false;
        }

        // Verificando se a data de emissão é posterior à data de nascimento
        return !documento.getDataEmissao().isBefore(documento.getDataNascimento().atStartOfDay().toLocalDate());
    }

    private boolean isDadosConsistentes(Documento documento) {
        // Verifica consistência básica, como nome não vazio, CPF válido, etc.
        if (documento.getNomePessoa() == null || documento.getNomePessoa().trim().isEmpty()) {
            return false;
        }

        if (documento.getCpfPessoa() == null || !isCpfValido(documento.getCpfPessoa())) {
            return false;
        }

        return true;
    }

    private boolean isCpfValido(String cpf) {
        // Implementação simplificada - em produção teria uma validação mais completa
        cpf = cpf.replaceAll("[^0-9]", "");
        return cpf.length() == 11 && !cpf.matches("(\\d)\\1{10}");
    }
}