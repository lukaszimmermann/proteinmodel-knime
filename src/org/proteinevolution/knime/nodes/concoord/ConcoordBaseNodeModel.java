package org.proteinevolution.knime.nodes.concoord;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
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


	// Constants for the dist file
	protected static final String[] namesDist =  new String[] {
			"class", "atom1", "atom2", "dist1", "dist2", "dist3",
			"atom3", "angle", "factor"
	};
	private static final int[] paddingsDIST = new int[] {8, 8, 10, 10, 10, 6, 10, 10};

	protected static File writeDistFile(final DataTable dataTable) throws IOException {

		DataTableSpec dataTableSpec = dataTable.getDataTableSpec();

		// Figure out the indices of the columns in the data table
		int[] indices = new int[namesDist.length];

		for (int i = 0; i < namesDist.length; ++i) {

			indices[i] = dataTableSpec.findColumnIndex(namesDist[i]);
		}
		// Write distFile
		File distFile = Files.createTempFile("concoordDiscoDist", ".dat").toFile();

		try(BufferedWriter bw = new BufferedWriter(new FileWriter(distFile))) {

			String currentClass = null;
			for (DataRow row : dataTable) {

				String rowClass = row.getCell(indices[0]).toString();

				if ( ! rowClass.equals(currentClass)) {

					currentClass = rowClass;

					bw.write("#" + currentClass);
					bw.newLine();
				}

				int bound = "1-3 restricted pairs".equals(currentClass) ? indices.length : 6;
				for (int i = 1; i < bound; ++i) {

					int padding = paddingsDIST[i-1]; // -1, because class is not contained in padding
					String dataCellValue = row.getCell(indices[i]).toString();

					// Format some columns as double (better safe than sorry)
					if ( i == 3 || i == 4 || i == 5 || i == 7 || i == 9) {

						if (dataCellValue.contains(".")) {

							int pointIndex = dataCellValue.lastIndexOf(".");			
							int decimals = dataCellValue.substring(pointIndex + 1).length();

							dataCellValue = decimals <= 4 ? dataCellValue.concat(new String(new char[4 - decimals]).replace('\0', '0')) :
								dataCellValue.substring(0, pointIndex + 5);
						} else {

							dataCellValue = dataCellValue.concat(".0000");
						}
					}
					bw.write(String.format("%1$" + padding + "s", dataCellValue));
				}
				bw.newLine();
			}
		}
		distFile.deleteOnExit();
		return distFile;
	}


	// Constants for the NOE file
	// Names of the NOE file header
	protected static final String[] namesNOE = new String[] {
			"resid1", "resname1", "atomname1", "segid1", "resid2",
			"resname2", "atomname2", "segid2", "lowbound", "uppbound",
			"upppscor", "index", "restrnum"
	};
	private static final int[] paddingsNOE = new int[] {8, 9, 10, 7, 7, 9, 10, 7, 9, 9, 9, 6, 9};

	
	protected static File writeNoeFile(final DataTable dataTable) throws IOException {

		DataTableSpec noesSpec = dataTable.getDataTableSpec();

		// Figure out the indices of the columns in the data table
		int[] indices = new int[namesNOE.length];

		for (int i = 0; i < namesNOE.length; ++i) {

			indices[i] = noesSpec.findColumnIndex(namesNOE[i]);
		}
		File noeFile = Files.createTempFile("concoord_noe", ".noe").toFile();
		try(BufferedWriter br = new BufferedWriter(new FileWriter(noeFile))) {

			// Write Header
			br.write("[ distance_restraints ]");
			br.newLine();
			br.write("[ resid1 resname1 atomname1 segid1 resid2 resname2 atomname2 segid2 lowbound uppbound upppscor index restrnum ]");
			for (DataRow row : dataTable) {

				br.newLine();

				for (int i = 0; i < indices.length; ++i) {

					
					int padding = paddingsNOE[i];
					String dataCellValue = row.getCell(indices[i]).toString();

					// Format some columns as double (better safe than sorry)
					if ( i == 8 || i == 9 || i == 10) {

						if (dataCellValue.contains(".")) {

							int pointIndex = dataCellValue.lastIndexOf(".");			
							int decimals = dataCellValue.substring(pointIndex + 1).length();

							dataCellValue = decimals <= 3 ? dataCellValue.concat(new String(new char[3 - decimals]).replace('\0', '0')) :
								dataCellValue.substring(0, pointIndex + 4);
						} else {

							dataCellValue = dataCellValue.concat(".000");
						}
					}
				
					br.write(String.format("%1$" + padding + "s", dataCellValue));
				
				}
			}
			br.newLine();
		}
		noeFile.deleteOnExit();
		return noeFile;
	}


	
	// Possible values for atomsMargin and bonds
	private static final Map<String, String> atomsMargins;
	private static final List<String> atomsMarginsList;
	private static final Map<String, String> bonds;
	private static final List<String> bondsList;
	static {
		atomsMargins = new LinkedHashMap<String, String>(6);
		atomsMargins.put("OPLS-UA (united atoms)", "oplsua");
		atomsMargins.put("OPLS-AA (all atoms)", "oplsaa");
		atomsMargins.put("PROLSQ repel", "repel");
		atomsMargins.put("Yamber2", "yamber2");
		atomsMargins.put("Li et al.", "li");
		atomsMargins.put("OPLS-X", "oplsx");
		atomsMarginsList = new ArrayList<String>(atomsMargins.keySet());

		bonds = new LinkedHashMap<String, String>(2);
		bonds.put("Concoord default", ".noeh");
		bonds.put("Engh-Huber", "");
		bondsList = new ArrayList<String>(bonds.keySet());
	}

	public static List<String> getAtomsMarginsList() {

		// Provide safe copy
		return new ArrayList<String>(atomsMarginsList);
	}
	public static List<String> getBondsList() {

		// Provide safe copy
		return new ArrayList<String>(bondsList);
	}
	

	protected static File createTempLib(final ExecutionContext exec,  final String atomMarginName, final String bondsName) throws IOException, CanceledExecutionException {

		// Copy lib directory to a temporary location (because some files need to be renamed there)
		File tempLib = Files.createTempDirectory("concoordLib").toFile();
		tempLib.deleteOnExit();	

		FileUtil.copyDir(new File(ProteinevolutionNodePlugin.getDefault().getPreferenceStore().getString(PreferencePage.CONCOORD_PATH),
				"lib"), tempLib);

		String atomMarginSuffix = atomsMargins.get(atomMarginName);
		
		// Copy atomMargin files
		FileUtil.copy(
				new File(tempLib, String.format("ATOMS_%s.DAT", atomMarginSuffix)),
				new File(tempLib, "ATOMS.DAT"), exec); 

		FileUtil.copy(
				new File(tempLib, String.format("MARGINS_%s.DAT",atomMarginSuffix)),
				new File(tempLib, "MARGINS.DAT"), exec); 

		// BONDS File
		FileUtil.copy(
				new File(tempLib, String.format("BONDS.DAT%s", bonds.get(bondsName))), 
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
