package com.TrabajoPractico2.Ejercicio4.Servidor;

public class Task {

    private String storageImageName;

    private String partsNamesJsonArray;

    public Task(String storageImageName, String partsNamesJsonArray){
        this.storageImageName = storageImageName;
        this.partsNamesJsonArray = partsNamesJsonArray;
    }

    public String getStorageImageName() {
        return storageImageName;
    }

    public void setStorageImageName(String storageImageName) {
        this.storageImageName = storageImageName;
    }

    public String getPartsNamesJsonArray() {
        return partsNamesJsonArray;
    }

    public void setPartsNamesJsonArray(String partsNamesJsonArray) {
        this.partsNamesJsonArray = partsNamesJsonArray;
    }
}
