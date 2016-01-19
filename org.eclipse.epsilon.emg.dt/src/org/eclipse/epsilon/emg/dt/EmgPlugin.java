package org.eclipse.epsilon.emg.dt;

import org.eclipse.epsilon.epl.dt.EplPlugin;

public class EmgPlugin extends EplPlugin {
	
	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.epsilon.emg.dt";
		
	public static EmgPlugin getDefault() {
	     return (EmgPlugin) plugins.get(EmgPlugin.class);
	}
	
}
