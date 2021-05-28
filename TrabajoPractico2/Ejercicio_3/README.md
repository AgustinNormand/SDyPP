## DONE:
* Creación de cluster en GKE con Terraform.
* Deployment de Workers con tiempo de procesamiento simulado y con Request y Limite de CPU y Memoria.
* Deployment de Receptionists + loadbalancer.
* Deployment de RabbitMQ.
* Prometheus desplegado + Prometheus Adapter + Grafana.
* HPA Custom con profundidad de cola de RabbitMQ. Video demostrativo: https://youtu.be/3eOQBZ9OjnA
* LivenessProbe y ReadinessProbe. Video demostrativo: https://youtu.be/KrVg8z8RMQg
* ClusterAutoscaler. Video demostrativo: https://youtu.be/I1BwCSoAMe8

## Deployment:
* kubectl apply -f Kubernetes/Prometheus/setup
* kubectl apply -f Kubernetes/Prometheus/
* kubectl apply -f Kubernetes/HPA-Custom/
* kubectl apply -f Kubernetes/

## TO-DO:
* Agregar node autoprovisioning a Terraform
* CICD Pipeline. GithubActions + ArgoCD

## TO-FIX:
* Los workers tiran error hasta que no se haga una petición a algun Receptionist de encolar un mensaje. Porque recién ahi es cuando se crea la cola a la que los workers están intentando acceder.
* HPA cuando hace down-scale, pasa de 10 replicas a 1, debería hacerlo gradualmente.
