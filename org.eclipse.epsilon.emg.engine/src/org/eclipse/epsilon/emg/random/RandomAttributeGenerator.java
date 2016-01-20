package org.eclipse.epsilon.emg.random;

/**
 * The Random Attribute Generator interface defines the different 
 * @author Goblin
 *
 */
public interface RandomAttributeGenerator<T, K extends CharacterSet> {
	
	public enum Default_CharacterSet implements CharacterSet {
		ID("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"), 
		NUMERIC("1234567890"),
		LETTER("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"),
		LETTER_UPPER("ABCDEFGHIJKLMNOPQRSTUVWXYZ"), 
		LETTER_LOWER("abcdefghijklmnopqrstuvwxyz"), 
		UPPER_NUM("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"), 
		LOWER_NUM("abcdefghijklmnopqrstuvwxyz1234567890"),
		ID_SYMBOL("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~!@#$%^&*()_+-=[]\\{}|;':\"<>?,./"), 
		HEX_LOWER("abcdef1234567890"),
		HEX_UPPER("ABCDEF1234567890"); 
		private String characters;

		private Default_CharacterSet(String characters) {
			this.characters = characters;
		}

		public String getCharacters() {
			return characters;
		}

	}
	
	public enum Distribution {
		Beta,
		Binomial,
		Cauchy,
		ChiSquare,
		Exponential,
		F,
		Gamma,
		Gaussian,
		HyperGeometric,
		Uniform,
		Pascal,
		Poisson,
		T,
		Weibull,
		Zipf
	}
	
	
	public T generateIngeter(int lower, int upper);
	public T generateLong(long lower, long upper);
	public T generateDobule(double lower, double upper);
	
	/**
	 * Generates a value using the configured distribution
	 * @param Distribution
	 * @return
	 */
	public T generateValue();
	
	public T generateBinomialValue(int numberOfTrials, double probabilityOfSuccess);
	public T generateExponentialValue(double mean);
	// TODO add more methods for other distributions
	
	/**
	 * Generates a random string of the given length using the specified 
	 * character set. Characters are picked from the set using a uniform
	 * distribution.
	 * @param charSet
	 * @param lenght
	 * @return
	 */
	public T generateRandomString(K charSet, int lenght);
	
	
}
