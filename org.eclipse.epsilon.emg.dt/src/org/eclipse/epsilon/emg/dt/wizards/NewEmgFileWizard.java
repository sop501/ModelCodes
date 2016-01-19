package org.eclipse.epsilon.emg.dt.wizards;

import org.eclipse.epsilon.common.dt.wizards.AbstractNewFileWizard2;
public class NewEmgFileWizard extends AbstractNewFileWizard2{

	@Override
	public String getTitle() {
		return "New EMG Program";
	}

	@Override
	public String getExtension() {
		return "emg";
	}

	@Override
	public String getDescription() {
		return "This wizard creates a new EMG program file with *.emg extension";
	}

}
