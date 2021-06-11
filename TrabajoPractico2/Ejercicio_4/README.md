## DONE:
* Version beta de procesamiento de imagen distribuido.
* Servicio RabbitMQ levantado.
* Servicio Redis levantado.
*  Servicio "Server":
    * Recepciona una imagen para aplicarle sobel, la convierte a byte[], y la almacena en Google Cloud Storage. 
    * Almacena en una cola de RabbitMQ (sliceQueue), un trabajo pendiente para recortar la imagen.
    * Proporciona una url para obtener la imagen resultado, luego de que sea procesada.
* Servicio "Slicer": 
    * Consume mensajes de una cola (sliceQueue) para recortar imagenes. 
    * Obtiene la imagen de GCS, las corta en 16 partes iguales, las convierte a byte[], las almacena en GCS.
    * Encola, por cada parte de la imagen, un trabajo pendiente de aplicar sobel (sovelQueue).
    * Crea un registro en Redis indicando la cantidad de partes de la imagen, e inicializando las "partesFinalizadas" en 0.
* Nodo "WorkerSobel": 
    * Consume mensajes de una cola (sovelQueue), obtiene la parte de la imagen de GCS, le aplica el sovel, y la almacena en GCS.
    * Incrementa en 1 las "partesFinalizadas" en Redis.
* Nodo "WorkerManager": 
    * Monitorea si ya se procesaron todas las partes de alguna imagen. Es decir, controla si algun registro de Redis cumple con la condición ("cantidadPartesTotales" == "cantidadPartesFinalizadas")
    * En caso de que alguna imagen se haya procesado por completo, encola un trabajo en una cola (assemblyQueue), para ensamblar la imagen.
* Nodo "WorkerAssembler": 
    * Consume mensajes de una cola (assemblyQueue), por cada mensaje, obtiene todas las partes de la imagen a ensamblar, de GCS.
    * Elimina todas las partes y la imagen orinal de GCS.
    * Almacena la imagen ensamblada en GCS. (Cuando esto sucede, si el cliente, va a la url que le proporcionó el Server, se muestra la imagen procesada.)

## Example:
* Video demostrativo: https://youtu.be/m4Rc9e0G2pk 

## TO-DO:
* No hacer auto-ack en los mensajes de RabbitMQ.
* Refactorizar el codigo, hay cosas desprolijas.
* Mejorar como mostrar la imagen resultado, o en su defecto, mejorar la url que se genera para consultar la imagen resultado.
* El numero de partes en las que el slicer corta la imagen debería poder parametrizarse.
* Actualmente muestra las imagenes resultado en formate png, dado que con otra extension, no funciona.
* El sobel tiene algunos errores, la imagen resultado no está muy limpia.
* La imagen resultado tiene unas lineas que se notan las partes en la que fue cortada.
* Implementarlo en kubernetes (LoadBalancer, HPA, Cluster Auto Scaler, etc, etc.).



## Deployment:
* Proveer un archivo JSON de credencial de GCP en el path "/usr/src/googleCredentials/credential.json
* docker-compose up -d

## Comandos Utiles:
* Para levantar un cliente redis: docker run -it --network docker_default --rm redis redis-cli -h redis-service
* Comandos redis: (auth password, keys *, hgetall key)




