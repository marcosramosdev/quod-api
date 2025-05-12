package br.com.fiap.quod_api.controller;

import br.com.fiap.quod_api.dto.ValidacaoResultadoDTO;
import br.com.fiap.quod_api.model.ValidacaoResultado;
import br.com.fiap.quod_api.service.ValidacaoResultadoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/validacoes")
public class ValidacaoResultadoController {

    private final ValidacaoResultadoService service;

    @Autowired
    public ValidacaoResultadoController(ValidacaoResultadoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ValidacaoResultado> criar(@Valid @RequestBody ValidacaoResultadoDTO dto) {
        ValidacaoResultado resultado = service.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ValidacaoResultado> buscarPorId(@PathVariable String id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ValidacaoResultado>> listarTodos() {
        List<ValidacaoResultado> validacoes = service.listarTodos();
        return ResponseEntity.ok(validacoes);
    }
}