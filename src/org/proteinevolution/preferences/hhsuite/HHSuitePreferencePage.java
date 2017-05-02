package org.proteinevolution.preferences.hhsuite;

import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.proteinevolution.nodes.ProteinevolutionNodePlugin;

public class HHSuitePreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String HHSUITE_EXECUTABLE_PATH = "HHSUITE_EXECUTABLE_PATH";
	
	
	@Override
	protected void createFieldEditors() {

		this.addField(new DirectoryFieldEditor(
				HHSUITE_EXECUTABLE_PATH,
				"HH-Suite Binary Path",			// Where the HH-suite binaries are located
				this.getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

		// Preference store is the default one from the Node Plugin
		this.setPreferenceStore(ProteinevolutionNodePlugin.getDefault().getPreferenceStore());
	}
}
