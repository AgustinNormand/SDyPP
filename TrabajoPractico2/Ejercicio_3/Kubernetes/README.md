## DONE:
* Deployment de Workers con tiempo de procesamiento simulado.
* Deployment de Receptionists + loadbalancer.
* Deployment de RabbitMQ.
* Prometheus desplegado + Prometheus Adapter + Grafana.
* HPA Custom con profundidad de cola de RabbitMQ.

## Deployment:
* TO-DO.

## TO-DO:
* Deployment con Request y Limite de CPU y Memoria.
* Livenesprove y Readynesprove (Ya que spring tarda un toque en levantar, no esta bueno que mate al otro contenedor apenas pone a correr el nuevo, porque ahi la app tiene un  tiempo de downtime)

## Ejemplo de funcionamiento: 
* https://youtu.be/3eOQBZ9OjnA

## Comandos utiles:
* kubectl -n monitoring port-forward grafana-5c55845445-d9jps 3000:3000
* kubectl port-forward locust-6bb76bb7cb-p4vj4 9090:8089
* kubectl port-forward rabbit-5d6459f789-4l7k7 8080:15672
