package br.com.fiap.quod_api.service;

import br.com.fiap.quod_api.dto.BiometriaDTO;
import br.com.fiap.quod_api.dto.ValidacaoBiometriaResultadoDTO;
import br.com.fiap.quod_api.model.Biometria;
import br.com.fiap.quod_api.repository.BiometriaRepository;
import br.com.fiap.quod_api.utils.BiometriaValidator;
import br.com.fiap.quod_api.utils.ImageValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BiometriaService {

    private static final Logger logger = LoggerFactory.getLogger(BiometriaService.class);

    private final BiometriaRepository repository;

    @Autowired
    public BiometriaService(BiometriaRepository repository) {
        this.repository = repository;
    }

    public Biometria salvar(BiometriaDTO dto) {
        Biometria biometria = converterParaEntidade(dto);
        return repository.save(biometria);
    }

    public Optional<Biometria> buscarPorId(String id) {
        return repository.findById(id);
    }

    public List<Biometria> listarTodos() {
        return repository.findAll();
    }

    public ValidacaoBiometriaResultadoDTO validarBiometria(String id) {
        Optional<Biometria> biometriaOpt = repository.findById(id);

        if (biometriaOpt.isEmpty()) {
            return ValidacaoBiometriaResultadoDTO.builder()
                    .valido(false)
                    .mensagem("Biometria não encontrada")
                    .build();
        }

        Biometria biometria = biometriaOpt.get();
        ValidacaoBiometriaResultadoDTO.ValidacaoBiometriaResultadoDTOBuilder resultadoBuilder =
                ValidacaoBiometriaResultadoDTO.builder()
                        .biometriaId(biometria.getTransacaoId());

        List<String> erros = new ArrayList<>();
        boolean valido = true;
        double scoreQualidade = 1.0;
        double scoreConfianca = 1.0;

        // Validação do formato da imagem
        if (!ImageValidator.isFormatoValido(biometria.getImagem())) {
            erros.add("Formato de imagem biométrica inválido");
            valido = false;
            scoreQualidade -= 0.3;
            scoreConfianca -= 0.3;
        }

        // Validação da qualidade da imagem
        if (ImageValidator.isImagemBaixaQualidade(biometria.getImagem())) {
            erros.add("Imagem biométrica com baixa qualidade");
            scoreQualidade -= 0.5;
            scoreConfianca -= 0.2;
            if (scoreQualidade < 0.4) {
                valido = false;
            }
        }

        // Verificações específicas de biometria facial
        if (!BiometriaValidator.possuiFaceDetectavel(biometria.getImagem())) {
            erros.add("Face não detectável na imagem");
            valido = false;
            scoreConfianca -= 0.5;
        }

        if (!BiometriaValidator.possuiIluminacaoAdequada(biometria.getImagem())) {
            erros.add("Iluminação inadequada para reconhecimento facial");
            scoreQualidade -= 0.3;
            if (scoreQualidade < 0.4) {
                valido = false;
            }
        }

        if (!BiometriaValidator.possuiPosicionamentoAdequado(biometria.getImagem())) {
            erros.add("Posicionamento facial inadequado");
            scoreQualidade -= 0.3;
            scoreConfianca -= 0.2;
            if (scoreQualidade < 0.4) {
                valido = false;
            }
        }

        // Verificação de fraude
        double scoreFraude = 0.0;
        String tipoFraude = null;

        if (biometria.getTipoFraude() != null) {
            tipoFraude = biometria.getTipoFraude();
            scoreFraude = 0.8;
            valido = false;
            erros.add("Fraude detectada: " + tipoFraude);
        }

        if (BiometriaValidator.isDetectadoSpoofing(biometria.getImagem())) {
            erros.add("Possível spoofing detectado");
            if (tipoFraude == null) {
                tipoFraude = "spoofing";
            }
            scoreFraude = Math.max(scoreFraude, 0.7);
            valido = false;
        }

        if (BiometriaValidator.isDetectadoDeepfake(biometria.getImagem())) {
            erros.add("Possível deepfake detectado");
            if (tipoFraude == null) {
                tipoFraude = "deepfake";
            }
            scoreFraude = Math.max(scoreFraude, 0.8);
            valido = false;
        }

        // Ajusta scores
        scoreFraude = Math.min(1.0, Math.max(0.0, scoreFraude));
        scoreQualidade = Math.min(1.0, Math.max(0.0, scoreQualidade));
        scoreConfianca = Math.min(1.0, Math.max(0.0, scoreConfianca));

        String mensagem = valido ?
                "Biometria válida" :
                "Biometria inválida. Verificar erros para mais detalhes.";

        return resultadoBuilder
                .valido(valido)
                .mensagem(mensagem)
                .erros(erros)
                .tipoFraude(tipoFraude)
                .scoreFraude(scoreFraude)
                .scoreQualidade(scoreQualidade)
                .scoreConfianca(scoreConfianca)
                .build();
    }

    private Biometria converterParaEntidade(BiometriaDTO dto) {
        Biometria biometria = new Biometria();

        biometria.setTipo(dto.getTipo());
        biometria.setImagem(dto.getImagem());
        biometria.setDataCaptura(dto.getDataCaptura());
        biometria.setCanalNotificacao(dto.getCanalNotificacao());
        biometria.setNotificadoPor(dto.getNotificadoPor());

        // Converter dispositivo se existir
        if (dto.getDispositivo() != null) {
            Biometria.Dispositivo dispositivo = new Biometria.Dispositivo(
                    dto.getDispositivo().getFabricante(),
                    dto.getDispositivo().getModelo(),
                    dto.getDispositivo().getSistemaOperacional()
            );
            biometria.setDispositivo(dispositivo);
        }

        return biometria;
    }
}