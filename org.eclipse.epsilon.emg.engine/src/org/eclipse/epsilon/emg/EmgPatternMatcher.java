package org.eclipse.epsilon.emg;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import org.eclipse.epsilon.common.parse.AST;
import org.eclipse.epsilon.eol.exceptions.EolIllegalReturnException;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.Return;
import org.eclipse.epsilon.eol.execute.context.FrameType;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.epl.EplModule;
import org.eclipse.epsilon.epl.combinations.CombinationGenerator;
import org.eclipse.epsilon.epl.combinations.CombinationGeneratorListener;
import org.eclipse.epsilon.epl.combinations.CompositeCombinationGenerator;
import org.eclipse.epsilon.epl.combinations.CompositeCombinationValidator;
import org.eclipse.epsilon.epl.dom.NoMatch;
import org.eclipse.epsilon.epl.dom.Pattern;
import org.eclipse.epsilon.epl.dom.Role;
import org.eclipse.epsilon.epl.execute.*;

public class EmgPatternMatcher extends PatternMatcher {
	RandomGenerator random;
	public EmgPatternMatcher(){
		random= new RandomGenerator();
	}
	public EmgPatternMatcher(RandomGenerator rand){
		random=rand;
	}

	@Override
	public List<PatternMatch> match(final Pattern pattern, final IEolContext context) throws Exception {
		//System.out.println("running pattern "+pattern.getName());
		long time= System.currentTimeMillis();
		List<PatternMatch> patternMatches = new ArrayList<PatternMatch>();
		
		context.getFrameStack().enterLocal(FrameType.PROTECTED, pattern);
		boolean noRepeat= pattern.hasAnnotation("noRepeat");
		boolean probability= pattern.hasAnnotation("probability");
		boolean number= pattern.hasAnnotation("number");
		boolean annotationChange;
		int num=0, value=1;
		List<Object> matchList= new ArrayList<Object>();
		CompositeCombinationGenerator<Object> generator = new CompositeCombinationGenerator<Object>();
		
		//Collections.shuffle(generator., random);
		
		//Stack<Object> st;
		//st.
		for (Role role : pattern.getRoles()) {	
			generator.addCombinationGenerator(createCombinationGenerator(role, context));
			//System.out.println("s "+role.getInstances(context).get(0)+role.getInstances(context).get(1) );
		}
		
		//System.out.println("genrator  "+ generator.getNext());
		generator.setValidator(new CompositeCombinationValidator<Object>() {
			
			@Override
			public boolean isValid(List<List<Object>> combination) throws Exception {
				//System.out.println(combination);
				//Stack<List<Object>> t=generator.getStack();
				//System.out.println("t  "+t.toString());
				for (Object o : combination.get(combination.size()-1)) {
					if (o instanceof NoMatch) return true;
				}
				
				frame = context.getFrameStack().enterLocal(FrameType.PROTECTED, pattern);
				boolean result = true;
				int i = 0;
				Role role = null;
				for (List<Object> values : combination) {
					role = pattern.getRoles().get(i);
					for (Variable variable : getVariables(values, role)) {
						frame.put(variable);
					}
					i++;
				}
				if (!role.isNegative() && role.getGuard() != null && role.isActive(context) && role.getCardinality().isOne()) {
					result = role.getGuard().execute(context);
				}
				context.getFrameStack().leaveLocal(pattern);
				return result;
			}
		});
		
		//annotation number 
		if(number){
			List vals=pattern.getAnnotationsValues("number", context);							
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
				}
			}	
		}//end annotation number
		//generator.
		while (generator.hasMore()) {
			
			List<List<Object>> candidate = generator.getNext();
			//System.out.println("df");
			//System.out.println(candidate);
			boolean test=false;
			if(number && value<=num)
				break;
			if(noRepeat){		
				for(Object temp:candidate){
					if(matchList.contains(temp)){
						test=true;
						break;
					}
				}	
				if (test){
					continue;
				}
			}//end annotation noRepeat
						
		
			boolean matches = true;
			annotationChange=true;
			
			frame = context.getFrameStack().enterLocal(FrameType.PROTECTED, pattern);
			
			if (pattern.getMatchAst() != null || pattern.getNoMatchAst() != null || pattern.getOnMatchAst() != null) {
				int i = 0;
				for (Role role : pattern.getRoles()) {
					for (Variable variable : getVariables(candidate.get(i), role)) {
						frame.put(variable);
					}
					i++;
				}
			}
			
			if (pattern.getMatchAst() != null) {
				Object result = context.getExecutorFactory().executeAST(pattern.getMatchAst(), context);
				if (result instanceof Return) result = ((Return) result).getValue();
				if (result instanceof Boolean) {
					matches = (Boolean) result;
				}
				else throw new EolIllegalReturnException("Boolean", result, pattern.getMatchAst(), context);
			}
			
			if (matches) { 
				if(noRepeat){
					matchList.addAll(candidate);
							
				}//end annotation noRepeat		
				//annotation number
				if(number){	
					num++;		
				}//end annotation number
				
				//annotation probability
				if (probability) {
					Object val=1;
					if(pattern.getAnnotationsValues("probability", context).size()>0)
						val= pattern.getAnnotationsValues("probability", context).get(0);
					float value2=1;
					if((!val.equals(null))){
						value2=getFloat(val);
					}
					
					if(!random.generateBoolean(value2)){
						annotationChange=false;
					}
			
				}//end annotation probability
				if(annotationChange){
					context.getExecutorFactory().executeAST(pattern.getOnMatchAst(), context);
					patternMatches.add(createPatternMatch(pattern, candidate));
				}
				else context.getFrameStack().leaveLocal(pattern);
			}
			else context.getExecutorFactory().executeAST(pattern.getNoMatchAst(), context);	
		}
		
		context.getFrameStack().leaveLocal(pattern);
		//System.out.println("time is: "+(System.currentTimeMillis()-time));
		return patternMatches;
	}
	protected int getInt(Object object){
		if(object instanceof Integer)
			return (int)object;
		else
			return Integer.parseInt((String) object);	
	}
	protected float getFloat(Object object){
		if(object instanceof Float || object instanceof Integer)
			return (float)object;
		else
			return Float.parseFloat((String) object);	
	}
}
