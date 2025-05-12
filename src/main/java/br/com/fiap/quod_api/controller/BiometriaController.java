package br.com.fiap.quod_api.controller;

import br.com.fiap.quod_api.dto.BiometriaDTO;
import br.com.fiap.quod_api.dto.ValidacaoBiometriaResultadoDTO;
import br.com.fiap.quod_api.model.Biometria;
import br.com.fiap.quod_api.service.BiometriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/biometrias")
public class BiometriaController {

    private final BiometriaService biometriaService;

    @Autowired
    public BiometriaController(BiometriaService biometriaService) {
        this.biometriaService = biometriaService;
    }

    @PostMapping
    public ResponseEntity<Biometria> cadastrar(@Valid @RequestBody BiometriaDTO biometriaDTO) {
        Biometria biometriaSalva = biometriaService.salvar(biometriaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(biometriaSalva);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Biometria> buscarPorId(@PathVariable String id) {
        Optional<Biometria> biometria = biometriaService.buscarPorId(id);
        return biometria.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Biometria>> listarTodos() {
        List<Biometria> biometrias = biometriaService.listarTodos();
        return ResponseEntity.ok(biometrias);
    }

    @GetMapping("/{id}/validar")
    public ResponseEntity<ValidacaoBiometriaResultadoDTO> validarBiometria(@PathVariable String id) {
        ValidacaoBiometriaResultadoDTO resultado = biometriaService.validarBiometria(id);

        if (resultado.getMensagem().contains("não encontrada")) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(resultado);
    }
}