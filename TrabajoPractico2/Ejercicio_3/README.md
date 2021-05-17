## DONE:
* Deployment de Workers con tiempo de procesamiento simulado.
* Deployment de Receptionists + loadbalancer.
* Deployment de RabbitMQ.
* Prometheus desplegado + Prometheus Adapter + Grafana.
* HPA Custom con profundidad de cola de RabbitMQ.
* LivenessProbe y ReadinessProbe. Video demostrativo: https://youtu.be/KrVg8z8RMQg

## Deployment:
* kubectl apply -f Kubernetes/Prometheus/setup
* kubectl apply -f Kubernetes/Prometheus/
* kubectl apply -f Kubernetes/HPA-Custom/
* kubectl apply -f Kubernetes/

## TO-DO:
* Deployment con Request y Limite de CPU y Memoria.

## TO-FIX:
* Los workers tiran error hasta que no se haga una petición a algun Receptionist de encolar un mensaje. Porque recién ahi es cuando se crea la cola a la que los workers están intentando acceder.
* HPA cuando hace down-scale, pasa de 10 replicas a 1, debería hacerlo gradualmente.

## Ejemplo de funcionamiento: 
* https://youtu.be/3eOQBZ9OjnA

## Comandos utiles:
* kubectl -n monitoring port-forward grafana-5c55845445-d9jps 3000:3000
* kubectl port-forward locust-6bb76bb7cb-p4vj4 9090:8089
* kubectl port-forward rabbit-5d6459f789-4l7k7 8080:15672
