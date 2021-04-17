Moverse al directorio donde se encuentra el Dockerfile: cd Server

Construir imagen de docker con el comando: docker build -t agustinnormand/tp1_ej3_server .

Correr servidor con el comando: docker run -p 9090:9090/tcp agustinnormand/tp1_ej3_server

Realizar una petición como cliente con el comando: nc localhost 9090