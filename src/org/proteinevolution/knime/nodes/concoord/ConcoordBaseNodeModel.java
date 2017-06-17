package org.proteinevolution.knime.nodes.concoord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.port.PortType;
import org.knime.core.util.FileUtil;
import org.proteinevolution.ProteinevolutionNodePlugin;
import org.proteinevolution.knime.nodes.base.ExecutableNodeModel;
import org.proteinevolution.preferences.PreferencePage;

public abstract class ConcoordBaseNodeModel extends ExecutableNodeModel {

	protected ConcoordBaseNodeModel(int nrInDataPorts, int nrOutDataPorts) throws InvalidSettingsException {

		super(nrInDataPorts, nrOutDataPorts);
	}

	protected ConcoordBaseNodeModel(PortType[] inPortTypes, PortType[] outPortTypes) throws InvalidSettingsException {

		super(inPortTypes, outPortTypes);
	}


	protected static File createTempLib(final ExecutionContext exec,  final String atomMarginSuffix, final String bondsSuffix) throws IOException, CanceledExecutionException {

		// Copy lib directory to a temporary location (because some files need to be renamed there)
		File tempLib = Files.createTempDirectory("concoordLib").toFile();
		tempLib.deleteOnExit();	
		
		FileUtil.copyDir(new File(ProteinevolutionNodePlugin.getDefault().getPreferenceStore().getString(PreferencePage.CONCOORD_PATH),
				"lib"), tempLib);

		// Copy atomMargin files
		FileUtil.copy(
				new File(tempLib, String.format("ATOMS_%s.DAT", atomMarginSuffix)),
				new File(tempLib, "ATOMS.DAT"), exec); 

		FileUtil.copy(
				new File(tempLib, String.format("MARGINS_%s.DAT",atomMarginSuffix)),
				new File(tempLib, "MARGINS.DAT"), exec); 
	
		// BONDS File
		FileUtil.copy(
				new File(tempLib, String.format("BONDS.DAT%s", bondsSuffix)), 
				new File(tempLib, "BONDS.DAT"), exec);
		
		return tempLib;
	}

	@Override
	protected final File getExecutable() {

		return new File(
				new File(
						ProteinevolutionNodePlugin.getDefault().getPreferenceStore().getString(PreferencePage.CONCOORD_PATH),
						"bin"),
				this.getExecutableName());
	}

	@Override
	protected void check() throws InvalidSettingsException {

		// Ensure that the lib directory with all the required files are present
		File libdir = new File(ProteinevolutionNodePlugin.getDefault().getPreferenceStore().getString(PreferencePage.CONCOORD_PATH),
				"lib");

		String[] files = new String[] {

				"AA.DAT", "ATOMS_oplsx.DAT", "BB.DAT", "CHIRAL.DAT", "MARGINS_oplsua.DAT", "PLANAR.DAT", "RING.DAT",
				"ATOMS_li.DAT", "ATOMS_repel.DAT", "BONDS.DAT", "HBONDS.DAT", "MARGINS_oplsx.DAT", "PSEUDO.DAT",
				"ATOMS_oplsaa.DAT", "ATOMS_yamber2.DAT", "BONDS.DAT.noeh", "MARGINS_li.DAT", "MARGINS_repel.DAT", "RENAME_ATOM.DAT",
				"ATOMS_oplsua.DAT", "ATOMS_yamber3.DAT", "CHARGE.DAT", "MARGINS_oplsaa.DAT", "MARGINS_yamber2.DAT", "RESIDUES.DAT"
		};

		for (String f : files) {

			File currentFile = new File(libdir, f);

			if ( ! currentFile.exists()) {

				throw new InvalidSettingsException("File " + currentFile + " does not exist!");
			}
		}
	}
}
