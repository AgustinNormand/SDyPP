## Masters:
* Agregue tantos Masters como desee, en el docker-compose.

## Endpoints:
* Agregue tantos Endpoints como desee, en el docker-compose, cree un directorio para cada enpoint en "DirectoryWithSharedFiles", monte ese directorio a ese endpoint en el docker-compose, aregue en el directorio, los archivos que quiere que ese endpoint comparta.


## Funcionamiento Masters:
* Obtener los endpoints que estan actualmente disponibles: curl MASTER_IP/endpoints

## Funcionamiento Endpoints:
* Obtener archivos que está compartiendo un endpoint: curl ENDPOINT_IP/files
* Solicitarle a un endpoint que obtenga un archivo: curl ENDPOINT_IP/requestFile/NOMBRE_DEL_ARCHIVO



