package org.eclipse.epsilon.modelStynthesis;
import java.io.File;
//import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.epsilon.common.parse.AST;
import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.eol.dom.Annotation;
import org.eclipse.epsilon.eol.dom.AnnotationBlock;
import org.eclipse.epsilon.eol.dom.Operation;
import org.eclipse.epsilon.eol.dom.Parameter;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelElementTypeNotFoundException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.execute.ExecutorFactory;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.models.IRelativePathResolver;
import org.eclipse.epsilon.epl.EplModule;
//import org.eclipse.emf.common.util.URI;
import org.eclipse.epsilon.epl.dom.Pattern;
import org.eclipse.epsilon.epl.execute.PatternMatch;
import org.eclipse.epsilon.epl.execute.PatternMatchModel;
import org.eclipse.epsilon.epl.parse.EplParser;

public class ModelClass {
	RandomGenerator random;
	IEolContext context;
	CustomEplModule module;
	EmfModel model;
	//int nu=0; // for number annotations in pattern
	//int value=1; // for number annotations in pattern
	//Pattern pat=null;
	//Map<String,EClassifier> classes = new HashMap<String,EClassifier>();//maps created class names to their classifiers
	Map<String,Collection> classGroup= new HashMap<String,Collection>();//maps create names to collection of models
	public ModelClass(){
		//context = new EolContext();
		module = new CustomEplModule();
		random= new RandomGenerator(35467839);
	}
	
	protected void executeModule(File ecoreFile,File eolFile) throws Exception{
		java.net.URI temp= ecoreFile.toURI();
		URI ecoreUri= URI.createURI(temp.getPath());
		URI ecoreNew = ecoreUri.trimFileExtension();
		String ecoreU= ecoreUri.toString();
		String ecoreN= ecoreNew.toString()+"epl.ecore";
		
		File file= eolFile;
		boolean me = module.parse(file);
		//check for errors in parsing file
		if (module.getParseProblems().size() > 0) {
			System.err.println("Parse errors occured...");
			for (ParseProblem problem : module.getParseProblems()) {
				System.err.println(problem.toString());
			}
			return;
		}
		///context.getFrameStack().put(Variable.createReadOnlyVariable("size", 3));
		//context.getFrameStack().put(Variable.createReadOnlyVariable("seed", 1000));
		
		
		
		//create a new model
		model= createEmfModel("Model", ecoreU,ecoreU, true, false);
		EmfModel model1= createEmfModel("Model2", ecoreN,ecoreU, false, true);//empty model
		
		module.getContext().getModelRepository().addModel(model1);
		context=module.getContext();
		context.setModule(module);
		
		ArrayList create= new ArrayList<Operation>();
		ArrayList link= new ArrayList<Operation>();
		context.getOperationContributorRegistry().add(new CollectionOperationContributor(random));
		context.getOperationContributorRegistry().add(new ObjectOperationContributor(random,model1,classGroup));
		//context.
		/*module.getContext().setExecutorFactory(new ExecutorFactory() {
			@Override
			public Object executeAST(AST ast, IEolContext context)
					throws EolRuntimeException {
				
				if (ast != null && ast.getParent() != null && ast.getParent().getType() == EplParser.MATCH) {
					Pattern pattern = (Pattern) ast.getParent().getParent();
					
					//System.out.println(pattern.getOnMatchAst().getType());
					
					//context.
					//System.out.println(getExecutorFor(pattern.getOnMatchAst().getType()));
					Boolean result = (Boolean) super.executeAST(ast, context);
					if(result){
						
						if (pattern.hasAnnotation("number")) {
							if(!pattern.equals(pat)){
								nu=0;
								pat=pattern;						
								value=1;
								List vals=pattern.getAnnotationsValues("number", context);							
								//System.out.println(vals.get(0)+"");
								if(vals.size()>1){
									Object val= vals.get(0);
									Object val2= vals.get(1);
									if(!(val.equals(null) || (val2.equals(null)))){
										value = random.generateInteger(getInt( val),getInt( val2));									
									}
								}
								else if(vals.size()>0){
									Object val= vals.get(0);
									
									if(!(val.equals(null))){
										if(val instanceof Collection){
											List valC= (List)val;
											if(valC.size()>1)
												value = random.generateInteger(getInt(valC.get(0)),getInt(valC.get(1)));
											else
												value = getInt(valC.get(0));
										}
										else
											value=getInt(val);
										//value=Integer.parseInt((String) val);
									}
								}
								
								
							}//end if !pattern	
							//if(result){
							if(value>nu){
								nu++;
								return result;
							}
							else return !result;
							
						}//end annotation number
						if (pattern.hasAnnotation("probability")) {
							Object val= pattern.getAnnotationsValues("probability", context).get(0);
							float value=1;
							if((!val.equals(null)) && (val instanceof Float)) value=(float) val;
							if(random.generateBoolean(value)==false) return !result;
						}//end annotation probability
					}//enf if result
				}				
				
				return super.executeAST(ast, context);
			}
			private int getInt(Object object){
				if(object instanceof Integer)
					return (int)object;
				else
					return Integer.parseInt((String) object);
				
			}
		});*/
		for (Operation operation: module.getOperations()){
			String name = operation.getName();
			if(name.equals("create"))
				create.add(operation);
			else if(name.equals("link"))
				link.add(operation);
		}
		create.addAll(link);
		EmfModel model2= executeOperations(create,model1,ecoreN);
		//Object[] pat;
		//pat=((PatternMatchModel)module.execute()).getMatches().toArray();
		//PatternMatch tes= (PatternMatch) pat[0];
		//System.out.println(tes.getRoleBindings());
		module.execute();
		System.out.println("Model Generation Successful");
		module.getContext().getModelRepository().dispose();
		//model2.
		
		
	}//end mainClass
	protected EmfModel executeOperations(ArrayList<Operation> operationNames,EmfModel model2, String ne) throws EolModelElementTypeNotFoundException, EolRuntimeException{
		//ArrayList<Operation> operationNames= new ArrayList<Operation>(); //all the operation to be executed
		AnnotationBlock annotationBlock;
		String name,operationName,guard;
		
		//search through the operations
		EClass eclass,clasNew;
		//module.getOperations()
		for (Operation operation: operationNames){
			//get the class context
			eclass = model2.classForName(operation.getContextType(context).getName());
			//System.out.println(eclass.getEPackage().getName());
			if(eclass.isAbstract() || eclass.equals(null))
				continue;
			
			int instances = 1;
			operationName="";guard="";
			//get the annotations
			annotationBlock = operation.getAnnotationBlock();

			if(!(annotationBlock==null)){
				List<Object> annotationValues;
				//System.out.println("size"+annotationBlock.getAnnotations().size());
				for(Annotation annotation:annotationBlock.getAnnotations()){
					//Annotation annotation= annotationBlock.getAnnotations().get(0);
					if(!(annotation.hasValue()))
						continue;	
					name= annotation.getName();
					annotationValues = operation.getAnnotationsValues(name, context);
					String ann;
					if(name.equals("instances")){
						if (!annotationValues.isEmpty()) {
							//System.out.println(annotationValues.size());
							Object val=annotationValues.get(0);
							if(val instanceof List){
								List valC = (List)val;
								if(valC.size()>1)
									instances = random.generateInteger((int)valC.get(0), (int) valC.get(1));
								else
									instances= (int) valC.get(0);
							}
							else
								instances = (int) annotationValues.get(0);
							//System.out.println(instances);
						}
					}
					else if(name.equals("name")){
						if (!annotationValues.isEmpty()) {
							operationName = (String) annotationValues.get(0);
							//System.out.println(operationName);
						}
					}
					else if(name.equals("guard")){
						if (!annotationValues.isEmpty()) {
							guard = (String) annotationValues.get(0);
						}
					}
					
				}//end for loop annotations
			}

			// how many instances of the class to create
			if(operation.getName().equals("create")){
				ArrayList classes= new ArrayList();
				for(int i=0;i<instances;i++){		
					//System.out.println(model2.toString());
					Object modelObject = model2.createInstance(operation.getContextType(context).getName());
					operation.execute(modelObject, null, context);
					classes.add(modelObject);
					
				}
				if(!operationName.isEmpty()){
					if(classGroup.containsKey(operationName)){
						classGroup.get(operationName).addAll(classes);
					}
					else
						classGroup.put(operationName, classes);
				}
					
			}
			if(operation.getName().equals("link")){
				Collection type;
				ArrayList parameters= new ArrayList<>();
				//System.out.println("guard"+ guard);
				if(!guard.isEmpty()){
					if(guard.contains(",")){
						Boolean found=false;
						type=new ArrayList<>();
						for(String p:guard.split("\\,")){
							if(classGroup.containsKey(p)){
								type.addAll(classGroup.get(p));
								found=true;
							}
								
							else
								System.err.println("no operation create was named: "+p);
						}
						if(!found)
							type= model2.getAllOfType(operation.getContextType(context).getName());	
					}
					else if(classGroup.containsKey(guard))
						type=classGroup.get(guard);
					else
						type= model2.getAllOfType(operation.getContextType(context).getName());	
				}			
				else
					type= model2.getAllOfType(operation.getContextType(context).getName());
				String pName;
				for(Parameter p:operation.getFormalParameters() ){
					pName=p.getName();
					if(classGroup.containsKey(pName)){
						parameters.add(classGroup.get(pName));
					}
					else{
						System.out.println("ERROR: no operation was named:  " + pName);
						parameters.add(null);
					}
						
				}
				if(parameters.size()>0){
					for(Object t: type)
						operation.execute(t, parameters, context);
				}
				else{
					for(Object t: type)
						operation.execute(t, null, context);
				}
				
			}
				
		}//end for loop (operations)
		//model2.
		model2.store(ne.substring(1));
		return model2;
		
	}
	
	
	//methods from org.eclipse.epsilon.examples.standalone.epsilonstandalone
	protected static EmfModel createEmfModel(String name, String model, 
			String metamodel, boolean readOnLoad, boolean storeOnDisposal) 
					throws EolModelLoadingException, URISyntaxException {
		EmfModel emfModel = new EmfModel();
		StringProperties properties = new StringProperties();
		properties.put(EmfModel.PROPERTY_NAME, name);
		properties.put(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI,
				metamodel);
		properties.put(EmfModel.PROPERTY_MODEL_URI, 
				model);
		properties.put(EmfModel.PROPERTY_READONLOAD, readOnLoad + "");
		properties.put(EmfModel.PROPERTY_STOREONDISPOSAL, 
				storeOnDisposal + "");
		emfModel.load(properties, (IRelativePathResolver) null);
		return emfModel;
	}
	
	public static void main(String[] args){
		ModelClass model= new ModelClass();
		File ecoreFile = new File("src/org/eclipse/epsilon/modelStynthesis/Ecore.ecore");
		File eolFile= new File("src/org/eclipse/epsilon/modelStynthesis/operations.epl");
		try {
			model.executeModule(ecoreFile, eolFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
		
}
