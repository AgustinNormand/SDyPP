Server:
Open ports 9090, 6666
docker run -p 9090:9090 -p 6666:6666 -d agustinnormand/tp1_ej4_server PUBLIC_ACCESIBLE_IP_OF_SERVER

Client:
docker run agustinnormand/tp1_ej4_client PUBLIC_ACCESIBLE_IP_OF_SERVER