package org.eclipse.epsilon.emg.dt.launching.tabs;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.epsilon.common.dt.EpsilonPlugin;
import org.eclipse.epsilon.common.dt.util.LogUtil;
import org.eclipse.epsilon.emg.dt.EmgPlugin;
import org.eclipse.epsilon.emg.dt.launching.EmgLaunchConfigurationAttributes;
import org.eclipse.epsilon.epl.dt.launching.tabs.EplSourceConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class EmgSourceConfigurationTab extends EplSourceConfigurationTab {
	
	//private Label genModelNameLabel;
	//private Text genModelName;
	private Label seedLabel;
	private Text seedValue;


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
	
	public String getTitle() {
		return "Source";
	}
	
	
	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		seedLabel = new Label(extras, SWT.NONE);
		seedLabel.setText(getSeedLabel());
		GridData seedData = new GridData(GridData.FILL_HORIZONTAL);
		seedValue = new Text(extras, SWT.BORDER);
		seedValue.setLayoutData(seedData);
		seedValue.addModifyListener(this);
		
	}
	
	private String getSeedLabel() {
		return "Seed Value";
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		super.performApply(configuration);
		configuration.setAttribute(EmgLaunchConfigurationAttributes.SEED, Integer.parseInt(seedValue.getText()));
	}
	
	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		
		super.initializeFrom(configuration);
		try {
			seedValue.setText(String.valueOf(configuration.getAttribute(EmgLaunchConfigurationAttributes.SEED, (int) System.currentTimeMillis())));
		}
		catch (Exception ex) {
			LogUtil.log(ex);
			maxLoopsText.setText(String.valueOf(EmgLaunchConfigurationAttributes.DEFAULT_SEED));
		}
	}
	
}
