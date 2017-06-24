package org.proteinevolution.knime.nodes.concoord.dist;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Paths;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTable;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.MissingCell;
import org.knime.core.data.RowKey;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.DoubleCell.DoubleCellFactory;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.IntCell.IntCellFactory;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.def.StringCell.StringCellFactory;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortType;
import org.proteinevolution.ProteinevolutionNodePlugin;
import org.proteinevolution.externaltools.tools.ConcoordDist;
import org.proteinevolution.externaltools.tools.ExternalToolInvocation;
import org.proteinevolution.knime.KNIMEAdapter;
import org.proteinevolution.knime.nodes.ToolInvocationNodeFactory;
import org.proteinevolution.knime.porttypes.structure.StructureContent;
import org.proteinevolution.knime.porttypes.structure.StructurePortObject;
import org.proteinevolution.knime.porttypes.structure.StructurePortObjectSpec;
import org.proteinevolution.models.interfaces.Writeable;
import org.proteinevolution.preferences.PreferencePage;

/**
 * <code>NodeFactory</code> for the "ConcoordDist" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class ConcoordDistNodeFactory extends ToolInvocationNodeFactory<Writeable[], File[]> {

	// Class for writing out the NOE file
	private static final class NOEWriter implements Writeable {

		private final DataTable dataTable;

		public NOEWriter(final DataTable dataTable) {

			this.dataTable = dataTable;
		}

		@Override
		public void write(final Writer out) throws IOException {

			DataTableSpec noesSpec = this.dataTable.getDataTableSpec();

			// Figure out the indices of the columns in the data table
			int[] indices = new int[namesNOE.length];
			for (int i = 0; i < namesNOE.length; ++i) {

				indices[i] = noesSpec.findColumnIndex(namesNOE[i]);
			}
			try(BufferedWriter br = new BufferedWriter(out)) {

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
		}
	}

	@Override
	protected ExternalToolInvocation<Writeable[], File[]> initTool() throws IllegalStateException {

		try{
			return new ConcoordDist(Paths.get(
					ProteinevolutionNodePlugin.getDefault().getPreferenceStore().getString(PreferencePage.CONCOORD_PATH)));
		} catch(IOException e) {

			throw new IllegalStateException(e.getMessage());
		}
	}

	// Constants for the dist file
	protected static final String[] namesDist =  new String[] {
			"class", "atom1", "atom2", "dist1", "dist2", "dist3",
			"atom3", "angle", "factor"
	};


	// Constants for the NOE file
	// Names of the NOE file header
	protected static final String[] namesNOE = new String[] {
			"resid1", "resname1", "atomname1", "segid1", "resid2",
			"resname2", "atomname2", "segid2", "lowbound", "uppbound",
			"upppscor", "index", "restrnum"
	};
	private static final int[] paddingsNOE = new int[] {8, 9, 10, 7, 7, 9, 10, 7, 9, 9, 9, 6, 9};



	@Override
	protected KNIMEAdapter<Writeable[], File[]> getAdapter() {

		return new KNIMEAdapter<Writeable[], File[]>() {

			@Override
			public Writeable[] portToInput(final PortObject[] ports) {

				Writeable[] result = new Writeable[2];
				StructureContent structureContent = ((StructurePortObject) ports[0]).getStructureContent();
				structureContent.setOmitHET(true);
				result[0] = structureContent;
				result[1] = ports[1] == null ? null : new NOEWriter((BufferedDataTable) ports[1]);
				return result;
			}

			@Override
			public PortObject[] resultToPort(final File[] result, final ExecutionContext exec) throws IOException {

				StructureContent structureContentResult = StructureContent.fromFile(result[0].getAbsolutePath());

				// Assemble data table
				BufferedDataContainer container = exec.createDataContainer(new DataTableSpec(new DataColumnSpec[] {

						new DataColumnSpecCreator("class", StringCell.TYPE).createSpec(),
						new DataColumnSpecCreator("atom1", IntCell.TYPE).createSpec(),
						new DataColumnSpecCreator("atom2", IntCell.TYPE).createSpec(),
						new DataColumnSpecCreator("dist1", DoubleCell.TYPE).createSpec(),
						new DataColumnSpecCreator("dist2", DoubleCell.TYPE).createSpec(),
						new DataColumnSpecCreator("dist3", DoubleCell.TYPE).createSpec(),

						// for 1-3 restrictions
						new DataColumnSpecCreator("atom3", IntCell.TYPE).createSpec(),
						new DataColumnSpecCreator("angle", DoubleCell.TYPE).createSpec(),
						new DataColumnSpecCreator("factor", DoubleCell.TYPE).createSpec() 
				}));

				try(BufferedReader br = new BufferedReader(new FileReader(result[1]))) {

					String currentClass = null;

					int rowCounter = 0;
					String line;
					while ((line = br.readLine()) != null) {

						line = line.trim();

						if (line.equals("")) {

							continue;
						}

						// New class of constraints encountered
						if (line.startsWith("#")) {

							currentClass = line.substring(1);

						} else {

							String[] spt = line.split("\\s+");
							DataCell[] cells = new DataCell[9];

							cells[0] = StringCellFactory.create(currentClass);
							cells[1] = IntCellFactory.create(spt[0]);
							cells[2] = IntCellFactory.create(spt[1]);
							cells[3] = DoubleCellFactory.create(spt[2]);
							cells[4] = DoubleCellFactory.create(spt[3]);
							cells[5] = DoubleCellFactory.create(spt[4]);

							if (spt.length > 5) {

								cells[6] = IntCellFactory.create(spt[5]);
								cells[7] = DoubleCellFactory.create(spt[6]);
								cells[8] = DoubleCellFactory.create(spt[7]);

							} else {

								cells[6] = new MissingCell("Not included");
								cells[7] = new MissingCell("Not included");
								cells[8] = new MissingCell("Not included");
							}
							container.addRowToTable(new DefaultRow(new RowKey("Row"+rowCounter++), cells));
						}
					}
				}
				container.close();
				return new PortObject[]{
						new StructurePortObject(
								structureContentResult,
								new StructurePortObjectSpec(StructureContent.TYPE, 1)),
						container.getTable()};
			}

			@Override
			public PortType[] getInputPortType() {

				return new PortType[] {StructurePortObject.TYPE, BufferedDataTable.TYPE_OPTIONAL};
			}

			@Override
			public PortType[] getOutputPortType() {

				return new PortType[] {StructurePortObject.TYPE, BufferedDataTable.TYPE };
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
