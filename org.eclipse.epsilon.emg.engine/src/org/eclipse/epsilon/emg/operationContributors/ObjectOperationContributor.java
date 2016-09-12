/*******************************************************************************
 * Copyright (c) 2012 The University of York.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 *     Saheed Popoola - aditional functionality
 *     Horacio Hoyos - aditional functionality
 ******************************************************************************/
package org.eclipse.epsilon.emg.operationContributors;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.epsilon.emg.EmgModule;
import org.eclipse.epsilon.emg.random.EmgRandomGenerator;
import org.eclipse.epsilon.emg.random.IEmgRandomGenerator;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.operations.contributors.OperationContributor;

/**
 * The Class ObjectOperationContributor delegates all the random generating
 * functions to a EmgRandomGenerator but overloads the nextXXXList operations
 * to allow the user to specify @list annotations values as listIDs.
 */
public class ObjectOperationContributor
		extends OperationContributor
		implements IEmgRandomGenerator<IEmgRandomGenerator.DefaultCharacterSet> {
	
	private final EmgRandomGenerator delegate;
	private final EmgModule module;
	/** The list samples. */
	private Map<String, List<Integer>> listSamples;
	
	
	public ObjectOperationContributor(EmgModule module) {
		delegate = new EmgRandomGenerator(module.getContext());
		this.module = module;
	}
	
	public ObjectOperationContributor(EmgModule module, int seed) {
		delegate = new EmgRandomGenerator(module.getContext(), seed);
		this.module = module;
	}
	
	@Override
	public boolean contributesTo(Object target) {
		return target instanceof Object;
	}
	
	
	/**
	 * Gets the elements from create rules with the @list annotation with the
	 * given name parameter 
	 * @param name
	 * @return
	 */
	public Collection<Object> getNamedList(String name) {
		Map<String, List<Object>> existing = module.getNamedCreatedObjects();
		if(existing.containsKey(name)){
			return existing.get(name);
		}
		return null;
	}
	/**
	 * @param numberOfTrials
	 * @param probabilityOfSuccess
	 * @see org.eclipse.epsilon.emg.random.EmgRandomGenerator#useBinomialDistribution(int, double)
	 */
	public void useBinomialDistribution(int numberOfTrials, double probabilityOfSuccess) {
		delegate.useBinomialDistribution(numberOfTrials, probabilityOfSuccess);
	}
	/**
	 * @param mean
	 * @see org.eclipse.epsilon.emg.random.EmgRandomGenerator#useExponentialDistribution(double)
	 */
	public void useExponentialDistribution(double mean) {
		delegate.useExponentialDistribution(mean);
	}
	/**
	 * @return
	 * @throws EolRuntimeException
	 * @see org.eclipse.epsilon.emg.random.EmgRandomGenerator#nextBoolean()
	 */
	public boolean nextBoolean() throws EolRuntimeException {
		return delegate.nextBoolean();
	}
	/**
	 * @param upper
	 * @return
	 * @throws EolRuntimeException
	 * @see org.eclipse.epsilon.emg.random.EmgRandomGenerator#nextInteger(int)
	 */
	public int nextInteger(int upper) throws EolRuntimeException {
		return delegate.nextInteger(upper);
	}
	/**
	 * @param upper
	 * @return
	 * @throws EolRuntimeException
	 * @see org.eclipse.epsilon.emg.random.EmgRandomGenerator#nextLong(long)
	 */
	public long nextLong(long upper) throws EolRuntimeException {
		return delegate.nextLong(upper);
	}
	/**
	 * @param upper
	 * @return
	 * @throws EolRuntimeException
	 * @see org.eclipse.epsilon.emg.random.EmgRandomGenerator#nextDobule(double)
	 */
	public double nextDobule(double upper) throws EolRuntimeException {
		return delegate.nextDobule(upper);
	}
	/**
	 * @param lower
	 * @param upper
	 * @return
	 * @throws EolRuntimeException
	 * @see org.eclipse.epsilon.emg.random.EmgRandomGenerator#nextInteger(int, int)
	 */
	public int nextInteger(int lower, int upper) throws EolRuntimeException {
		return delegate.nextInteger(lower, upper);
	}
	/**
	 * @param lower
	 * @param upper
	 * @return
	 * @throws EolRuntimeException
	 * @see org.eclipse.epsilon.emg.random.EmgRandomGenerator#nextLong(long, long)
	 */
	public long nextLong(long lower, long upper) throws EolRuntimeException {
		return delegate.nextLong(lower, upper);
	}
	/**
	 * @param lower
	 * @param upper
	 * @return
	 * @see org.eclipse.epsilon.emg.random.EmgRandomGenerator#nextDobule(double, double)
	 */
	public double nextDobule(double lower, double upper) {
		return delegate.nextDobule(lower, upper);
	}
	/**
	 * @return
	 * @see org.eclipse.epsilon.emg.random.EmgRandomGenerator#nextValue()
	 */
	public double nextValue() {
		return delegate.nextValue();
	}
	/**
	 * @param numberOfTrials
	 * @param probabilityOfSuccess
	 * @return
	 * @see org.eclipse.epsilon.emg.random.EmgRandomGenerator#nextBinomialValue(int, double)
	 */
	public double nextBinomialValue(int numberOfTrials, double probabilityOfSuccess) {
		return delegate.nextBinomialValue(numberOfTrials, probabilityOfSuccess);
	}
	/**
	 * @param mean
	 * @return
	 * @see org.eclipse.epsilon.emg.random.EmgRandomGenerator#nextExponentialValue(double)
	 */
	public double nextExponentialValue(double mean) {
		return delegate.nextExponentialValue(mean);
	}
	/**
	 * @param charSet
	 * @param length
	 * @return
	 * @see org.eclipse.epsilon.emg.random.EmgRandomGenerator#nextString(java.lang.String, int)
	 */
	public String nextString(String charSet, int length) {
		return delegate.nextString(charSet, length);
	}
	/**
	 * @param charSet
	 * @param length
	 * @return
	 * @see org.eclipse.epsilon.emg.random.EmgRandomGenerator#nextCapitalisedString(java.lang.String, int)
	 */
	public String nextCapitalisedString(String charSet, int length) {
		return delegate.nextCapitalisedString(charSet, length);
	}

	/**
	 * @param length
	 * @param minWordLength
	 * @return
	 * @throws EolRuntimeException
	 * @see org.eclipse.epsilon.emg.random.EmgRandomGenerator#nextCamelCaseString(int, int)
	 */
	public String nextCamelCaseString(int length, int minWordLength) throws EolRuntimeException {
		return delegate.nextCamelCaseString(length, minWordLength);
	}
	/**
	 * @param c
	 * @return
	 * @see org.eclipse.epsilon.emg.random.EmgRandomGenerator#nextFromCollection(java.util.Collection)
	 */
	public Object nextFromCollection(Collection<?> c) {
		return delegate.nextFromCollection(c);
	}
	/**
	 * The listID is the value of a @list annotation of a create operation. If
	 * no create operation with that @list annotation value is found, the 
	 * operation is delegated to the underlying {@link EmgRandomGenerator}.
	 * @param listID
	 * @return
	 * @throws EolRuntimeException
	 * @see org.eclipse.epsilon.emg.random.EmgRandomGenerator#nextFromList(java.lang.String)
	 */
	public Object nextFromList(String listID) throws EolRuntimeException {
		Collection<Object> existing = getNamedList(listID);
		if (existing == null) {
			return delegate.nextFromList(listID);
		}
		else {
			return delegate.nextFromCollection(existing);
		}
	}
	/**
	 * The listID is the value of a @list annotation of a create operation. If
	 * no create operation with that @list annotation value is found, the 
	 * operation is delegated to the underlying {@link EmgRandomGenerator}.
	 * @param listID
	 * @return
	 * @throws EolRuntimeException
	 * @see org.eclipse.epsilon.emg.random.EmgRandomGenerator#nextSampleFromList(java.lang.String)
	 */
	public Object nextFromListAsSample(String listID) throws EolRuntimeException {
		List<Object> existing = (List<Object>) getNamedList(listID);
		if (existing == null) {
			return delegate.nextFromListAsSample(listID);
		}
		else {
			Map<String, List<Integer>> sampleList = getListSamples();
			int size = existing.size();
			List<Integer> index = delegate.getIndex(listID, size, sampleList);
			Object result = null;
			try {
				result = existing.get(index.remove(0));
			} catch (IndexOutOfBoundsException e) {
				// More matches than existing, what to do here? Does returning
				// null makes EPL disregard the match?
				//EolRuntimeException.propagate(e);
			}
	        return result;
		}
	}
	/**
	 * @param c
	 * @param k
	 * @return
	 * @throws EolRuntimeException
	 * @see org.eclipse.epsilon.emg.random.EmgRandomGenerator#nextSample(java.util.Collection, int)
	 */
	public List<Object> nextSample(Collection<?> c, int k) throws EolRuntimeException {
		return delegate.nextSample(c, k);
	}
	/**
	 * The listID is the value of a @list annotation of a create operation. If
	 * no create operation with that @list annotation value is found, the 
	 * operation is delegated to the underlying {@link EmgRandomGenerator}.
	 * @param listID
	 * @param k
	 * @return
	 * @throws EolRuntimeException
	 * @see org.eclipse.epsilon.emg.random.EmgRandomGenerator#nextSample(java.lang.String, int)
	 */
	public List<Object> nextSample(String listID, int k) throws EolRuntimeException {
		List<Object> existing = (List<Object>) getNamedList(listID);
		if (existing == null) {
			return delegate.nextSample(listID, k);
		}
		else {
			return nextSample(existing, k);
		}
	}
	
	/**
	 * @param addPort
	 * @param addPath
	 * @param addQuery
	 * @param addFragment
	 * @return
	 * @throws EolRuntimeException
	 * @see org.eclipse.epsilon.emg.random.EmgRandomGenerator#nextURI(boolean, boolean, boolean, boolean)
	 */
	public String nextURI(boolean addPort, boolean addPath, boolean addQuery, boolean addFragment)
			throws EolRuntimeException {
		return delegate.nextURI(addPort, addPath, addQuery, addFragment);
	}
	/**
	 * @return
	 * @throws EolRuntimeException
	 * @see org.eclipse.epsilon.emg.random.EmgRandomGenerator#nextURI()
	 */
	public String nextURI() throws EolRuntimeException {
		return delegate.nextURI();
	}
	/**
	 * @param addPort
	 * @param addPath
	 * @param addQuery
	 * @param addFragment
	 * @return
	 * @throws EolRuntimeException
	 * @see org.eclipse.epsilon.emg.random.EmgRandomGenerator#nextHttpURI(boolean, boolean, boolean, boolean)
	 */
	public String nextHttpURI(boolean addPort, boolean addPath, boolean addQuery, boolean addFragment)
			throws EolRuntimeException {
		return delegate.nextHttpURI(addPort, addPath, addQuery, addFragment);
	}
	
	/**
	 * Gets the list samples.
	 *
	 * @return the listSamples
	 */
	private Map<String, List<Integer>> getListSamples() {
		if (listSamples == null) {
			listSamples = new HashMap<String, List<Integer>>();
		}
		return listSamples;
	}
	
}
		

