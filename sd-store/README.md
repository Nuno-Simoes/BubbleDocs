# Bubble Docs SD-STORE
# Projecto de Sistemas DistribuÃ­dos #

## Primeira entrega ##

Grupo de SD: 23

Rita Pereira 70473 ritap420@gmail.com

Nuno Simões  70568	nunosimoes_86@hotmail.com

Marco Pereira 70644 marcopereira_32@hotmail.com


RepositÃ³rio:
[tecnico-softeng-distsys-2015/T_04_07_23-project](https://github.com/tecnico-softeng-distsys-2015/T_04_07_23-project/)


-------------------------------------------------------------------------------

## Serviço SD-STORE 

### InstruÃ§Ãµes de instalaÃ§Ã£o 

[0] Iniciar sistema operativo
 Windows

[1] Iniciar servidores de apoio

JUDDI:
> startup.bat

[2] Criar pasta temporÃ¡ria

> cd Desktop
> mkdir projectoG23

[3] Obter versÃ£o entregue

> git clone -b SD-STORE_R_1 https://github.com/tecnico-softeng-distsys-2015/T_04_07_23-project.git projectoG23

[4] Construir e executar **servidor**

> cd projectoG23/sd-store
> mvn clean package 
> mvn exec:java


[5] Construir **cliente**

> cd projectoG23/sd-store-cli
> mvn clean package

...


-------------------------------------------------------------------------------

### InstruÃ§Ãµes de teste: ###


[1] Executar **cliente de testes** ...

> cd projectoG23
> mvn test


[2] Executar ...



...


-------------------------------------------------------------------------------
**FIM**