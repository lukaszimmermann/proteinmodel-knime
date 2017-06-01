package org.proteinevolution.preferences;


import org.eclipse.jface.preference.DirectoryFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.proteinevolution.ProteinevolutionNodePlugin;

public final class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public static final String BLAST_EXECUTABLE_PATH = "BLAST_EXECUTABLE_PATH";
	public static final String HHSUITE_EXECUTABLE_PATH = "HHSUITE_EXECUTABLE_PATH";
	public static final String PSIPRED_EXECUTABLE_PATH = "PSIPRED_EXECUTABLE_PATH";
	public static final String ROSETTA_FRAGMENTPICKER_EXECUTABLE = "ROSETTA_FRAGMENTPICKER_EXECUTABLE";
	

	@Override
	public void init(final IWorkbench workbench) {

		this.setPreferenceStore(ProteinevolutionNodePlugin.getDefault().getPreferenceStore());	
	}

	@Override
	protected void createFieldEditors() {
		
		Composite parent = this.getFieldEditorParent();
		
		this.addField(new DirectoryFieldEditor(
				BLAST_EXECUTABLE_PATH,
				"BLAST+ Executable Path",			// Where the BLAST+ executables are located
				parent));

		this.addField(new DirectoryFieldEditor(
				HHSUITE_EXECUTABLE_PATH,
				"HH-Suite Executable Path",			// Where the HH-suite executables are located
				parent));

		this.addField(new DirectoryFieldEditor(
				PSIPRED_EXECUTABLE_PATH,
				"PSIPRED Executable Path",			// Where the PSIPRED executables are located
				parent));
		
		this.addField(new FileFieldEditor(
				ROSETTA_FRAGMENTPICKER_EXECUTABLE,
				"ROSETTA Fragmentpicker Executable", // Location of the Fragmentpicker executable from ROSETTA
				true,   // File path to executable must be absolute
				parent));
	}
}
