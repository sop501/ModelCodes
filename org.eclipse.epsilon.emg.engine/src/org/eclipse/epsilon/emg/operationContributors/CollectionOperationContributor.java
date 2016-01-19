package org.eclipse.epsilon.emg.operationContributors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.eclipse.epsilon.emg.RandomGenerator;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.execute.operations.contributors.IterableOperationContributor;
import org.eclipse.epsilon.eol.execute.operations.contributors.OperationContributor;

public class CollectionOperationContributor extends OperationContributor{

	Collection targetCollection;
	IterableOperationContributor contributor= new IterableOperationContributor();
	RandomGenerator random;
	public CollectionOperationContributor(){
		if(target instanceof Collection<?>){
			targetCollection = (Collection) target;// change the target to a collection
		}
		random= new RandomGenerator();
	}
	public CollectionOperationContributor(RandomGenerator rand){ 
		if(target instanceof Collection<?>){
			targetCollection = (Collection) target;// change the target to a collection
		}
		random= rand;
	}
	@Override
	public boolean contributesTo(Object target) {
		return target instanceof Collection<?>;
		
	}
	public Object randomD() throws EolRuntimeException {
		
		if(target instanceof Collection<?>){
			targetCollection = (Collection) target;// change the target to a collection
		}
		else{
			System.err.println("only collections are allowed!");
			return null;
		}
		int cSize=targetCollection.size();
		contributor.setTarget(targetCollection);
		return contributor.at(random.nextInt(cSize));
			
	}
	public Object get(int num){
		return contributor.at(num);
		
	}
	public Collection<?> randomD(int size) throws EolRuntimeException {
		Collection targetCollection;
		if(target instanceof Collection<?>){
			targetCollection = (Collection) target;// change the target to a collection
		}
		else{
			System.err.println("only collections are allowed!");
			return null;
		}
		if(size<0){
			System.err.println("size must not be less than 0");
			return null;
		}
		int  seed,limitValue;//values of the variables

		//check if there is a limit
		if(context.getFrameStack().contains("limit")){
			limitValue=(int) context.getFrameStack().get("limit").getValue();
		}
		else{
			limitValue=targetCollection.size();
		}	
		return deterministicRandomBase(size,limitValue);
			
		}
		public Collection<?> randomD(int minSize,int maxSize) throws EolRuntimeException {
			
			if(target instanceof Collection<?>){
				targetCollection = (Collection) target;// change the target to a collection
			}
			else{
				System.err.println("only collections are allowed!");
				return null;
			}
			
			int seed,limit;
			//check if there is a seed
			if(context.getFrameStack().contains("seed")){
				seed = (int) context.getFrameStack().get("seed").getValue();
			}
			else{
				//we create a new seed and add it to the context
				seed = (int) System.currentTimeMillis();
				context.getFrameStack().put(Variable.createReadOnlyVariable("seed", seed));
			}
			if(maxSize-minSize<0 && maxSize!= -1){
				System.err.println("maximum size must not be less than minimum size");
				return null;
			}
			if(minSize<0){
				System.err.println("minimum size must not be less than 0");
				return null;
			}
			
			//check if there is a seed
			if(context.getFrameStack().contains("seed")){
				//seed = context.getFrameStack().get("seed").getValue();
				seed=(int) context.getFrameStack().get("seed").getValue();
			}
			else{
				//we create a new seed and add it to the context
				seed=(int) System.currentTimeMillis();
				context.getFrameStack().put(Variable.createReadOnlyVariable("seed", seed));
				//seed= new Variable("seed",seedValue,null);
				//context.getFrameStack().put(seed);
			}
			
			//check if there is a limit
			if(context.getFrameStack().contains("limit")){
				//limit = context.getFrameStack().get("limit");
				limit=(int) context.getFrameStack().get("limit").getValue();
			}
			else{
				limit=targetCollection.size();
				
			}	
			int size;
			//Random random= new Random(seed);
			if(maxSize==-1) size=random.nextInt()+minSize;
			else size=random.nextInt((maxSize-minSize)+1)+minSize;
							
			
			return deterministicRandomBase(size,limit);		
			
		}
		protected Collection<?> deterministicRandomBase(int size,int limit) throws EolRuntimeException {
			//target <- Student.all
			/*
			 * *this method generates a deterministic collection as output
			 * the limit is used in case new things has been added to the collection
			 * so we stop at the previous size of the collection
			 */
			if(target instanceof Collection<?>){
				targetCollection = (Collection) target;// change the target to a collection
			}
			else{
				System.err.println("only collections are allowed!");
				return null;
			}
			contributor.setTarget(targetCollection);
			Collection<Object> out = contributor.createCollection(); // the output collection
			
			//Random random=new Random(seed);
			//the if statement ensures the limit is less than the size of the input
			//its a criteria to see if no deletion has occur
			if(limit<=targetCollection.size()){
				for(int i=0;i<size;i++)
					out.add(contributor.at(random.nextInt(limit)));
				//System.out.println(out.toString());
			}
			else{
				//E.g when items have been deleted
				Random rand = new Random();
				int temp;
				int cSize=targetCollection.size();
				for(int i=0;i<size;i++){
					temp =random.nextInt(limit);
					if(temp<=cSize)
						out.add(contributor.at(temp));
					else
						out.add(contributor.at(rand.nextInt(cSize)));
				}
					
				
			}
			return out;
			
			
		}
		public Object uniqueRandom(int size) throws EolRuntimeException {
			if(target instanceof Collection<?> ){
				targetCollection = (Collection) target;// change the target to a collection
				if( size> targetCollection.size()){
					System.err.println("size is greater than the number of objects!");
					return null;
				}
			}
			else{
				System.err.println("only collections are allowed!");
				return null;
			}
			contributor.setTarget(targetCollection);
			
			Collection<Object> out = contributor.createCollection(); // the output collection
			List<Integer> num= new ArrayList<Integer>();
			int tSize =targetCollection.size();
			if(tSize < (size*2)){
				for(int i=0;i<tSize;num.add(i++));
				Collections.shuffle(num, random);
				for(int i=0;i<size;i++)
					out.add(contributor.at(num.get(i)));
			}
			else{
				Set<Integer> numb= new HashSet();
				while(numb.size()<=size){
					numb.add(random.nextInt(tSize));
				}
				Object[] number;
				number=numb.toArray();
				for(int i=0;i<size;i++)
					out.add(contributor.at((int) number[i]));
			}
			return out;
			
		}
		public Object uniqueInteger(int size,int limit) throws EolRuntimeException {
			return null;
		}
		public Object uniformRandom() throws EolRuntimeException {
			if(target instanceof Collection<?>){
				targetCollection = (Collection) target;// change the target to a collection
			}
			else{
				System.err.println("only collections are allowed!");
				return null;
			}
			int cSize=targetCollection.size();
			if(cSize<1){
				return null;
			}
			else if(cSize<5){
				return randomD();
			}
			else{
				int mean= cSize/2;
				int variance= cSize/5;
				return uniformRandom(mean,variance);
			}
			
		}
		public Object uniformRandom(int mean,int variance) throws EolRuntimeException {
			//target <- Student.all
			//Collection targetCollection;
			if(target instanceof Collection<?>){
				targetCollection = (Collection) target;// change the target to a collection
			}
			else{
				System.err.println("only collections are allowed!");
				return null;
			}
			int cSize=targetCollection.size();
			int index=-1;
			double ind;
			while(index<0||index>=cSize){
				ind=random.nextGaussian()*variance+mean;
				index= (int)ind;
			}
			contributor.setTarget(targetCollection);
			//System.out.println(mean+"  "+variance);
			//System.out.println("ght  "+(random.nextGaussian()*variance+mean));
			return contributor.at(index);
			
		}
		

}
