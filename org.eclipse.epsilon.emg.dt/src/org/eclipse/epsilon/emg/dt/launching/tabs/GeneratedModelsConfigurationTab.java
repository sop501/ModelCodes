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
package org.eclipse.epsilon.emg.dt.launching.tabs;

import org.eclipse.epsilon.common.dt.launching.dialogs.ModelTypeSelectionDialog;
import org.eclipse.epsilon.common.dt.launching.tabs.ModelsConfigurationTab;
import org.eclipse.epsilon.emg.dt.launching.dialogs.GeneratorModelTypeSelectionDialog;

public class GeneratedModelsConfigurationTab extends ModelsConfigurationTab {
	
	
	protected ModelTypeSelectionDialog getModelTypeSelectionDialog() {
		return new GeneratorModelTypeSelectionDialog(getShell());
	}
	
	

}
