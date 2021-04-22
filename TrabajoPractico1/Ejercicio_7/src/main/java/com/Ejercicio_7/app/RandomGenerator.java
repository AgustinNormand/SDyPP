package com.Ejercicio_7.app;

import java.util.Random;

public class RandomGenerator implements Tarea{
	
	private int n;
	
	public RandomGenerator(int n) {
		this.n = n;
	}
	
	public Float ejecutar() {
        Random r = new Random();

        return (float) r.nextInt(this.n)*r.nextInt(this.n);
    }
}
