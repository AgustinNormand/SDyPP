docker-compose up -d rabbitmq-service 
docker-compose up -d

Comandos utiles:
docker run -it --network docker_default --rm redis redis-cli -h redis-service
auth password
keys *
hgetall key
