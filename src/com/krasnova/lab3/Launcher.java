package com.krasnova.lab3;

import java.io.IOException;

public class Launcher {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		FrequencyDictionary dict = new FrequencyDictionary(new FileRepository());
		dict.read("E:\\JavaWorkspace\\itirod\\bin\\com\\krasnova\\lab3\\qwerty.txt");
		dict.buildDictionary();
		System.out.print(dict.toString());
		System.in.read();
	}
}
