package com.krasnova.lab4;

public class ConsoleOutStream implements IOutStream {

	public ConsoleOutStream() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void out(String str) {
		System.out.print(str);
	}

	@Override
	public void outln(String str) {
		System.out.println(str);
	}

}
