package org.eclipse.epsilon.modelStynthesis;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.common.parse.AST;
import org.eclipse.epsilon.eol.compile.context.EolCompilationContext;
import org.eclipse.epsilon.eol.dom.AbstractExecutableModuleElement;
import org.eclipse.epsilon.eol.dom.ExecutableBlock;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.ExecutorFactory;
import org.eclipse.epsilon.eol.execute.context.FrameType;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.models.java.JavaModel;
import org.eclipse.epsilon.epl.EplModule;
import org.eclipse.epsilon.epl.dom.Pattern;
import org.eclipse.epsilon.epl.parse.EplParser;

public class CustomEplModule extends EplModule {
	
	public static void main(String[] args) throws Exception {
		
		List<Object> objects = new ArrayList<Object>();
		objects.add("foo");
		objects.add("bar");
		
		List<Class<?>> classes = new ArrayList<Class<?>>();
		classes.add(String.class);
		
		JavaModel model = new JavaModel(objects, classes);
		
		CustomEplModule module = new CustomEplModule();
		module.parse("@negate\npattern P1 s : String { match : false  onmatch { s.println(); } }");
		module.getContext().getModelRepository().addModel(model);
		
		module.getContext().setExecutorFactory(new ExecutorFactory() {
			@Override
			public Object executeAST(AST ast, IEolContext context)
					throws EolRuntimeException {
				
				if (ast != null && ast.getParent() != null && ast.getParent().getType() == EplParser.MATCH) {
					Pattern pattern = (Pattern) ast.getParent().getParent();
					if (pattern.hasAnnotation("negate")) {
						Boolean result = (Boolean) super.executeAST(ast, context);
						return !result;
					}
				}
				
				return super.executeAST(ast, context);
			}
		});
		
		module.execute();
		
	}
	
}
