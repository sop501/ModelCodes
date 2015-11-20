package org.eclipse.epsilon.emg.dt.launching;

import org.eclipse.epsilon.emg.EmgModule;
import org.eclipse.epsilon.eol.IEolExecutableModule;
import org.eclipse.epsilon.epl.dt.launching.EplLaunchConfigurationDelegate;

public class EmgLaunchConfigurationDelegate extends EplLaunchConfigurationDelegate{
	@Override
		public IEolExecutableModule createModule() {
			return new EmgModule();
		}

}
