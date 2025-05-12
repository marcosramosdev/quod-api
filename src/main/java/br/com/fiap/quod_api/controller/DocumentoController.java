package br.com.fiap.quod_api.controller;

import br.com.fiap.quod_api.dto.DocumentoDTO;
import br.com.fiap.quod_api.dto.ValidacaoDocumentoResultadoDTO;
import br.com.fiap.quod_api.model.Documento;
import br.com.fiap.quod_api.service.DocumentoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/documentos")
public class DocumentoController {

    private final DocumentoService documentoService;

    @Autowired
    public DocumentoController(DocumentoService documentoService) {
        this.documentoService = documentoService;
    }

    @PostMapping
    public ResponseEntity<Documento> cadastrar(@Valid @RequestBody DocumentoDTO documentoDTO) {
        Documento documentoSalvo = documentoService.salvar(documentoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(documentoSalvo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Documento> buscarPorId(@PathVariable String id) {
        Optional<Documento> documento = documentoService.buscarPorId(id);
        return documento.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Documento>> listarTodos() {
        List<Documento> documentos = documentoService.listarTodos();
        return ResponseEntity.ok(documentos);
    }

    @GetMapping("/{id}/validar")
    public ResponseEntity<ValidacaoDocumentoResultadoDTO> validarDocumento(@PathVariable String id) {
        ValidacaoDocumentoResultadoDTO resultado = documentoService.validarDocumento(id);

        if (resultado.getMensagem().contains("não encontrado")) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(resultado);
    }
}