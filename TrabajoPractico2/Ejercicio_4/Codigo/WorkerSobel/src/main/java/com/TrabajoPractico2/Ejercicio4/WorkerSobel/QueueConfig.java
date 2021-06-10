package com.TrabajoPractico2.Ejercicio4.WorkerSobel;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class QueueConfig {

    private AmqpAdmin amqpAdmin;

    public QueueConfig(AmqpAdmin amqpAdmin) {
        this.amqpAdmin = amqpAdmin;
    }

    @PostConstruct
    public void createQueues() {
        amqpAdmin.declareQueue(new Queue("sliceJobs", true));
        amqpAdmin.declareQueue(new Queue("sobelJobs", true));
        amqpAdmin.declareQueue(new Queue("assemblyJobs", true));
    }
}
