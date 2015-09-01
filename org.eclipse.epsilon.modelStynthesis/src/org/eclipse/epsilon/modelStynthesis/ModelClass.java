package org.eclipse.epsilon.modelStynthesis;
import java.io.File;
//import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
import org.eclipse.emf.ecore.impl.EClassImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.epsilon.common.parse.problem.ParseProblem;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.emc.emf.EmfUtil;
import org.eclipse.epsilon.eol.EolModule;
import org.eclipse.epsilon.eol.dom.Annotation;
import org.eclipse.epsilon.eol.dom.AnnotationBlock;
import org.eclipse.epsilon.eol.dom.Operation;
import org.eclipse.epsilon.eol.dom.Parameter;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelElementTypeNotFoundException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.execute.context.EolContext;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.execute.operations.contributors.IterableOperationContributor;
import org.eclipse.epsilon.eol.execute.operations.contributors.OperationContributor;
//import org.eclipse.emf.common.util.URI;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.models.IRelativePathResolver;
import org.eclipse.epsilon.eugenia.operationcontributors.EModelElementOperationContributor;

//import Eclass;

public class ModelClass {
	static RandomGenerator random;
	IEolContext context;
	EolModule module;
	EmfModel model;
	Map<String,EClassifier> classes = new HashMap<String,EClassifier>();//maps created class names to their classifiers
	
	public ModelClass(){
		//context = new EolContext();
		module = new EolModule();
		random= new RandomGenerator();
	}
	
	protected void executeModule(File ecoreFile,File eolFile) throws Exception{
		java.net.URI temp= ecoreFile.toURI();
		URI ecoreUri= URI.createURI(temp.getPath());
		//URI ecoreUri= URI.createPlatformResourceURI("org.eclipse.epsilon.modelStynthesis/src/org/eclipse/epsilon/modelStynthesis/Ecore.ecore", true);
		//System.out.println(ecoreUri.toString());
		URI ecoreNew = ecoreUri.trimFileExtension();
		//System.out.println(ecoreUri.trimSegments(1).appendSegment("EcoreNw.ecore").toString());
		
		//URI test = URI.createPlatformResourceURI("C:/Users/Popoola/git/ModelClass/org.eclipse.epsilon.modelStynthesis/src/org/eclipse/epsilon/modelStynthesis/Ecore.ecore",true);
		
		String ecoreU= ecoreUri.toString();
		String ecoreN= ecoreNew.toString()+"new.ecore";
		//String te = "/C:/Users/Popoola/git/ModelClass/org.eclipse.epsilon.modelStynthesis/src/org/eclipse/epsilon/modelStynthesis/Ecore.ecore";
		//String te2 = "/C:/Users/Popoola/git/ModelClass/org.eclipse.epsilon.modelStynthesis/src/org/eclipse/epsilon/modelStynthesis/std.ecore";
		
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
		
		//context.getModelRepository().addModel(model);
		//module.setContext(context);
		module.getContext().getModelRepository().addModel(model1);
		context=module.getContext();
		context.setModule(module);
		//operator.generateString(5, 5);
		//context.getOperationContributorRegistry().add(new EModelElementOperationContributor());
		context.getOperationContributorRegistry().add(new ObjectOperationContributor(random));
		EmfModel model2= executeOperations(module.getOperations(),model1,ecoreN);
		//model2.store(ecoreN.substring(1));
		//System.out.println(model2.hasType("String"));
		/*EClassifier eobject;
		for(EObject eo: model.allContents()){
			if(eo instanceof EDataType ){
				classes.put(((EDataType) eo).getName(), (EDataType) eo);
				//System.out.println(((EDataType) eo).getName());
			}
			else if(eo instanceof EClassifier ){
				classes.put(((EClassifier) eo).getName(), (EClassifier) eo);
				//System.out.println(((EDataType) eo).getName());
			}
				
				
		}*/
		//EClassifier data= model2.classForName("EDataType");
		//model2.
		//System.out.println(model.allContents().toString());
		System.out.println("Model Generation Successful");
		module.getContext().getModelRepository().dispose();
		//model2.
		
		
	}//end mainClass
	protected EmfModel executeOperations(ArrayList<Operation> operationNames,EmfModel model2, String ne) throws EolModelElementTypeNotFoundException, EolRuntimeException{
		//ArrayList<Operation> operationNames= new ArrayList<Operation>(); //all the operation to be executed
		AnnotationBlock annotationBlock;
		Map<String,Collection> classGroup= new HashMap<String,Collection>();
		//Map<String,EStructuralFeature> referenceMap=new HashMap<String,EStructuralFeature>();//links references to their types
		//EPackage for new and general
		//EPackage pack,p;
		//pack= EcoreFactory.eINSTANCE.createEPackage();
		String name,operationName = "";
		//pack = EcoreFactory.eINSTANCE.createEPackage();
		//map the original package to the new package
		/*Map<String,EPackage> packages=new HashMap<String,EPackage>();
		// get all the root packages and map them to a newly created packages
		for (EObject eo : model.getResource().getContents()) {
			//System.out.println();
			if (eo instanceof EPackage) {
				p = (EPackage) eo;
				pack.setNsURI(p.getNsURI());
				pack.setNsPrefix(p.getNsPrefix());
				pack.setName(p.getName());
				//packages.
				packages.put(p.getName(), pack);
				//break;
				
			}
		}*/
		
		//search through the operations
		EClass eclass,clasNew;
		//module.getOperations()
		for (Operation operation: operationNames){
			//get the class context
			eclass = model2.classForName(operation.getContextType(context).getName());
			//System.out.println(eclass.getEPackage().getName());
			if(eclass.isAbstract() || eclass.equals(null))
				continue;
			//factory= eclass.getEPackage().getEFactoryInstance();
			//clasNew= (EClass) EcoreFactory.eINSTANCE.create(eclass.eClass());
			//p=eclass.getEPackage();
			//System.out.println(eclass.getEPackage().getName());
			//pack=packages.get(p.getName());
			//clasNew.setName(random.generateString());
			//System.out.println(random.generateString());
			//System.out.println(random.generateString());
			
			int instances = 1;
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
						instances = (Integer) annotationValues.get(0);
						}
					}
					if(name.equals("name")){
						if (!annotationValues.isEmpty()) {
							operationName = (String) annotationValues.get(0);
						}
					}
					
				}//end for loop annotations
			}
			//classes.put(clasNew.getName(), clasNew);
			//bigtest=clasNew.getInstanceClassName();
			//System.out.println(clasNew.getName());
			// how many instances of the class to create
			if(operation.getName().equals("create")){
				ArrayList classes= new ArrayList();
				for(int i=0;i<instances;i++){		
					//System.out.println(model2.toString());
					Object modelObject = model2.createInstance(operation.getContextType(context).getName());
					operation.execute(modelObject, null, context);
					//classes.add(modelObject);
					
				}
			}
			if(operation.getName().equals("link")){
				
				operation.execute(model2.getAllOfType(operation.getContextType(context).getName()), null, context);
					//cla.
					//cla.getEStructuralFeatures().add((EStructuralFeature) cla2);
					//ref.
				
				
				
				//System.out.println("size "+operation.getFormalParameters().size());
			}
				
		}//end for loop (operations)
		
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
		File eolFile= new File("src/org/eclipse/epsilon/modelStynthesis/operations.eol");
		try {
			model.executeModule(ecoreFile, eolFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
/*	protected static File getFile(String fileName) throws URISyntaxException {
		
		URI binUri = ModelClass2.class.
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
	public static void writeme(){
		try{
			
		}
		catch(Exception e){
			System.out.print(""+ e.getMessage());
		}
	}
	*/
	//eo = op.execute(module, null, null);
	
	/*public static void main(String [] args) throws Exception{
	String bigtest= "fg";
	// map of a class name to a list of references
	File ecoreFile = new File("src/org/eclipse/epsilon/modelStynthesis/Ecore.ecore");
	java.net.URI temp= ecoreFile.toURI();
	URI ecoreUri= URI.createURI(temp.getPath());
	//URI ecoreUri= URI.createPlatformResourceURI("org.eclipse.epsilon.modelStynthesis/src/org/eclipse/epsilon/modelStynthesis/Ecore.ecore", true);
	//System.out.println(ecoreUri.toString());
	URI ecoreNew = ecoreUri.trimFileExtension();
	//System.out.println(ecoreUri.trimSegments(1).appendSegment("EcoreNw.ecore").toString());
	
	//URI test = URI.createPlatformResourceURI("C:/Users/Popoola/git/ModelClass/org.eclipse.epsilon.modelStynthesis/src/org/eclipse/epsilon/modelStynthesis/Ecore.ecore",true);
	EolContext context = new EolContext();
	EolModule module = new EolModule();
	String ecoreU= ecoreUri.toString();
	String ecoreN= ecoreNew.toString()+"New.ecore";
	//String te = "/C:/Users/Popoola/git/ModelClass/org.eclipse.epsilon.modelStynthesis/src/org/eclipse/epsilon/modelStynthesis/Ecore.ecore";
	//String te2 = "/C:/Users/Popoola/git/ModelClass/org.eclipse.epsilon.modelStynthesis/src/org/eclipse/epsilon/modelStynthesis/std.ecore";
	
	File file= new File("src/org/eclipse/epsilon/modelStynthesis/operations.eol");
	
	ArrayList<Operation> operationNames= new ArrayList<Operation>(); //all the operation to be executed
	AnnotationBlock annotationBlock;
	Map<String,EStructuralFeature> referenceMap=new HashMap<String,EStructuralFeature>();//links references to their types
	boolean me = module.parse(file);
	//check for errors in parsing file
	if (module.getParseProblems().size() > 0) {
		System.err.println("Parse errors occured...");
		for (ParseProblem problem : module.getParseProblems()) {
			System.err.println(problem.toString());
		}
		return;
	}
	context.getFrameStack().put(Variable.createReadOnlyVariable("size", 3));
	context.getFrameStack().put(Variable.createReadOnlyVariable("seed", 1000));
	
	
	
	//create a new model
	EmfModel model= createEmfModel("Model", ecoreU,ecoreU, true, false);
	EmfModel model2= createEmfModel("Model2", ecoreN,ecoreU, false, true);//empty model
	
	//EPackage for new and original
	EPackage pack,p;
	
	context.getModelRepository().addModel(model);
	module.setContext(context);
	//operator.generateString(5, 5);
	//context.getOperationContributorRegistry().add(new EModelElementOperationContributor());
	//context.getOperationContributorRegistry().add(new CollectionOperationContributor());
	
	String name;
	//pack = EcoreFactory.eINSTANCE.createEPackage();
	//map the original package to the new package
	Map<String,EPackage> packages=new HashMap<String,EPackage>();
	
	// get all the root packages and map them to a newly created packages
	for (EObject eo : model.getResource().getContents()) {
		if (eo instanceof EPackage) {
			pack = EcoreFactory.eINSTANCE.createEPackage();
			p = (EPackage) eo;
			pack.setNsURI(p.getNsURI());
			pack.setNsPrefix(p.getNsPrefix());
			pack.setName(p.getName());
			//packages.
			packages.put(p.getName(), pack);
			//metaPackag.add(p);
			
		}
	}
	
	//search through the operations
	EClass eclass,clasNew;
	for (Operation operation: module.getOperations()){
		//get the class context
		eclass = model2.classForName(operation.getContextType(context).getName());
		//System.out.println(eclass.getEPackage().getName());
		if(eclass.isAbstract() || eclass.equals(null))
			continue;
		//factory= eclass.getEPackage().getEFactoryInstance();
		clasNew= (EClass) EcoreFactory.eINSTANCE.create(eclass.eClass());
		p=eclass.getEPackage();
		//System.out.println(eclass.getEPackage().getName());
		pack=packages.get(p.getName());
		clasNew.setName(random.generateString());
		//System.out.println(random.generateString());
		//System.out.println(random.generateString());
		
		
		//get the annotations
		annotationBlock = operation.getAnnotationBlock();
		if(annotationBlock==null)
			continue;
		List<Object> annotationValues;
		int instances = 1;
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
				instances = (Integer) annotationValues.get(0);
				}
			}
			if(name.equals("annotate")){
				
				if (!annotationValues.isEmpty()) {
					ann=(String) annotationValues.get(0);
					//System.out.println("annotaions before execution: "+eclass.getEAnnotations().size());
					EAnnotation eAnnotation = getAnnotation(ann);
					//EcoreFactory.eINSTANCE.createEAnnotation();
					//EcoreFactory.eINSTANCE.createEPackage()
					//eAnnotation.setSource(ann);
					//model.getResource().
					clasNew.getEAnnotations().add(eAnnotation);
					//System.out.println("successfully annotated class: "+ eclass.getName() );
					//annotate(eclass,ann);
					//annotateRef(eclass,ann);
					//System.out.println("annotaions after execution: "+eclass.getEAnnotations().size());
				//eclass.
				}
			}
			if(name.equals("create")){
				
				if (!annotationValues.isEmpty()) {
					ann=(String) annotationValues.get(0);
					EClass refClas;
					EStructuralFeature refClass;
					if(ann.contains("(")){
						String[] temp2= ann.split("\\(", 2);
						refClas = model2.classForName(temp2[0].trim());
						temp2[1]=temp2[1].trim();
						temp2[1]=temp2[1].substring(0, temp2[1].length()-1);
						refClass=getFeature(temp2[1]);
						referenceMap.put(temp2[0].trim(), refClass);
					}
					else{
						refClas = model2.classForName(ann);
						refClass=getFeature("");
						referenceMap.put(ann, refClass);
					}
					//System.out.println("annotaions before execution: "+eclass.getEAnnotations().size());
					
					//refClass= EcoreFactory.eINSTANCE.createEReference();
					//EStructuralFeature d=EcoreFactory.eINSTANCE.createEAttribute();
					//refClass.
					//EcoreFactory.eINSTANCE.createEAttribute();
					refClass.setEType(refClas);
					//EcoreFactory.eINSTANCE.createEPackage()
					//if(!annotationValues.get(1).equals(null))
						//refClass.setName((String)annotationValues.get(1));
					//else
						//refClass.setName(refClas.getName());
					
					//model.getResource().
					clasNew.getEStructuralFeatures().add(refClass);
					//System.out.println("successfully annotated class: "+ eclass.getName() );
					//annotate(eclass,ann);
					//annotateRef(eclass,ann);
					//System.out.println("annotaions after execution: "+eclass.getEAnnotations().size());
				//eclass.
				}
			}
			if(name.equals("name")){
				if (!annotationValues.isEmpty()) {
					ann=(String) annotationValues.get(0);
					clasNew.setName(ann);
					//System.out.println(clasNew.getInstanceClassName());
					//System.out.println(clasNew.getInstanceTypeName());
					//model2.classForName(name);
				}
			}
			if(name.equals("annotateRefClass")){
				
				if (!annotationValues.isEmpty()) {
					ann=(String) annotationValues.get(0);
							
				}
			}
			
		}
		//bigtest=clasNew.getInstanceClassName();
		//System.out.println(clasNew.getName());
		// how many instances of the class to create
		if(instances<=1)
			pack.getEClassifiers().add(clasNew);
		else{	
			EClass clas2;
			for (int i = 0; i<instances; i++) {
				
				clas2= (EClass) EcoreUtil.copy(clasNew);
				clas2.setName(clasNew.getName()+i);
				
				pack.getEClassifiers().add(clas2);
				//EString fg;
				//System.out.println("name"+ pack.getName());
				
			}
		}
	}//end for loop (operations)
	
	//add the packages to the model
	
	for (Entry<String, EPackage> entry : packages.entrySet())
	{
		//System.out.println(entry.getValue().getName()+"test");
		p= entry.getValue();
		if(p.getEClassifiers().size()>0)
			model2.getResource().getContents().add(p);		
	}
	//model2.store(ecoreN.substring(1));
	//System.out.println(model2.allContents().toString());
	System.out.println(bigtest);
	System.out.println(model2.hasType(bigtest));
	//model2.classForName("Student");
	System.out.println("Model Generation Successful");
	module.getContext().getModelRepository().dispose();
	
	
}//end main class
*/
	
		
}
