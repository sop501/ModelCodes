package org.eclipse.epsilon.modelStynthesis;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.impl.DynamicEObjectImpl;
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

public class ModelClass {
	public static void main(String [] args) throws Exception{
		EolContext context = new EolContext();
		EolModule module = new EolModule();
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
		
		//org.eclipse.epsilon.emc.emf
		//List<IModel> models = new ArrayList<IModel>();
		//models.add(createEmfModel("Model", "student.model","student.ecore", true, true));
		
		//load a new ecore
		String metamodel =getFile("student.ecore").getAbsolutePath();
		List<EPackage> metaPackage= EmfUtil.register(org.eclipse.emf.common.util.URI.createFileURI(metamodel), EPackage.Registry.INSTANCE);
		
		//EmfMetaModel meta = new EmfMetaModel();
		//meta.load();
		
		//create a new model
		IModel model= createEmfModel("Model", "student.model","student.ecore", true, true);
		//model.store();
		context.getModelRepository().addModel(model);
		module.setContext(context);
		//EModelElementOperationContributor modelContributor = new EModelElementOperationContributor();
		//contrib.annotate(metamodel);
		//OperationContributor ade;
		//context.getOperationContributorRegistry().add(new CollectionOperationContributor());
		context.getOperationContributorRegistry().add(new OperationContributor(){
			IterableOperationContributor contributor= new IterableOperationContributor();
			@Override
			public boolean contributesTo(Object target) {
				//return target instanceof EClass;
				return true;
			}
			public EClass annotated(EClass clas,String annotation, Map<?,?> details) {
				EAnnotation eAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
				eAnnotation.setSource(annotation);
				clas.getEAnnotations().add(eAnnotation);
				for (Object key : details.keySet()) {
					eAnnotation.getDetails().put(key + "", details.get(key) + "");
				}
				return clas;
			}
			public EClass annotated(String annotation) {
				DynamicEObjectImpl obj = (DynamicEObjectImpl) target;
				EClass clas = obj.eClass();
				System.out.println(obj.eClass());
				EAnnotation eAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
				eAnnotation.setSource(annotation);
				return annotated(clas,annotation, new HashMap<Object, Object>());
				//EClass eModelElement = (EClass) target;
				//System.out.println(target.getClass());
				
				//contributor.setTarget(eModelElement);
				//EAnnotation eAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
				//eAnnotation.setSource(annotation);
				//eModelElement.getEAnnotations().add(eAnnotation);
				
				
			}
			
		});
		//System.out.println(ast.hasChildren());
		
		
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
		String name="";
		for(EPackage po:metaPackage ){
			for(EClassifier clas:po.getEClassifiers() ){
				if(clas instanceof EClass){
					EClass eclass= (EClass) clas;
					if(!(eclass.isAbstract())){
						for (Operation operation: operationNames){
							if(operation.getContextType(context).getName().equals(eclass.getName())){					
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
									//System.out.println(name);
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
											System.out.println(eclass.getEAnnotations().size());
											annotate(eclass,ann);
											annotateRef(eclass,ann);
											System.out.println(eclass.getEAnnotations().size());
										//eclass.
										}
									}
									if(name.equals("annotateRefClass")){
										
										if (!annotationValues.isEmpty()) {
											ann=(String) annotationValues.get(0);
											System.out.println(eclass.getEAnnotations().size());
											//annotate(eclass,ann);
											annotateRefClass(eclass,ann);
											System.out.println(eclass.getEAnnotations().size());
										//eclass.
										}
									}
									
								}
								
								
								for (int i = 0; i<instances; i++) {
									//System.out.println("found create method for: " + eclass.getName()+i);
									System.out.println(eclass.getEAnnotations().size());
									
									Object modelObject = model.createInstance(eclass.getName());
									//System.out.println(po.toString());
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
	protected static EClass annotate(EClass clas, String source){
		EAnnotation eAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
		eAnnotation.setSource(source);
		clas.getEAnnotations().add(eAnnotation);
		//System.out.println("successful "+clas.getEAnnotations().size());
		return clas;
		//annotation.setSource(source);
		//clas.
	}
	protected static void annotateRef(EClass clas, String source){
		int i=0;
		for(EReference refClass: clas.getEAllReferences()){
			//System.out.println(refClass.getEReferenceType());
			System.out.println(refClass.isContainment());
			//if(refClass instanceof EClass){
				EClass refClas= refClass.eClass();
				EAnnotation eAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
				eAnnotation.setSource(source);
				refClas.getEAnnotations().add(eAnnotation);
				i++;
			//}
		}
		System.out.println("successful "+i);
		//clas.
		//annotation.setSource(source);
		//clas.
	}
	protected static void annotateRefClass(EClass clas, String source){
		int i=0;
		for(EReference refClass: clas.getEAllReferences()){
			EClass refClas= refClass.eClass();
			EAnnotation eAnnotation = EcoreFactory.eINSTANCE.createEAnnotation();
			eAnnotation.setSource(source);
			refClas.getEAnnotations().add(eAnnotation);
			
		}
	}
	protected static List<EReference> getReferences(EClass clas){
		int i=0;
		List<EReference> references = Collections.emptyList();
		for(EReference ref: clas.getEAllReferences()){
			if(!(references.contains(ref)))
				references.add(ref);
		}
		return references;
	}
	protected static List<EReference> getContainmentReferences(EClass clas){
		int i=0;
		List<EReference> references = Collections.emptyList();
		for(EReference ref: clas.getEAllReferences()){
			if(!(references.contains(ref)) && ref.isContainment())
				references.add(ref);
		}
		return references;
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
