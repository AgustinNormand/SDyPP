Moverse al directorio donde se encuentra el Dockerfile: cd Server

Construir imagen de docker con el comando: docker build -t agustinnormand/tp1_ejudp_server .

Correr servidor con el comando: docker run -p 9090:9090/udp agustinnormand/tp1_ejudp_server

Realizar una petición como cliente con el comando: nc -u localhost 9090