package org.eclipse.epsilon.emg.random;

import org.apache.commons.math3.random.RandomDataGenerator;

/**
 * The simplest implementation of the RandomAttributeGenerator interface.
 * All attributes are generated as strings. It is the responsibility of the 
 * calling class/method to do the appropriate conversion.
 * @author Goblin
 *
 */
public class SimpleAttributeGenerator
		implements RandomAttributeGenerator<String,RandomAttributeGenerator.Default_CharacterSet> {
			
			
	/**
	 * Be default we use a normal distribution
	 */
	private Distribution globalDistribution = Distribution.Uniform;
	private RandomDataGenerator generator = new RandomDataGenerator();
	private double firstArg;
	private double secondArg;
	
	
	public void useBinomialDistribution(int numberOfTrials, double probabilityOfSuccess) {
		this.globalDistribution = Distribution.Binomial;
		this.firstArg = numberOfTrials;
		this.secondArg = probabilityOfSuccess;
	}
	
	public void useExponentialDistribution(double mean) {
		this.globalDistribution = Distribution.Exponential;
		this.firstArg = mean;
	}

	@Override
	public String generateIngeter(int lower, int upper) {
		return Integer.toString(generator.nextInt(lower, upper));
	}

	@Override
	public String generateLong(long lower, long upper) {
		return Long.toString(generator.nextLong(lower, upper));
	}
	
	@Override
	public String generateDobule(double lower, double upper) {
		
		double diff = upper-lower;
		if (diff == 0) {
			return Double.toString(lower);
		}
		if(upper<lower) {
			return Double.toString(generator.getRandomGenerator().nextDouble()*diff + upper);
		}
		return Double.toString(generator.getRandomGenerator().nextDouble()*diff + lower);
	}


	@Override
	public String generateValue() {
		switch(globalDistribution) {
			case Binomial:
				return generateBinomialValue((int) firstArg, secondArg);
			case Exponential:
				return generateExponentialValue(firstArg);
			case Uniform:
				return Double.toString(generator.nextUniform(firstArg, secondArg));
			default:
				return "0";
		}
	}

	@Override
	public String generateBinomialValue(int numberOfTrials, double probabilityOfSuccess) {
		return Double.toString(generator.nextBinomial((int) firstArg, secondArg));
	}
	
	@Override
	public String generateExponentialValue(double mean) {
		return Double.toString(generator.nextExponential(firstArg));
	}
	
	
	@Override
	public String generateRandomString(RandomAttributeGenerator.Default_CharacterSet charSet,
			int lenght) {
		
		String cs = charSet.getCharacters();
		int upper = cs.length();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < upper; i++) {
			sb.append(cs.charAt(generator.nextInt(0, upper-1)));
		}
		return sb.toString();
	}




	
}
