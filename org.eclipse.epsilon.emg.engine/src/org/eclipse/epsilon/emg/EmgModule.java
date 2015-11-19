package org.eclipse.epsilon.emg;

import org.eclipse.epsilon.common.module.IModule;
import org.eclipse.epsilon.eol.IEolExecutableModule;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.epl.EplModule;
import org.eclipse.epsilon.epl.execute.PatternMatchModel;

public class EmgModule extends EplModule implements IModule, IEolExecutableModule {
	@Override
	public Object execute() throws EolRuntimeException {
		
			prepareContext(context);
			execute(getPre(), context);
			
			EmgPatternMatcher patternMatcher = new EmgPatternMatcher();
			PatternMatchModel matchModel = null;
			try {
				int loops = 1;
				matchModel = patternMatcher.match(this);
				if (repeatWhileMatchesFound) {
					
					while (!matchModel.allContents().isEmpty()) {
						if (maxLoops != INFINITE) {
							if (loops == maxLoops) break;
						}
						matchModel = patternMatcher.match(this);
						loops++;
					}
				}
			}
			catch (Exception ex) {
				EolRuntimeException.propagate(ex);
			}	
			execute(getPost(), context);	
			return matchModel;
		
	}
}
