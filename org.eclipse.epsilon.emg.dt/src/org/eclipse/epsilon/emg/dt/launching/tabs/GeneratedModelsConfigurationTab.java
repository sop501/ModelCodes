package org.eclipse.epsilon.emg.dt.launching.tabs;

import org.eclipse.epsilon.common.dt.launching.dialogs.ModelTypeSelectionDialog;
import org.eclipse.epsilon.common.dt.launching.tabs.ModelsConfigurationTab;
import org.eclipse.epsilon.emg.dt.launching.dialogs.GeneratorModelTypeSelectionDialog;

public class GeneratedModelsConfigurationTab extends ModelsConfigurationTab {
	
	
	protected ModelTypeSelectionDialog getModelTypeSelectionDialog() {
		return new GeneratorModelTypeSelectionDialog(getShell());
	}
	
	

}
