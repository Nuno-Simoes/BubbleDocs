# Bubble Docs SD-ID
# Projecto de Sistemas Distribuidos #

## Primeira entrega ##

Grupo de SD 7

70464 André Andrade

70632 Bruno Serrano

74304 Pedro Nascimento


Repositorio:
[tecnico-softeng-distsys-2015/T_04_07_23-project]
https://github.com/tecnico-softeng-distsys-2015/T_04_07_23-project

## Servico SD-ID

### Instrucoes de instalacao e de testes

[0] Iniciar sistema operativo
Windows

[1] Iniciar servidores de apoio
JUDDI:
> startup.bat

[2] Criar pasta temporaria
> mkdir grupo7
> cd grupo7

[3] Obter versao entregue
Enviado pelo fenix

[4] Construir e executar **servidor**
cd grupo7
cd SD-ID_R_1
cd sd-id
> mvn clean
> mvn generate-sources
> mvn compile
> mvn test
> mvn exec:java


[5] Construir **cliente**
cd grupo7
cd SD-ID_R_1
cd sd-id-cli
> mvn clean
> mvn generate-sources
> mvn compile
> mvn test

-------------------------------------------------------------------------------
**FIM**