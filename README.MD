# QUOD API - Sistema de Validação Biométrica e Documental

## Visão Geral

QUOD API é um sistema de validação de biometrias faciais e documentos digitais, desenvolvido com Spring Boot e MongoDB. A aplicação oferece endpoints RESTful para cadastro, consulta e validação de biometrias faciais, com foco em detecção de fraudes e análise de qualidade.

## Arquitetura (Modelo C4)

### Nível 1: Contexto do Sistema

O sistema QUOD API é uma solução que permite:
- Cadastro e validação de biometrias faciais
- Análise de qualidade de imagens
- Detecção de possíveis fraudes (spoofing, deepfake)
- Integração com sistemas externos via API REST

**Usuários:**
- Aplicações cliente que precisam validar biometrias
- Sistemas de segurança e autenticação

### Nível 2: Containers

A solução é composta por:

| Container | Tecnologia | Responsabilidade |
|-----------|------------|------------------|
| API REST | Spring Boot | Receber e processar requisições |
| Banco de Dados | MongoDB Atlas | Armazenar biometrias e documentos |

### Nível 3: Componentes

A aplicação Spring Boot contém os seguintes componentes principais:

- **Controllers**: Camada de API que expõe endpoints REST
    - `BiometriaController`: Gerencia operações relacionadas a biometrias

- **Services**: Camada de negócio
    - `BiometriaService`: Implementa validações e regras de negócio

- **Repositories**: Acesso a dados
    - `BiometriaRepository`: Gerencia persistência de biometrias

- **Validators**: Componentes de validação
    - `BiometriaValidator`: Lógica de validação de biometrias faciais
    - `ImageValidator`: Validação de qualidade e formato de imagens

### Nível 4: Código

Estrutura de pacotes da aplicação:

```
br.com.fiap.quod_api
  ├── controller
  │     ├── BiometriaController.java
  │     └── DocumentoController.java
  ├── service
  │     ├── BiometriaService.java
  │     └── DocumentoService.java
  ├── repository
  │     ├── BiometriaRepository.java
  │     └── DocumentoRepository.java
  ├── model
  │     ├── Biometria.java
  │     └── Documento.java
  ├── dto
  │     ├── BiometriaDTO.java
  │     └── ValidacaoBiometriaResultadoDTO.java
  ├── utils
  │     ├── BiometriaValidator.java
  │     └── ImageValidator.java
  └── config
        └── MongoConfig.java
```

## Tecnologias e Bibliotecas

| Biblioteca | Versão | Descrição | Uso no Projeto |
|------------|--------|-----------|----------------|
| Spring Boot | 3.x | Framework Java para desenvolvimento de aplicações | Base do projeto, criação de API REST |
| Spring Data MongoDB | 3.x | Integração com MongoDB | Persistência e consulta de dados |
| Jakarta Validation | 3.x | Validação de dados | Validação dos DTOs de entrada |
| Lombok | 1.18.x | Redução de código boilerplate | Geração de getters, setters e construtores |
| MongoDB Java Driver | 4.x | Driver oficial para MongoDB | Conexão com o banco de dados |
| SLF4J + Logback | - | Framework de logging | Registro de logs da aplicação |
| Gradle | 7.x+ | Ferramenta de build | Gerenciamento de dependências e build |

## Funcionalidades Principais

### Gestão de Biometrias Faciais

1. **Cadastro de Biometria**
    - Endpoint: `POST /biometrias`
    - Recebe dados da biometria, dispositivo e outros metadados

2. **Consulta de Biometria**
    - Endpoint: `GET /biometrias/{id}`
    - Retorna detalhes de uma biometria específica

3. **Listagem de Biometrias**
    - Endpoint: `GET /biometrias`
    - Retorna todas as biometrias cadastradas

4. **Validação de Biometria**
    - Endpoint: `GET /biometrias/{id}/validar`
    - Analisa a biometria e fornece resultados detalhados de validação

### Análise Biométrica

O sistema realiza as seguintes validações:

- **Qualidade da Imagem**
    - Resolução
    - Nitidez
    - Formato adequado

- **Detecção Facial**
    - Presença de face detectável
    - Posicionamento adequado
    - Iluminação suficiente

- **Detecção de Fraude**
    - Análise de spoofing (uso de fotos, máscaras)
    - Detecção de deepfake
    - Outros tipos de fraude

## Configuração do MongoDB

A aplicação utiliza MongoDB Atlas como banco de dados, com as seguintes configurações:

- **Cluster**: MongoDB Atlas Cloud
- **Pool de Conexões**: Configuração otimizada (10-50 conexões)
- **Timeouts**: Valores configuráveis para operações de leitura e escrita
- **Collections**: Separação entre biometrias e documentos

## Como Testar

### Exemplos de Requisições

#### 1. Cadastrar Biometria

```bash
curl -X POST http://localhost:8080/biometrias \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "FACE",
    "imagem": "base64_encoded_image_data",
    "dataCaptura": "2024-05-15T14:30:45",
    "dispositivo": {
      "fabricante": "Samsung",
      "modelo": "Galaxy S22",
      "sistemaOperacional": "Android 13"
    },
    "canalNotificacao": ["email", "sms"],
    "notificadoPor": "app_mobile"
  }'
```

#### 2. Validar Biometria

```bash
curl -X GET http://localhost:8080/biometrias/{id}/validar
```

#### 3. Consultar Biometria

```bash
curl -X GET http://localhost:8080/biometrias/{id}
```

### Resposta de Validação

```json
{
  "valido": true,
  "biometriaId": "64a8b2c3d1e0f4a5b6c7d8e9",
  "mensagem": "Biometria válida",
  "erros": [],
  "tipoFraude": null,
  "scoreFraude": 0.0,
  "scoreConfianca": 0.95,
  "scoreQualidade": 0.87
}
```

## Considerações de Segurança

É importante notar que o projeto atual tem credenciais de banco de dados no arquivo `application.properties`. Em ambientes de produção, recomenda-se:

1. Usar variáveis de ambiente ou um serviço de configuração seguro
2. Implementar rotação de credenciais
3. Restringir permissões do usuário do banco de dados
4. Considerar a implementação de autenticação por certificado

## Próximas Etapas

- Implementação de testes automatizados
- Adicionar autenticação aos endpoints
- Expandir a validação para outros tipos biométricos
- Implementar análise estatística de resultados

---

Este projeto foi desenvolvido para demonstrar capacidades de validação biométrica usando tecnologias modernas de desenvolvimento Java.