# projeto-cinema
Projeto de sistema que controlará a operação comercial de um cinema moderno.

Pré-requisitos

- **Java JDK 11+**
- **Maven** (para compilar)
- **MySQL** (banco de dados)
- **Apache Tomcat 7+** (ou usar Maven Tomcat Plugin)
- **VS Code** com Live Server (ou navegador)

# BACK-END
Entrar na pasta do backend
cd back-end

# Configurar senha do MySQL
- Edite: src/main/java/com/cinema/util/DatabaseConnection.java 
- Altere: private static final String PASSWORD = "sua_senha";

# Compilar e iniciar servidor
mvn clean install
mvn tomcat7:run
