package org.eclipse.epsilon.emg.random;

import org.apache.commons.math3.random.RandomDataGenerator;

public class testdist {

	public static void main(String[] args) {
		RandomDataGenerator gen = new RandomDataGenerator();
		for (int i = 1; i<5;i++)
			System.out.println(gen.nextCauchy(0, i));
		
	}

}
