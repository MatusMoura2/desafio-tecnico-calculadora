# Calculadora de Empréstimos (Desafio Técnico)

Este projeto é uma aplicação Full-Stack (Angular + Spring Boot) construída para simular e gerar cronogramas de empréstimos baseados no **Sistema de Amortização Constante (SAC)**, utilizando juros pro-rata (base comercial de 360 dias).

## 🚀 Como Executar o Projeto (Docker)

A aplicação foi inteiramente "containerizada", o que significa que você não precisa instalar o Java, Node.js ou o banco de dados na sua máquina. Tudo é gerenciado pelo **Docker**.

**Pré-requisitos:** Ter o [Docker](https://docs.docker.com/get-docker/) e o [Docker Compose](https://docs.docker.com/compose/install/) instalados.

1. Clone o repositório e navegue até a pasta raiz do projeto.
2. No seu terminal, execute o seguinte comando:
   ```bash
   docker-compose up -d --build
   ```
3. Aguarde o download e o build (pode levar alguns minutos na primeira execução). 
4. Quando os containers estiverem ativos, acesse:
   - **Frontend (Aplicação Web):** [http://localhost:4200](http://localhost:4200)
   - **Backend API (Swagger):** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

Para parar e remover os containers, execute:
```bash
docker-compose down
```

---

## 🏗️ Decisões Arquiteturais

### Arquitetura Hexagonal (Ports and Adapters)
A API foi desenhada utilizando a **Arquitetura Hexagonal**. Essa escolha visa manter o Domínio (regras de cálculo financeiro) completamente isolado de frameworks e tecnologias externas. 

**Pensando no Futuro:**
Essa separação torna o projeto altamente escalável. Se no futuro for necessário adicionar mensageria (como **Apache Kafka**) para processamento assíncrono de grandes volumes de empréstimos, ou implementar autenticação/autorização robusta com **Spring Security**, isso pode ser feito acoplando novos "Adapters" sem a necessidade de reescrever ou poluir o núcleo (Domain) das regras matemáticas.

### Banco de Dados: PostgreSQL
O banco de dados relacional **PostgreSQL** foi escolhido visando a persistência do histórico completo de simulações.

**Pensando no Futuro (Histórico e Consultas):**
A estrutura atual permite criar uma futura "Tela de Histórico" onde o usuário poderá consultar todas as simulações passadas. Para lidar com a performance de milhares de cálculos salvos no banco, duas estratégias vitais devem ser adotadas:

1. **Paginação (Pagination):** No endpoint de busca do histórico, em vez de carregar milhares de cálculos de uma vez na memória, devemos retornar "Páginas" (ex: 20 registros por página). Isso garante que a requisição seja leve e rápida.
2. **Lazy Loading:** Ao gerenciar os dados no JPA/Hibernate, o carregamento "Preguiçoso" (Lazy Load) será utilizado para carregar a entidade de Empréstimo sem precisar carregar imediatamente as suas 120 parcelas atreladas, acessando o detalhamento (ScheduleRows) apenas quando o usuário clicar em "Ver Detalhes".

---

## 📖 Documentação da API (Swagger)

A API Rest foi totalmente documentada usando a especificação **OpenAPI 3.0**. 
Com os containers rodando, você pode acessar a interface visual do Swagger UI através do endereço:

🔗 **http://localhost:8080/swagger-ui/index.html**

Através do Swagger, é possível visualizar o "Contrato da API", os schemas de entrada (`LoanRequestDTO`), de saída, e até mesmo efetuar simulações de requisições de teste diretamente pelo navegador, sem a necessidade de softwares como Postman ou Insomnia.
