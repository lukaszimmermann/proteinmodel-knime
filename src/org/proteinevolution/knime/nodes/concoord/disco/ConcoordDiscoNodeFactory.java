package org.proteinevolution.knime.nodes.concoord.disco;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Paths;

import org.knime.core.data.DataRow;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortType;
import org.proteinevolution.ProteinevolutionNodePlugin;
import org.proteinevolution.externaltools.tools.ConcoordDisco;
import org.proteinevolution.externaltools.tools.ExternalToolInvocation;
import org.proteinevolution.knime.KNIMEAdapter;
import org.proteinevolution.knime.nodes.ToolInvocationNodeFactory;
import org.proteinevolution.knime.porttypes.structure.StructureContent;
import org.proteinevolution.knime.porttypes.structure.StructurePortObject;
import org.proteinevolution.knime.porttypes.structure.StructurePortObjectSpec;
import org.proteinevolution.models.interfaces.Writeable;
import org.proteinevolution.preferences.PreferencePage;

/**
 * <code>NodeFactory</code> for the "ConcoordDisco" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public final class ConcoordDiscoNodeFactory extends ToolInvocationNodeFactory<Writeable[], File[]> {


	// Class for writing out the NOE file
	private static final class DistWriter implements Writeable {

		private final DataTable dataTable;

		public DistWriter(final DataTable dataTable) {

			this.dataTable = dataTable;
		}

		@Override
		public void write(final Writer out) throws IOException {

			DataTableSpec dataTableSpec = dataTable.getDataTableSpec();

			// Figure out the indices of the columns in the data table
			int[] indices = new int[namesDist.length];

			for (int i = 0; i < namesDist.length; ++i) {

				indices[i] = dataTableSpec.findColumnIndex(namesDist[i]);
			}

			try(BufferedWriter bw = new BufferedWriter(out)) {

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
		}
	}


	// Constants for the dist file
	protected static final String[] namesDist =  new String[] {
			"class", "atom1", "atom2", "dist1", "dist2", "dist3",
			"atom3", "angle", "factor"
	};
	private static final int[] paddingsDIST = new int[] {8, 8, 10, 10, 10, 6, 10, 10};




	@Override
	protected ExternalToolInvocation<Writeable[], File[]> initTool() {

		try{
			return new ConcoordDisco(Paths.get(
					ProteinevolutionNodePlugin.getDefault().getPreferenceStore().getString(PreferencePage.CONCOORD_PATH)));
		} catch(IOException e) {

			throw new IllegalStateException(e.getMessage());
		}
	}

	@Override
	protected KNIMEAdapter<Writeable[], File[]> getAdapter() {

		return new KNIMEAdapter<Writeable[], File[]>() {

			@Override
			public Writeable[] portToInput(PortObject[] ports) {
				
				Writeable[] result = new Writeable[2];
				result[0] = ((StructurePortObject) ports[0]).getStructureContent();
				result[1] = ports[1] == null ? null : new DistWriter((BufferedDataTable) ports[1]);
				return result;				
			}

			@Override
			public PortObject[] outputToPort(File[] result, ExecutionContext exec) throws IOException {
				
				// Assuming that the prefix is still part of the filename
				StructureContent structureContent = StructureContent.fromDirectory(result[0].getParent());

				return new StructurePortObject[] {
						new StructurePortObject(
								structureContent,
								new StructurePortObjectSpec(StructureContent.TYPE,
										structureContent.getNumberOfStructures()))
				};
			}

			@Override
			public PortType[] getInputPortType() {
				
				return new PortType[]   {StructurePortObject.TYPE, BufferedDataTable.TYPE};
			}

			@Override
			public PortType[] getOutputPortType() {
				
				return new PortType[] {StructurePortObject.TYPE};
			}
		};
	}

	@Override
	protected void check() throws IllegalStateException {

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

				throw new IllegalStateException("File " + currentFile + " does not exist!");
			}
		}	
	}
}

