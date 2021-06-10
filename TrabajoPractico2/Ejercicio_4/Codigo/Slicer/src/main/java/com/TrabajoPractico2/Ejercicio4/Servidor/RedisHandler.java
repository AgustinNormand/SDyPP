package com.TrabajoPractico2.Ejercicio4.Servidor;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.HashMap;
import java.util.Map;

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
        Map<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("totalParts", Integer.toString(totalParts));
        hashMap.put("endedParts", "0");
        hashMap.put("partsNames", "[]");
        syncCommands.hmset(imageName, hashMap);

    }

    public void addPartName(String imageName, String partName){
        String partsNamesJson = syncCommands.hget(imageName, "partsNames");
        JSONArray obj = new JSONArray(partsNamesJson);
        obj.put(partName);
        syncCommands.hset(imageName, "partsNames", obj.toString());

    }
}
