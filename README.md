 ## 🚀 Tecnologias Utilizadas
 
| Tecnologia | Descrição |
|---|---|
| **Java** | Linguagem principal |
| **Spring Boot** | Framework para o backend/API REST |
| **Lombok** | Redução de boilerplate com anotações |
| **JPA / Hibernate** | Mapeamento objeto-relacional (ORM) |
| **PostgreSQL** | Banco de dados relacional |
| **Prometheus** | Coleta de métricas da aplicação |
| **Grafana** | Visualização e dashboards de métricas |
| **Redis** | Cache em memória |
| **Swing** | Interface gráfica desktop |
| **FlatLaf** | Look and Feel moderno para o Swing |
| **MigLayout** | Gerenciador de layout para o Swing |
| **Docker** | Containerização do ambiente |
| **JUnit** | Criação de testes unitários |
| **Mockito** | Mock das classes para testes |
 
---
 
## 📋 Pré-requisitos
 
- Java 25+
- Docker e Docker Compose
---
 
## ⚙️ Como Executar
 
### 1. Clonar o repositório
 
```bash
git clone https://github.com/Frasato/library-system.git
```
 
### 2. Subir o ambiente com Docker
 
```bash
docker-compose up --build
```
 
Isso irá iniciar:
- PostgreSQL
- Prometheus
- Grafana
- Redis
- Build API
 
---

## 📚 Gerar Documentação
 
### 1. Rode o comando
 
```bash
./mvnw javadoc:javadoc
```

### 2. Entre na pasta
 
```bash
/target/reports/apidocs/index.html
```

---
 
## 🔌 Endpoints da API
 
> Base URL: `http://localhost:8080/v1/book`
 
| Método | Endpoint | Descrição | Parâmetros | Retorno |
|---|---|---|---|---|
| `GET` | ``        | Lista todos os livros  | — | `200 OK` + lista em JSON |
| `GET` | `/{isbn}` | Busca livro por ISBN code| `isbn` (path) | `200 OK` + objeto JSON |
| `POST` | `/create`| Cria novo livro | (body) JSON `String isbn` | `201 Created` + objeto JSON |
| `POST` | `/import` | Cria ou atualiza livro | (Param) Multipart `file` | `200 OK` + objeto JSON |
| `PATCH` | `/{id}` | Atualiza livro | `id` (path) + (body) JSON | `200 OK` |
| `DELETE` | `/delete/{id}` | Remove livro | `id` (path) | `200 OK` + objeto JSON |

---
 
## 📊 Monitoramento
 
### Prometheus
 
As métricas da aplicação são expostas automaticamente pelo Spring Boot Actuator em:
 
```
http://localhost:8080/actuator/prometheus
```
 
O Prometheus é configurado para coletar essas métricas e está disponível em:
 
```
http://localhost:9595
```
 
### Grafana
 
O Grafana está disponível em:
 
```
http://localhost:3030
```
 
**Credenciais padrão:** `admin` / `admin`
 
#### Configurar o Datasource Prometheus
 
1. Acesse **Connections → Data Sources**
2. Clique em **Add data source**
3. Selecione **Prometheus**
4. Defina a URL: `http://prometheus:9090`
5. Clique em **Save & Test**

#### Importar o Dashboard
 
1. Acesse **Dashboards → Import Dashboard**
2. Insira o ID do dashboard: **`12900`**
3. Selecione o datasource Prometheus configurado
4. Clique em **Import**