
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.eclipse.epsilon.emc.emf.EmfMetaModel;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.emc.emf.EmfUtil;
//import org.eclipse.emf.common.util.URI;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.models.IRelativePathResolver;
import org.eclipse.epsilon.eol.types.EolType;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.epsilon.common.parse.AST;
import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.dom.Annotation;
import org.eclipse.epsilon.eol.dom.AnnotationBlock;
import org.eclipse.epsilon.eol.dom.Operation;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.execute.context.EolContext;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.execute.operations.contributors.IterableOperationContributor;
import org.eclipse.epsilon.eol.execute.operations.contributors.OperationContributor;

public class ModelClass {
	public static void main(String [] args) throws Exception{
		EolContext context = new EolContext();
		EolModule module = new EolModule();
		File file= new File("operations.eol");
		ArrayList<Operation> operationNames= new ArrayList<Operation>(); //all the operation to be executed
		AnnotationBlock annotationBlock;
		boolean me = module.parse(file);
		//check for errors in parsing file
		if (module.getParseProblems().size() > 0) {
			System.err.println("Parse errors occured...");
			for (ParseProblem problem : module.getParseProblems()) {
				System.err.println(problem.toString());
			}
			return;
		}
		//org.eclipse.epsilon.emc.emf
		//List<IModel> models = new ArrayList<IModel>();
		//models.add(createEmfModel("Model", "student.model","student.ecore", true, true));
		
		//load a new ecore
		String metamodel =getFile("student.ecore").getAbsolutePath();
		List<EPackage> metaPackage= EmfUtil.register(org.eclipse.emf.common.util.URI.createFileURI(metamodel), EPackage.Registry.INSTANCE);
		
		//EmfMetaModel meta = new EmfMetaModel();
		//meta.load();
		
		//create a new model
		IModel model= createEmfModel("Model", "student.model","student.ecore", false, true);
		context.getModelRepository().addModel(model);
		module.setContext(context);
		//OperationContributor ade;
		context.getOperationContributorRegistry().add(new OperationContributor() {
			
			@Override
			public boolean contributesTo(Object target) {
				return target instanceof Collection<?>;
				//return true;
			}
			public void setSeed(int seed){
				if(!context.getFrameStack().contains("seed")){
					context.getFrameStack().put(Variable.createReadOnlyVariable("seed", seed));				
				}
			}
			public Collection<?> deterministicRandom() throws EolRuntimeException {
				//target <- Student.all
				Collection targetCollection;
				if(target instanceof Collection<?>){
					targetCollection = (Collection) target;// change the target to a collection
				}
				else{
					System.err.println("only collections are allowed!");
					return null;
				}
				
				//IterableOperationContributor contributor= new IterableOperationContributor(targetCollection);
				//Collection<Object> out = contributor.createCollection(); // the output collection
				//Variable seed ;//seed
				//Variable limit;//limit of the search in the target collection
				//Variable size;// size of the return collection
				int seedValue,limitValue,sizeValue;//values of the variables

				
				
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
					//context.getFrameStack().put(Variable.createReadOnlyVariable("limit", limitValue));
					//limit= new Variable("limit",limitValue,null);
					//context.getFrameStack().put(limit);
				}
				
				//check if there is a required size for the output collection
				if(context.getFrameStack().contains("sizes")){
					//size = context.getFrameStack().get("sizes");
					sizeValue = (int) context.getFrameStack().get("sizes").getValue();
				}
				else{
					Random random= new Random(seedValue);
					sizeValue = random.nextInt(limitValue);
					//sizeValue =1;
					//context.getFrameStack().put(Variable.createReadOnlyVariable("sizes", sizeValue));
					//size= new Variable("limit",sizeValue,null);
					//context.getFrameStack().put(size);
				}		
				
				return deterministicRandom(sizeValue,seedValue,limitValue);	
				//return deterministicRandom(2,100,5);	
				
			}
			
			public Collection<?> deterministicRandom(int seed) throws EolRuntimeException {
				//target <- Student.all
				Collection targetCollection;
				if(target instanceof Collection<?>){
					targetCollection = (Collection) target;// change the target to a collection
				}
				else{
					System.err.println("only collections are allowed!");
					return null;
				}
				
				int  size,limitValue;//values of the variables

				//check if there is a limit
				if(context.getFrameStack().contains("limit")){
					//limit = context.getFrameStack().get("limit");
					limitValue=(int) context.getFrameStack().get("limit").getValue();
				}
				else{
					limitValue=targetCollection.size();
					//limit different for each collections
					//context.getFrameStack().put(Variable.createReadOnlyVariable("limit", limitValue));
					//limit= new Variable("limit",limitValue,null);
					//context.getFrameStack().put(limit);
				}	
				//check if there is a general size
				if(context.getFrameStack().contains("sizes")){
					//seed = context.getFrameStack().get("seed").getValue();
					size=(int) context.getFrameStack().get("sizes").getValue();
				}
				else{
					Random random = new Random(seed);
					//we create a new seed and add it to the context
					size = random.nextInt(limitValue);
					//context.getFrameStack().put(Variable.createReadOnlyVariable("seed", seedValue));
					//seed= new Variable("seed",seedValue,null);
					//context.getFrameStack().put(seed);
				}
				
				
				
				return deterministicRandom(size,seed,limitValue);				
				
			}
			public Collection<?> deterministicRandom(int seed,int limit) throws EolRuntimeException {
				//target <- Student.all
				Collection targetCollection;
				if(target instanceof Collection<?>){
					targetCollection = (Collection) target;// change the target to a collection
				}
				else{
					System.err.println("only collections are allowed!");
					return null;
				}
				
				int size;
				//check if there is a general size
				if(context.getFrameStack().contains("sizes")){
					//seed = context.getFrameStack().get("seed").getValue();
					size=(int) context.getFrameStack().get("sizes").getValue();
				}
				else{
					Random random = new Random(seed);
					//we create a new seed and add it to the context
					size = random.nextInt(limit);
					//context.getFrameStack().put(Variable.createReadOnlyVariable("seed", seedValue));
					//seed= new Variable("seed",seedValue,null);
					//context.getFrameStack().put(seed);
				}
				
								
				
				return deterministicRandom(size,seed,limit);		
				
			}
			public Collection<?> deterministicRandom(int size,int seed,int limit) throws EolRuntimeException {
				//target <- Student.all
				/*
				 * *this method generates a deterministic collection as output
				 * the limit is used in case new things has been added to the collection
				 * so we stop at the previous size of the collection
				 */
				Collection targetCollection;
				if(target instanceof Collection<?>){
					targetCollection = (Collection) target;// change the target to a collection
				}
				else{
					System.err.println("only collections are allowed!");
					return null;
				}
				
				IterableOperationContributor contributor= new IterableOperationContributor(targetCollection);
				Collection<Object> out = contributor.createCollection(); // the output collection
				
				//random number and the seed
				Random random= new Random(seed);
				
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
			 
			public Collection<?> deterministicRandomSize(int minSize,int maxSize) throws EolRuntimeException {
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
				
				return deterministicRandom(size,seedValue,limitValue);				
				
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
				
				return deterministicRandom(size,seedValue,limitValue);				
				
			}
			
			
		});
		
		//System.out.println(ast.hasChildren());
		
		context.getFrameStack().put(Variable.createReadOnlyVariable("size", 10));
		//context.getFrameStack().put(Variable.createReadOnlyVariable("seed", 10));
		
		
		//get the names of the operation to be executed
		//the default is to execute all the operations in the module
		for (Operation operation: module.getOperations()){
			operationNames.add(operation);
			//System.out.println(operation.getName());
			//annotationBlock = operation.getAnnotationBlock();
			
		}
		
		//get the annotations
		annotationBlock=module.getOperations().get(0).getAnnotationBlock();
		for(Annotation annotation: annotationBlock.getAnnotations()){
			//System.out.println(annotation.getValue(context) + "  Stringh");
		}

		//check for classes in the metamodel and execute the method
		for(EPackage po:metaPackage ){
			for(EClassifier clas:po.getEClassifiers() ){
				if(clas instanceof EClass){
					EClass eclass= (EClass) clas;
					if(!(eclass.isAbstract())){
						for (Operation operation: operationNames){
							if(operation.getContextType(context).getName().equals(eclass.getName())){
								annotationBlock = operation.getAnnotationBlock();
								List<Object> annotationValues;
								for(Annotation annotation:annotationBlock.getAnnotations()){
									//annotationValues.addAll()
								}
								Annotation annotation= annotationBlock.getAnnotations().get(0);
								if(!(annotation.hasValue()))
									continue;									
								annotationValues = operation.getAnnotationsValues(annotation.getName(), context);
								int instances = 1;
								if (!annotationValues.isEmpty()) {
									instances = (Integer) annotationValues.get(0);
								}
								
								for (int i = 0; i<instances; i++) {
									//System.out.println("found create method for: " + eclass.getName());
									
									Object modelObject = model.createInstance(eclass.getName());
									operation.execute(modelObject, null, context);
								}
							}
						}
					}
				}
				//op.execute(clas,null,context,true);		
			}
			
		}
		module.getContext().getModelRepository().dispose();
		/*for(Object po: model.allContents()){
			//System.out.println(po);
			op.execute(po,null,context,true);
		}*/
		
	}
	
	//method
	protected void executeMethod(File file, String EcorePath){
		
	}
	
	//get list of non-abstract classes
	public List<EClass> getEClasses(List<EPackage> metaPackage){
		Collection<Object> eEClass=new IterableOperationContributor().createCollection();
		List<EClass> eClass = (List)eEClass;
		for(EPackage po:metaPackage ){
			for(EClassifier clas:po.getEClassifiers() ){
				if(clas instanceof EClass){
					EClass eclass= (EClass) clas;
					if(!(eclass.isAbstract())){
						eClass.add(eclass);
					}
				}
				//op.execute(clas,null,context,true);		
			}
		
		}
		return eClass;
	}
	
	
	//get unassigned annotations
	protected static List<Annotation> unAssignedAnnotations(EolModule module,EolContext context) throws EolRuntimeException{
		Collection<Object> unAssigned = new IterableOperationContributor().createCollection();
		List<Annotation> unAssignedAnnotations= (List)unAssigned;
		List<Annotation> annotationList;
		if(module.getOperations().isEmpty())
			return unAssignedAnnotations;
		for(Operation operation:module.getOperations()){
			annotationList =operation.getAnnotationBlock().getAnnotations();
			if(!annotationList.isEmpty()){
				for(Annotation annotation: annotationList ){
					if(!annotation.hasValue()){
						unAssignedAnnotations.add(annotation);
						//System.out.println(annotation.getName());
					}
						
				}
				
			}
				
		}
		return unAssignedAnnotations;
	}
	
	
	//methods from org.eclipse.epsilon.examples.standalone.epsilonstandalone
	protected static EmfModel createEmfModel(String name, String model, 
			String metamodel, boolean readOnLoad, boolean storeOnDisposal) 
					throws EolModelLoadingException, URISyntaxException {
		EmfModel emfModel = new EmfModel();
		StringProperties properties = new StringProperties();
		properties.put(EmfModel.PROPERTY_NAME, name);
		properties.put(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI,
				getFile(metamodel).toURI().toString());
		properties.put(EmfModel.PROPERTY_MODEL_URI, 
				getFile(model).toURI().toString());
		properties.put(EmfModel.PROPERTY_READONLOAD, readOnLoad + "");
		properties.put(EmfModel.PROPERTY_STOREONDISPOSAL, 
				storeOnDisposal + "");
		emfModel.load(properties, (IRelativePathResolver) null);
		return emfModel;
	}
	protected static File getFile(String fileName) throws URISyntaxException {
		
		URI binUri = ModelClass.class.
				getResource(fileName).toURI();
		URI uri = null;
		
		if (binUri.toString().indexOf("bin") > -1) {
			uri = new URI(binUri.toString().replaceAll("bin", "src"));
		}
		else {
			uri = binUri;
		}
		
		return new File(uri);
	}
	/*public static void writeme(){
		try{
			
		}
		catch(Exception e){
			System.out.print(""+ e.getMessage());
		}
	}
	*/
	//eo = op.execute(module, null, null);
	
	
	
		
}
