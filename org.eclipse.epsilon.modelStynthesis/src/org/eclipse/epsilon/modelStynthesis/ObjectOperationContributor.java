package org.eclipse.epsilon.modelStynthesis;

import java.util.Collection;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.operations.contributors.OperationContributor;

public class ObjectOperationContributor extends OperationContributor {
	//IterableOperationContributor contributor= new IterableOperationContributor();
	RandomGenerator random;
	public ObjectOperationContributor(){
		random= new RandomGenerator();
	}
	public ObjectOperationContributor(RandomGenerator rand){
		random= rand;
	}
	@Override
	public boolean contributesTo(Object target) {
		return target instanceof Object;
		//return true;
	}
	
	public String randomString() throws EolRuntimeException{
			return random.generateString();
	}
	public int randomInteger(int num) throws EolRuntimeException{
		return random.nextInt(num);
	}
	public Object link(Object objects) throws EolRuntimeException
	{
		return link(objects,false);
	}
	public Object link(Object objects,boolean containment) throws EolRuntimeException
	{
		if(target instanceof Collection){
			
		}
		else{
			EReference ref= EcoreFactory.eINSTANCE.createEReference();
			ref.setName(random.generateString());
			ref.setContainment(containment);
			//System.out.println(objects.toString());
			ref.setEType((EClassifier)objects);
			//EClass clas;
			((EClass) target).getEStructuralFeatures().add(ref);
		}
		return target;		
	}
}


	
	
		/*public String generateString(int min,int max) throws EolRuntimeException {
			if(max<min) return "";
			String result="";
			int ind= random.nextInt(max-min)+min;
			for (int i=0; i<ind; i++) //12
			    result = result + letter.charAt(random.nextInt(26));
			return result;		
		}
		public String generateCapitalString(int min,int max) throws EolRuntimeException {
			if(max<min) return "";
			String result="";
			int ind= random.nextInt(max-min)+min;
			for (int i=0; i<ind; i++) //12
			    result = result + capitalLetter.charAt(random.nextInt(26));
			return result;		
		}
		public String generateAlphaString(int min,int max) throws EolRuntimeException {
			
				if(max<min) return null;
				String result="";
				int ind= random.nextInt(max-min)+min;
				for (int i=0; i<ind; i++)
				    result = result + alphabet.charAt(random.nextInt(26));
				return result;				
		}
		public int generateInteger(int min, int max) throws EolRuntimeException {
			if(max<=min)
				return max;
			return random.nextInt(max-min)+min;				
		}
		public int generateUniformInteger(int min, int max) throws EolRuntimeException {
			if(max-min<6)
				return generateInteger(min,max);
			int diff=max-min;
			int mean =(diff/2)+min;
			int variance=diff/6;
			return generateUniformInteger(min,max,mean,variance);				
		}
		public int generateUniformInteger(int min, int max, int mean, int variance) throws EolRuntimeException {
			if(max<=min||mean+variance>max||mean-variance<min||variance<0)
				return 0;
			int index=min-1;
			double ind;
			while(index<min||index>max){
				ind=random.nextGaussian()*variance+mean;
				index= (int)ind;
			}
			return index;				
		}
		public float generateReal(int min,int max) throws EolRuntimeException {
			int diff=max-min;
			if(max<=min)
				return max;
			return random.nextFloat()*diff+min;	
			
		}
		public float generateUniformReal(int min, int max) throws EolRuntimeException {
			if(max-min<6)
				return generateReal(min,max);
			int diff=max-min;
			int mean =(diff/2)+min;
			int variance=diff/6;
			return generateUniformReal(min,max,mean,variance);				
		}
		public float generateUniformReal(int min, int max, int mean, int variance) throws EolRuntimeException {
			if(max<=min||mean+variance>max||mean-variance<min||variance<0)
				return 0;
			float index=min-1;
			double ind;
			while(index<min||index>max){
				ind=random.nextGaussian()*variance+mean;
				index= (float) ind;
			}
			return index;					
		}
		public boolean generateBoolean() throws EolRuntimeException {
			if (random.nextInt(2)==0)
				return true;	
			return false;
			
		}
		public boolean generateBoolean(int scale) throws EolRuntimeException {
			if(scale<1||scale>10) return generateBoolean();
			if (random.nextInt(10)<scale)
				return true;	
			return false;
			
		}*/
		 
		/*public Collection<?> deterministicRandomSize(int minSize,int maxSize) throws EolRuntimeException {
			//target <- Student.all
			Collection targetCollection;
			if(target instanceof Collection<?>){
				targetCollection = (Collection) target;// change the target to a collection
			}
			else{
				System.err.println("only collections are allowed!");
				return null;
			}
			if(maxSize-minSize<0 && maxSize!= -1){
				System.err.println("maximum size must not be less than minimum size");
				return null;
			}
			int seedValue,limitValue;//values of the variables
			
			//check if there is a seed
			if(context.getFrameStack().contains("seed")){
				//seed = context.getFrameStack().get("seed").getValue();
				seedValue=(int) context.getFrameStack().get("seed").getValue();
			}
			else{
				//we create a new seed and add it to the context
				seedValue=(int) System.currentTimeMillis();
				context.getFrameStack().put(Variable.createReadOnlyVariable("seed", seedValue));
				//seed= new Variable("seed",seedValue,null);
				//context.getFrameStack().put(seed);
			}
			
			//check if there is a limit
			if(context.getFrameStack().contains("limit")){
				//limit = context.getFrameStack().get("limit");
				limitValue=(int) context.getFrameStack().get("limit").getValue();
			}
			else{
				limitValue=targetCollection.size();
				
			}	
			Random random= new Random(seedValue);
			int size;
			if(maxSize==-1) size=random.nextInt()+minSize;
			else size=random.nextInt(maxSize-minSize)+minSize;
			
			return deterministicRandomBase(size,seedValue,limitValue);				
			
		}
		
		public Collection<?> deterministicRandomSize(int size) throws EolRuntimeException {
			//target <- Student.all
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
			int seedValue,limitValue;//values of the variables
			
			//check if there is a seed
			if(context.getFrameStack().contains("seed")){
				//seed = context.getFrameStack().get("seed").getValue();
				seedValue=(int) context.getFrameStack().get("seed").getValue();
			}
			else{
				//we create a new seed and add it to the context
				seedValue=(int) System.currentTimeMillis();
				context.getFrameStack().put(Variable.createReadOnlyVariable("seed", seedValue));
				//seed= new Variable("seed",seedValue,null);
				//context.getFrameStack().put(seed);
			}
			
			//check if there is a limit
			if(context.getFrameStack().contains("limit")){
				//limit = context.getFrameStack().get("limit");
				limitValue=(int) context.getFrameStack().get("limit").getValue();
			}
			else{
				limitValue=targetCollection.size();
				
			}	
			
			return deterministicRandomBase(size,seedValue,limitValue);				
			
		}*/
		

