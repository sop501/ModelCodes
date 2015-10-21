package org.eclipse.epsilon.modelStynthesis;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.eclipse.epsilon.eol.exceptions.EolIllegalReturnException;
import org.eclipse.epsilon.eol.execute.Return;
import org.eclipse.epsilon.eol.execute.context.FrameType;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.epl.combinations.CompositeCombinationGenerator;
import org.eclipse.epsilon.epl.combinations.CompositeCombinationValidator;
import org.eclipse.epsilon.epl.dom.NoMatch;
import org.eclipse.epsilon.epl.dom.Pattern;
import org.eclipse.epsilon.epl.dom.Role;
import org.eclipse.epsilon.epl.execute.*;

public class CustomPatternMatcher extends PatternMatcher {
	RandomGenerator random;
	public CustomPatternMatcher(){
		random= new RandomGenerator();
	}
	public CustomPatternMatcher(RandomGenerator rand){
		random=rand;
	}
	@Override
public List<PatternMatch> match(final Pattern pattern, final IEolContext context) throws Exception {
		List<PatternMatch> patternMatches = new ArrayList<PatternMatch>();
		
		context.getFrameStack().enterLocal(FrameType.PROTECTED, pattern);
		boolean noRepeat= pattern.hasAnnotation("noRepeat");
		boolean probability= pattern.hasAnnotation("noRepeat");
		boolean number= pattern.hasAnnotation("number");
		boolean annotationChange;
		int num, value;
		List<Object> matchList= new ArrayList<Object>();
		CompositeCombinationGenerator<Object> generator = new CompositeCombinationGenerator<Object>();
		
		for (Role role : pattern.getRoles()) {
			generator.addCombinationGenerator(createCombinationGenerator(role, context));
		}

		generator.setValidator(new CompositeCombinationValidator<Object>() {
			
			@Override
			public boolean isValid(List<List<Object>> combination) throws Exception {
				
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
		
		while (generator.hasMore()) {
			
			List<List<Object>> candidate = generator.getNext();
		
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
					boolean test=false;
					for(Object temp:candidate){
						if(matchList.contains(temp)){
							test=true;
							break;
						}
					}
					if (test){
						annotationChange=false;
					}
					else{
						matchList.addAll(candidate);
						
					}				
				}//end annotation noRepeat
				
				//annotation number
				if(number){
					num=0;						
					value=1;
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
					if(value>num){
						num++;
					}
					else  annotationChange=false;//context.getFrameStack().leaveLocal(pattern);			
				}//end annotation number
				
				//annotation probability
				if (probability) {
					Object val=1;
					if(pattern.getAnnotationsValues("probability", context).size()>0)
						val= pattern.getAnnotationsValues("probability", context).get(0);
					float value2=1;
					if((!val.equals(null))){
						if((val instanceof Float)) value2=(float) val;
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
			context.getFrameStack().leaveLocal(pattern);
			
		}
		
		context.getFrameStack().leaveLocal(pattern);
		
		return patternMatches;
	}
	
	private int getInt(Object object){
		if(object instanceof Integer)
			return (int)object;
		else
			return Integer.parseInt((String) object);	
	}
}
