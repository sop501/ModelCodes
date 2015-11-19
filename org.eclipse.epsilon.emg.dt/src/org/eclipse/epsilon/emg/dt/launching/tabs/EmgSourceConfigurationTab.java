package org.eclipse.epsilon.emg.dt.launching.tabs;

import org.eclipse.epsilon.common.dt.EpsilonPlugin;
import org.eclipse.epsilon.emg.dt.EmgPlugin;
import org.eclipse.epsilon.epl.dt.launching.tabs.EplSourceConfigurationTab;

public class EmgSourceConfigurationTab extends EplSourceConfigurationTab {
	@Override
	public EpsilonPlugin getPlugin() {
		return EmgPlugin.getDefault();
	}
	@Override
	public String getFileExtension() {
		return "emg";
	}

	@Override
	public String getSelectionTitle() {
		return "Select an EMG Program";
	}

	@Override
	public String getSelectionSubtitle() {
		return "EMG Programs in Workspace";
	}

	@Override
	public String getLaunchConfigurationKey() {
		return "SOURCE.EMG";
	}
	
}
