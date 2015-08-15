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
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.exceptions.models.EolModelLoadingException;
import org.eclipse.epsilon.eol.execute.context.EolContext;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.execute.operations.contributors.IterableOperationContributor;
import org.eclipse.epsilon.eol.execute.operations.contributors.OperationContributor;
//import org.eclipse.emf.common.util.URI;
import org.eclipse.epsilon.eol.models.IModel;
import org.eclipse.epsilon.eol.models.IRelativePathResolver;
import org.eclipse.epsilon.eugenia.operationcontributors.EModelElementOperationContributor;

//import Eclass;

public class ModelClassNew {
	public static void main(String [] args) throws Exception{
		File ecoreFile = new File("src/org/eclipse/epsilon/modelStynthesis/Ecore.ecore");
		java.net.URI temp= ecoreFile.toURI();
		URI ecoreUri= URI.createURI(temp.getPath());
		URI ecoreNew = ecoreUri.trimFileExtension();
		
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
		EmfModel model2= createEmfModel("Model2", ecoreN,ecoreU, false, true);
		
		//EPackage for new and original
		EPackage pack,p;
		
		context.getModelRepository().addModel(model);
		module.setContext(context);
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
		for (Operation operation: module.getOperations()){
			//get the class context
			EClass eclass = model2.classForName(operation.getContextType(context).getName());
			//System.out.println(eclass.getEPackage().getName());
			if(eclass.isAbstract() || eclass.equals(null))
				continue;
			//factory= eclass.getEPackage().getEFactoryInstance();
			EClass clasNew= (EClass) EcoreFactory.eINSTANCE.create(eclass.eClass());
			p=eclass.getEPackage();
			//System.out.println(eclass.getEPackage().getName());
			pack=packages.get(p.getName());
			clasNew.setName(eclass.getName()+"New");
			
			
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
						EAnnotation eAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
						//EcoreFactory.eINSTANCE.createEPackage()
						eAnnotation.setSource(ann);
						//model.getResource().
						clasNew.getEAnnotations().add(eAnnotation);
						//System.out.println("successfully annotated class: "+ eclass.getName() );
						//annotate(eclass,ann);
						//annotateRef(eclass,ann);
						//System.out.println("annotaions after execution: "+eclass.getEAnnotations().size());
					//eclass.
					}
				}
				/*if(name.equals("annotateRefClass")){
					
					if (!annotationValues.isEmpty()) {
						ann=(String) annotationValues.get(0);
								
					}
				}*/
				
			}
			// how many instances of the class to create
			if(instances<=1)
				pack.getEClassifiers().add(clasNew);
			else{	
				EClass clas2;
				for (int i = 0; i<instances; i++) {
					
					clas2= (EClass) EcoreUtil.copy(clasNew);
					clas2.setName(clasNew.getName()+i);
					
					pack.getEClassifiers().add(clas2);
					//System.out.println("name"+ pack.getName());
					
				}
			}
		}//end for loop (operations)
		
		//add the packages to the model
		for (Entry<String, EPackage> entry : packages.entrySet())
		{
			//System.out.println(entry.getValue().getName()+"test");
			model2.getResource().getContents().add(entry.getValue());		
		}
		model2.store(ecoreN.substring(1));
		System.out.println("Model Generation Successful");
		module.getContext().getModelRepository().dispose();
		
		
	}//end main class
	
	
	
	
	
	
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
	
	
	
		
}
