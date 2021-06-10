package com.TrabajoPractico2.Ejercicio4.Servidor;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;

public class RedisHandler {

    private RedisCommands<String, String> syncCommands;
    private StatefulRedisConnection<String, String> connection;
    private RedisClient redisClient;

    @Autowired
    public RedisHandler(Environment env) {
        RedisURI redisUri = RedisURI.Builder.redis(env.getProperty("redis.hostname"))
                .withPassword(env.getProperty("redis.password"))
                .withPort(Integer.parseInt(env.getProperty("redis.port")))
                .withDatabase(Integer.parseInt(env.getProperty("redis.database")))
                .build();
        this.redisClient = RedisClient.create(redisUri);
        this.connection = redisClient.connect();
        this.syncCommands = connection.sync();
    }

    public void close(){
        connection.close();
        redisClient.shutdown();
    }

    public void write(String imageName, int totalParts){
        syncCommands.hset(imageName, "totalParts", Integer.toString(totalParts));
        syncCommands.hset(imageName, "endedParts", "0");
    }

    public ArrayList<Task> getEndedTasks() {
        ArrayList<Task> endedTasks = new ArrayList<>();
        List<String> claves = syncCommands.keys("*");
        for(String clave:claves){
            int totalParts = 1;
            int endedParts = -1;
            try{
                totalParts = Integer.parseInt(syncCommands.hget(clave, "totalParts"));
                endedParts = Integer.parseInt(syncCommands.hget(clave, "endedParts"));
                if (totalParts == endedParts){
                    String partsNamesJsonArray = syncCommands.hget(clave, "partsNames");
                    endedTasks.add(new Task(clave, partsNamesJsonArray));
                    syncCommands.del(clave);
                }
            } catch(Exception e){
                e.printStackTrace();
                System.out.println("Clave:"+clave+"TotalParts:" + totalParts + "EndedParts:" + endedParts);
            }
        }
        return endedTasks;
    }
}
