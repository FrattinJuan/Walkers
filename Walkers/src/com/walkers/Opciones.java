package com.walkers;

public abstract class Opciones {
	private static int maxMalos = 1;
	public static void setMalos (int numero){
		Opciones.maxMalos  = numero;
	}
	public static int getMalos(){
		return maxMalos;
	}
	

}
