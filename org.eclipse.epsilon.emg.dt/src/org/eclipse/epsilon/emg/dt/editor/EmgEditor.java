package org.eclipse.epsilon.emg.dt.editor;

import org.eclipse.epsilon.common.module.IModule;
import org.eclipse.epsilon.emg.EmgModule;
import org.eclipse.epsilon.epl.dt.editor.EplEditor;

public class EmgEditor extends EplEditor {
	@Override
	public IModule createModule(){
		return new EmgModule();
	}
}
