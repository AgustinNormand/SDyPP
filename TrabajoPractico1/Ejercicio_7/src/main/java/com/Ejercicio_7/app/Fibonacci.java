package com.Ejercicio_7.app;

public class Fibonacci implements Tarea{
	
	private int n;
	
	public Fibonacci(int n) {
		this.n = n;
	}

	int calculateFibonacci(int n)
	{
	    if (n>1){
	       return calculateFibonacci(n-1) + calculateFibonacci(n-2);  //función recursiva
	    }
	    else if (n==1) {  // caso base
	        return 1;
	    }
	    else if (n==0){  // caso base
	        return 0;
	    }
	    else{ //error
	        System.out.println("Debes ingresar un tamaño mayor o igual a 1");
	        return -1; 
	    }
	}
	
	@Override
	public Float ejecutar() {
		int result = calculateFibonacci(this.n);
		return (float) result;
	}

}
