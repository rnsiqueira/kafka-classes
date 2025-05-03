# Kafka Classes

**Kafka Classes** é um projeto educacional desenvolvido em Java 11 que demonstra a implementação de microsserviços utilizando Apache Kafka como sistema de mensageria. Este repositório serve como material de estudo para compreender a arquitetura orientada a eventos e a integração entre serviços através de tópicos Kafka.

## 📚 Visão Geral

O projeto é composto por diversos módulos que simulam serviços independentes comunicando-se via Kafka. Cada módulo representa um componente específico dentro de uma arquitetura de microsserviços, permitindo explorar conceitos como produção e consumo de mensagens, serialização/deserialização, e processamento de eventos.

## 🧱 Estrutura do Projeto

O repositório está organizado da seguinte forma:

* `kafka-commons/` – Contém classes e configurações comuns utilizadas pelos demais módulos.
* `kafka-email-service/` – Serviço responsável por simular o envio de e-mails.
* `kafka-generate-report/` – Gera relatórios baseados em eventos recebidos.
* `kafka-http-server/` – Exposição de endpoints HTTP para interações externas.
* `kafka-logs/` – Serviço dedicado ao registro e armazenamento de logs.
* `kafka-products/` – Gerencia informações relacionadas a produtos.
* `kafka-send-messages/` – Produtor de mensagens para tópicos Kafka.
* `kafka-users/` – Gerencia dados de usuários.

Cada módulo é um projeto Maven independente, facilitando o entendimento isolado de cada componente.

## 🚀 Tecnologias Utilizadas

* **Java 11**
* **Apache Kafka**
* **Spring Boot** *(presumido com base na estrutura do projeto)*
* **Maven** para gerenciamento de dependências

## 🛠️ Como Executar

1. **Pré-requisitos**:

   * Java 11 instalado
   * Apache Kafka em execução localmente ou acessível remotamente
   * Maven instalado

2. **Clonar o repositório**:

   ```bash
   git clone https://github.com/rnsiqueira/kafka-classes.git
   cd kafka-classes
   ```

3. **Compilar os módulos**:

   ```bash
   mvn clean install
   ```

4. **Executar os serviços**: Navegue até o diretório de cada módulo e execute:

   ```bash
   mvn spring-boot:run
   ```

   Certifique-se de iniciar os serviços na ordem adequada, se houver dependências entre eles.

## 📄 Licença

Este projeto é destinado exclusivamente para fins educacionais e de estudo. Sinta-se à vontade para explorar, modificar e utilizar como base para aprendizado.

## 🤝 Contribuições

Contribuições são bem-vindas! Se você deseja melhorar este projeto, por favor, abra uma issue ou envie um pull request.

## 📢 Contato

Para dúvidas ou sugestões, entre em contato com [Rafael N. Siqueira](https://github.com/rnsiqueira).
