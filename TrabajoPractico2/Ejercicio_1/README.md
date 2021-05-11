## Masters:
* Agregue tantos Masters como desee, en el docker-compose.

## Endpoints:
* Agregue tantos Endpoints como desee, en el docker-compose, cree un directorio para cada enpoint en "DirectoryWithSharedFiles", monte ese directorio a ese endpoint en el docker-compose, aregue en el directorio, los archivos que quiere que ese endpoint comparta.


## Funcionamiento Masters:
* Obtener los endpoints que estan actualmente disponibles: curl MASTER_IP:PORT/endpoints

## Funcionamiento Endpoints:
* Obtener archivos que está compartiendo un endpoint: curl ENDPOINT_IP:PORT/files
* Solicitarle a un endpoint que obtenga un archivo: curl ENDPOINT_IP:PORT/requestFile/NOMBRE_DEL_ARCHIVO

## Nota:
* Si se trata de un entorno local MASTER_IP y ENDPOINT_IP van a ser 127.0.0.1, y lo que diferenciará a cada servicio van a ser los numeros de puerto.



