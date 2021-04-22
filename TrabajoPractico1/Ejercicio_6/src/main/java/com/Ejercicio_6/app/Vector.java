package com.Ejercicio_6.app;

import java.util.Arrays;
import java.util.Iterator;

public class Vector {
	
	private int[] v;
	
	public Vector(int[] v) {
		this.v = v;
	}
	
	public Vector sum(Vector v2){
		int [] arrayV2 = v2.getArray();
		int[] vectorResult = new int[10];
		for (int i = 0; i < 10; i++) {
			vectorResult[i] = v[i] + arrayV2[i];
		}
		return new Vector(vectorResult);
	}
	
	public int[] getArray() {
		return this.v;
	}

	public Vector subtract(Vector v2) {
		int [] arrayV2 = v2.getArray();
		int[] vectorResult = new int[10];
		for (int i = 0; i < 10; i++) {
			vectorResult[i] = v[i] - arrayV2[i];
		}
		return new Vector(vectorResult);
	}

	@Override
	public String toString() {
		return "Vector [v=" + Arrays.toString(v) + "]";
	}
}
