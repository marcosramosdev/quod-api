package br.com.fiap.quod_api.repository;

import br.com.fiap.quod_api.model.Documento;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentoRepository extends MongoRepository<Documento, String> {
}