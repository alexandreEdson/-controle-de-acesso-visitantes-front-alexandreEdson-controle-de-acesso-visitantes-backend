# controle-de-acesso-visitantes

baixar o mvn (Apache Maven) em `https://maven.apache.org/download.cgi`
Instalar variaveis de ambiente do maven

baixar o MySQL em `https://dev.mysql.com/downloads/`

baixar o Java versao 17
    #dica: por meio do site zulo voce nao vai precisar instalar variavel de ambiente para o java#

# edite o arquivo "application.properties"
    Voce vai precisar usar seu usuario e senha do mysql e substituir a porta do localhost:?? conforme o que estiver utilizando

        spring.datasource.url=jdbc:mysql://localhost:3306/SofthomeLiving?createDatabaseIfNotExist=true&useSSL=false
        spring.datasource.username=
        spring.datasource.password=

# apos instalado o mvn, o MySQL e o java use os comandos:
        mvn -v                                  -- para verificar se o mvn foi instalado corretamente
        java --version                          -- para verificar se o java foi instalado corretamente
        mvn clean install                       -- isso vai atualizar as dependencias necessarias do projeto
        
        executar o projeto por meio do servlet TomCat (Usado no Projeto de criaca o Ambiente Spring ToolSuite for Eclipse)