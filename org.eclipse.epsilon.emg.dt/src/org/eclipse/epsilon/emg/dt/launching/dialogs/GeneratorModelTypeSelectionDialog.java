package org.eclipse.epsilon.emg.dt.launching.dialogs;

import java.util.ArrayList;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.epsilon.common.dt.launching.dialogs.ModelTypeSelectionDialog;
import org.eclipse.epsilon.common.dt.launching.extensions.ModelTypeExtension;
import org.eclipse.epsilon.common.dt.util.LogUtil;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class GeneratorModelTypeSelectionDialog extends ModelTypeSelectionDialog {

	public GeneratorModelTypeSelectionDialog(Shell parentShell) {
		super(parentShell);
	}
	
	@Override
	protected void findModelTypes() {		
		modelTypes = new ArrayList<ModelTypeExtension>();
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		
		IExtensionPoint extensionPoint = registry.getExtensionPoint("org.eclipse.epsilon.common.dt.modelType");
		IConfigurationElement[] configurationElements =  extensionPoint.getConfigurationElements();
		
		try {
			for (int i=0;i<configurationElements.length; i++){
				IConfigurationElement configurationElement = configurationElements[i];
				// Intercept the EMF model as we want to provide our reduced dialog
				String type = configurationElement.getAttribute("type");				
				ModelTypeExtension modelType = new ModelTypeExtension();
				modelType.setClazz(configurationElement.getAttribute("class"));
				modelType.setType(type);
				modelType.setLabel(configurationElement.getAttribute("label"));
				modelType.setStable(Boolean.parseBoolean(configurationElement.getAttribute("stable")));
	
				String contributingPlugin = configurationElement.getDeclaringExtension().getNamespaceIdentifier();
				Image image = AbstractUIPlugin.imageDescriptorFromPlugin(contributingPlugin,configurationElement.getAttribute("icon")).createImage();
				modelType.setImage(image);
				modelType.setConfigurationElement(configurationElement);
				modelTypes.add(modelType);
			
			}
		}
		catch (Exception ex) {
			LogUtil.log(ex);
		}
		
	}
	
	

}
