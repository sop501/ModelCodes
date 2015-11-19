package org.eclipse.epsilon.emg.dt.launching;

import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.epsilon.emg.dt.launching.tabs.EmgSourceConfigurationTab;
import org.eclipse.epsilon.epl.dt.launching.EplLaunchConfigurationTabGroup;

public class EmgLaunchConfigurationTabGroup extends
		EplLaunchConfigurationTabGroup {
	@Override
	public ILaunchConfigurationTab getSourceConfigurationTab() {
		return new EmgSourceConfigurationTab();
	}
}
