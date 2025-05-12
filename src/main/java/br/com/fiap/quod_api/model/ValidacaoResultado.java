package br.com.fiap.quod_api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "validacoes")
public class ValidacaoResultado {

    @Id
    private String transacaoId;
    private String tipoBiometria;
    private LocalDateTime dataCaptura;
    private String notificadoPor;

    // Construtor padrão
    public ValidacaoResultado() {
    }

    // Construtor com argumentos
    public ValidacaoResultado(String transacaoId, String tipoBiometria, LocalDateTime dataCaptura, String notificadoPor) {
        this.transacaoId = transacaoId;
        this.tipoBiometria = tipoBiometria;
        this.dataCaptura = dataCaptura;
        this.notificadoPor = notificadoPor;
    }

    // Getters e Setters
    public String getTransacaoId() {
        return transacaoId;
    }

    public void setTransacaoId(String transacaoId) {
        this.transacaoId = transacaoId;
    }

    public String getTipoBiometria() {
        return tipoBiometria;
    }

    public void setTipoBiometria(String tipoBiometria) {
        this.tipoBiometria = tipoBiometria;
    }

    public LocalDateTime getDataCaptura() {
        return dataCaptura;
    }

    public void setDataCaptura(LocalDateTime dataCaptura) {
        this.dataCaptura = dataCaptura;
    }

    public String getNotificadoPor() {
        return notificadoPor;
    }

    public void setNotificadoPor(String notificadoPor) {
        this.notificadoPor = notificadoPor;
    }
}