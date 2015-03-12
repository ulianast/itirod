package com.krasnova.lab3;

import static org.junit.Assert.*;

import com.krasnova.lab3.FrequencyDictionary;

import org.junit.Test;

public class FrequencyDictionaryTest {

	@Test
	public void test() {
		FrequencyDictionary dict = new FrequencyDictionary("qwertyyq", new FileRepository());
		dict.buildDictionary();
		
		assertEquals(dict.get('q'),Integer.valueOf(2));
		assertEquals(dict.get('w'),Integer.valueOf(1));
		assertEquals(dict.get('e'),Integer.valueOf(1));
		assertEquals(dict.get('r'),Integer.valueOf(1));
		assertEquals(dict.get('t'),Integer.valueOf(1));
		assertEquals(dict.get('y'),Integer.valueOf(2));
	}

}
