package br.com.fiap.quod_api.repository;

import br.com.fiap.quod_api.model.Biometria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiometriaRepository extends MongoRepository<Biometria, String> {
}