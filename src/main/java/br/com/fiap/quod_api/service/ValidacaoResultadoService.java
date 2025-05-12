package br.com.fiap.quod_api.service;

import br.com.fiap.quod_api.dto.ValidacaoResultadoDTO;
import br.com.fiap.quod_api.model.ValidacaoResultado;
import br.com.fiap.quod_api.repository.ValidacaoResultadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ValidacaoResultadoService {

    private final ValidacaoResultadoRepository repository;

    @Autowired
    public ValidacaoResultadoService(ValidacaoResultadoRepository repository) {
        this.repository = repository;
    }

    public ValidacaoResultado salvar(ValidacaoResultadoDTO dto) {
        ValidacaoResultado validacao = new ValidacaoResultado();
        validacao.setTransacaoId(dto.getTransacaoId());
        validacao.setTipoBiometria(dto.getTipoBiometria());
        validacao.setDataCaptura(dto.getDataCaptura());
        validacao.setNotificadoPor(dto.getNotificadoPor());

        return repository.save(validacao);
    }

    public Optional<ValidacaoResultado> buscarPorId(String id) {
        return repository.findById(id);
    }

    public List<ValidacaoResultado> listarTodos() {
        return repository.findAll();
    }
}