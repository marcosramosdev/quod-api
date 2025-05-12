package br.com.fiap.quod_api.repository;

import br.com.fiap.quod_api.model.ValidacaoResultado;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ValidacaoResultadoRepository extends MongoRepository<ValidacaoResultado, String> {
}