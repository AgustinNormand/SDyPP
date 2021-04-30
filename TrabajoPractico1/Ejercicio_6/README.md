## Servidor:
* Open ports 9090, 6666
* docker run -p 9090:9090 -p 6666:6666 -d agustinnormand/tp1_ej6_server PUBLIC_ACCESIBLE_IP_OF_SERVER

## Cliente:
* docker run agustinnormand/tp1_ej6_client PUBLIC_ACCESIBLE_IP_OF_SERVER

## Respuestas:

Introduzca un error en su código que modifique los vectores recibidos por parámetro. 
Qué impacto se genera?
Modifica el vector del lado del servidor, pero del lado del cliente no se produce ningún cambio.

Qué conclusión saca sobre la forma de pasaje de parámetros en RMI? 
El pasaje de parametro no es por referencia, sino que es por valor. El servidor trabaja con una copia del objeto que le envió el cliente.

Cliente:
V1: [1, 2, 3, 4, 5]
V2: [1, 2, 3, 4, 5]
V3: []

Servidor
V1: [5, 4, 3, 2, 1]
V2: [1, 2, 3, 4, 5]
V3: []

Cliente luego de la suma:
V1: [1, 2, 3, 4, 5]
V2: [1, 2, 3, 4, 5]
V3: [6, 6, 6, 6, 6]
