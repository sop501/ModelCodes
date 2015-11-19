package org.eclipse.epsilon.emg.dt;

import org.eclipse.epsilon.epl.dt.EplPlugin;

public class EmgPlugin extends EplPlugin {
	public static EplPlugin getDefault() {
	     return (EmgPlugin) plugins.get(EmgPlugin.class);
	}
}
