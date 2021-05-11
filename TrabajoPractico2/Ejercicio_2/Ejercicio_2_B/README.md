## Servidor:
* docker run -p 8080:8080 -d agustinnormand/tp2_ej2b_server

## Cliente (Postman):
* Realizar un deposito de 100$ a la cuenta 1: SERVER_IP:8080/deposit/1/100
* Extraer 100$ de la cuenta 1: SERVER_IP:8080/withdrawal/1/100
* Rapidamente, realizar un depósito de 10000$ a la cuenta 1: SERVER_IP:8080/deposit/1/10000
* Comprobar el saldo de la cuenta 1, el cual debería ser 10000: SERVER_IP:8080/balance/1

* Video explicativo: https://youtu.be/Az0lV3WlQeQ

* Con respecto al punto A, se modificaron las funciones del código encargadas de la lectura y
  escritura del archivo de saldo en cuenta. A estas se le agregó un bloque syncronized, para que
  el acceso al recurso sea sincronizado. El video muestra como se obtiene el valor correcto luego de 
  dos peticiones en simultaneo, que sin este bloque, resultaban en un valor erroneo del saldo.

