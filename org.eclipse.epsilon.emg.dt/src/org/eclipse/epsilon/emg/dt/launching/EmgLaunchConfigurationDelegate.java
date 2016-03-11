/*******************************************************************************
 * Copyright (c) 2012 The University of York.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Dimitrios Kolovos - initial API and implementation
 *     Saheed Popoola - aditional functionality
 *     Horacio Hoyos - aditional functionality
 ******************************************************************************/
package org.eclipse.epsilon.emg.dt.launching;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.epsilon.emg.EmgModule;
import org.eclipse.epsilon.emg.dt.EmgPlugin;
import org.eclipse.epsilon.eol.IEolExecutableModule;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.epl.dt.launching.EplLaunchConfigurationDelegate;

public class EmgLaunchConfigurationDelegate extends EplLaunchConfigurationDelegate {
	
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
		boolean useSeed;
		try {
			useSeed = configuration.getAttribute(EmgLaunchConfigurationAttributes.USE_SEED, false);
		} catch (CoreException ex) {
			useSeed = false;
		}
		emgModule.setUseSeed(useSeed);
		if (useSeed) {
			try {
				emgModule.setSeed(configuration.getAttribute(EmgLaunchConfigurationAttributes.SEED, EmgPlugin.getRandomSeed()));
			} catch (CoreException ex) {
				EolRuntimeException.propagate(ex);
			}
		}
			
	}

}
