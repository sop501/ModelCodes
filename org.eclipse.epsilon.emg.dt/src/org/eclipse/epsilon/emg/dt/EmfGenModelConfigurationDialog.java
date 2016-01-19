package org.eclipse.epsilon.emg.dt;

import org.eclipse.emf.common.util.URI;
import org.eclipse.epsilon.common.util.StringProperties;
import org.eclipse.epsilon.emc.emf.EmfModel;
import org.eclipse.epsilon.emc.emf.dt.EmfModelConfigurationDialog;
import org.eclipse.swt.widgets.Composite;

public class EmfGenModelConfigurationDialog extends EmfModelConfigurationDialog {
	
	private static final String MODEL_TYPE = "EMF_GEN";
	private static final Object DEFAULT_MODEL_NAME = "Generator Model";

	@Override
	protected void createGroups(Composite control) {
		//super.createGroups(control);
		createEmfGroup(control);
		createFilesGroup(control);
		//createLoadStoreOptionsGroup(control);
	}
	
	
	@Override
	protected String getModelType() {
		return MODEL_TYPE;
	}
	
	@Override
	protected void loadProperties(){
		//super.loadProperties();
		if (properties == null) return;

		metamodels.clear();

		// Restore values from legacy launch configuration
		modelFileText.setText(properties.getProperty(PROPERTY_MODEL_FILE));
		final String sLegacyFileMetamodels = properties.getProperty(PROPERTY_METAMODEL_FILE);
		for (String sPath : sLegacyFileMetamodels.trim().split("\\s*,\\s*")) {
			if (sPath.length() > 0) {
				metamodels.add(sPath);
			}
		}

		// Restore values that are used directly to construct an instance of EmfModel
		final String sURIMetamodels = properties.getProperty(EmfModel.PROPERTY_METAMODEL_URI);
		for (String sURI : sURIMetamodels.trim().split("\\s*,\\s*")) {
			if (sURI.length() > 0) {
				metamodels.add(URI.createURI(sURI));
			}
		}
		expandButton.setSelection(new Boolean(properties.getProperty(EmfModel.PROPERTY_EXPAND)).booleanValue());

		metamodelList.refresh();
	}
	
	@Override
	protected void storeProperties(){
		//super.storeProperties();  We only need the metamodel info
		properties = new StringProperties();
		properties.put("type", getModelType());
		properties.put("name", DEFAULT_MODEL_NAME);
		/*
		 * Compute comma-separated lists with the file paths and URIs. If we
		 * only have one metamodel (either file- or URI-based), it should be
		 * compatible with previous versions of Epsilon.
		 */
		final StringBuilder sbFileMetamodels = new StringBuilder();
		final StringBuilder sbFileMetamodelURIs = new StringBuilder();
		final StringBuilder sbURIMetamodels = new StringBuilder();
		boolean bFirstFileMetamodel = true, bFirstURIMetamodel = true;
		for (Object o : metamodels) {
			if (o instanceof String) {
				if (!bFirstFileMetamodel) {
					sbFileMetamodelURIs.append(',');
					sbFileMetamodels.append(',');
				}
				else {
					bFirstFileMetamodel = false;
				}
				sbFileMetamodels.append((String)o);
				sbFileMetamodelURIs.append(createFullyQualifiedUri((String)o));
			}
			else if (o instanceof URI) {
				if (!bFirstURIMetamodel) {
					sbURIMetamodels.append(',');
				}
				else {
					bFirstURIMetamodel = false;
				}
				sbURIMetamodels.append(o.toString());
			}
		}
		// ReadOnLoad = False and StoreOnDisposal = true
		properties.put("readOnLoad", false);
		properties.put("storeOnDisposal", true);
		properties.put(PROPERTY_MODEL_FILE,     modelFileText.getText());
		properties.put(PROPERTY_METAMODEL_FILE, sbFileMetamodels.toString());

		// Persist values that are used directly to construct an instance of EmfModel (legacy - only one metamodel was supported)
		properties.put(EmfModel.PROPERTY_METAMODEL_URI, sbURIMetamodels.toString());
		properties.put(EmfModel.PROPERTY_EXPAND, expandButton.getSelection() + "");
		properties.put(PROPERTY_IS_METAMODEL_FILE_BASED, "".equals(sbURIMetamodels.toString()));

		// Create and persist URI values that are needed to construct an instance of EmfModel
		properties.put(EmfModel.PROPERTY_FILE_BASED_METAMODEL_URI, sbFileMetamodelURIs.toString());
		properties.put(EmfModel.PROPERTY_REUSE_UNMODIFIED_FILE_BASED_METAMODELS, reuseUnmodifiedFileBasedMetamodelsButton.getSelection() + "");
	}
	
}