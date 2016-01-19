package org.eclipse.epsilon.emg.dt.launching;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.epsilon.emg.EmgModule;
import org.eclipse.epsilon.eol.IEolExecutableModule;
import org.eclipse.epsilon.epl.dt.launching.EplLaunchConfigurationDelegate;

public class EmgLaunchConfigurationDelegate extends EplLaunchConfigurationDelegate{
	
	@Override
	public IEolExecutableModule createModule() {
		return new EmgModule();
	}
	
	@Override
	public void aboutToExecute(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor progressMonitor,
			IEolExecutableModule module) throws Exception {
		
		super.aboutToExecute(configuration, mode, launch, progressMonitor, module);
		EmgModule emgModule = (EmgModule) module;
		emgModule.setSeed(configuration.getAttribute(EmgLaunchConfigurationAttributes.SEED, (int) System.currentTimeMillis()));
	}

}
