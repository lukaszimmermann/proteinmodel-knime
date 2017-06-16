package org.proteinevolution.knime.nodes.concoord.dist;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
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
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.util.FileUtil;
import org.proteinevolution.knime.nodes.base.ExecutableNodeModel;
import org.proteinevolution.knime.nodes.concoord.ConcoordBaseNodeModel;
import org.proteinevolution.knime.porttypes.structure.StructureContent;
import org.proteinevolution.knime.porttypes.structure.StructurePortObject;
import org.proteinevolution.models.util.CommandLine;


/**
 * This is the model implementation of ConcoordDist.
 * 
 *
 * @author Lukas Zimmermann
 */
public class ConcoordDistNodeModel extends ConcoordBaseNodeModel {

	private static final NodeLogger logger = NodeLogger
			.getLogger(ConcoordDistNodeModel.class);

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



	// Param: Selection of atomsMargin (VdW parameters)
	public static SettingsModelString getParamAtomsMargin() {

		return new SettingsModelString("ATOMS_MARGIN_CFGKEY", "OPLS-UA (united atoms)");
	}
	private final SettingsModelString param_atoms_margin = getParamAtomsMargin();


	// Param: Selection of bonds/angles
	public static SettingsModelString getParamBonds() {

		return new SettingsModelString("BONDS_CFGKEY", "Concoord default");
	}
	private final SettingsModelString param_bonds = getParamBonds();


	// Param: Retain Hydrogen atoms 
	public static SettingsModelBoolean getParamRetainHydrogenAtoms() {

		return new SettingsModelBoolean("RETAIN_HYDROGEN_ATOMS_CFGKEY", false);
	} 
	private final SettingsModelBoolean param_retain_hydrogen_atoms = getParamRetainHydrogenAtoms();


	// Param: Find Alternative contacts
	public static SettingsModelBoolean getParamFindAlternativeContacts() {

		return new SettingsModelBoolean("FIND_ALTERNATIVE_CONTACTS_CFGKEY", false);
	} 
	private final SettingsModelBoolean param_find_alternative_contacts = getParamFindAlternativeContacts();


	// Param: Cut off Radius
	public static SettingsModelDoubleBounded getParamCutOffRadius() {

		return new SettingsModelDoubleBounded("CUT_OFF_RADIUS_CFGKEY", 4, 0, 20);
	}  
	private final SettingsModelDoubleBounded param_cut_off_radius = getParamCutOffRadius();


	// Param: Minimum number of distances to be defined for each atom
	public static SettingsModelIntegerBounded getParamMinDist() {

		return new SettingsModelIntegerBounded("MIN_DIST_CFGKEY", 50, 1, 200);
	}
	private final SettingsModelIntegerBounded param_min_dist = getParamMinDist();

	// Param: Damp
	public static SettingsModelDoubleBounded getParamDamp() {

		return new SettingsModelDoubleBounded("DAMP_CFGKEY", 1, 1, 10);
	}
	private final SettingsModelDoubleBounded param_damp = getParamDamp();


	/**
	 * Constructor for the node model.
	 */
	protected ConcoordDistNodeModel() throws InvalidSettingsException {

		super(new PortType[]   {StructurePortObject.TYPE, BufferedDataTable.TYPE_OPTIONAL},
				new PortType[] {BufferedDataTable.TYPE});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObject[] execute(final PortObject[] inData,
			final ExecutionContext exec) throws Exception {

		// Get Structure
		StructureContent structureContent = ((StructurePortObject) inData[0]).getStructure();

		// Copy lib directory to a temporary location (because some files need to be renamed there)
		File tempLib = Files.createTempDirectory("concoord").toFile();
		FileUtil.copyDir(ConcoordBaseNodeModel.getLibDir(), tempLib);

		// Copy atomMargin files
		FileUtil.copy(
				new File(tempLib, String.format("ATOMS_%s.DAT", atomsMargins.get(this.param_atoms_margin.getStringValue()))),
				new File(tempLib, "ATOMS.DAT"), exec); 

		FileUtil.copy(
				new File(tempLib, String.format("MARGINS_%s.DAT", atomsMargins.get(this.param_atoms_margin.getStringValue()))),
				new File(tempLib, "MARGINS.DAT"), exec); 

		// CONCOORD has no atom types for hetero atoms
		structureContent.setOmitHET(true);

		BufferedDataContainer container = null;
		try(CommandLine cmd = new CommandLine(this.getExecutable())) {

			cmd.addInput("-p", structureContent);
			cmd.addFlag("-r", this.param_retain_hydrogen_atoms.getBooleanValue());
			cmd.addFlag("-nb", this.param_find_alternative_contacts.getBooleanValue());
			cmd.addOption("-c", this.param_cut_off_radius.getDoubleValue());
			cmd.addOption("-m", this.param_min_dist.getIntValue());
			cmd.addOutput("-od", ".dat");
			cmd.addOutput("-op");

			ExecutableNodeModel.exec(cmd.toString(), exec, new String[] {
					String.format("CONCOORDLIB=%s", tempLib.getAbsoluteFile())	
			}, Files.createTempDirectory("concoord_work").toFile());


			// Assemble data table
			// the data table spec of the single output table, 
			// the table will have three columns:
			DataColumnSpec[] allColSpecs = new DataColumnSpec[] {

					new DataColumnSpecCreator("class", StringCell.TYPE).createSpec(),
					new DataColumnSpecCreator("atom1", IntCell.TYPE).createSpec(),
					new DataColumnSpecCreator("atom2", IntCell.TYPE).createSpec(),
					new DataColumnSpecCreator("dist1", DoubleCell.TYPE).createSpec(),
					new DataColumnSpecCreator("dist2", DoubleCell.TYPE).createSpec(),
					new DataColumnSpecCreator("dist3", DoubleCell.TYPE).createSpec(),

					// for 1-3 restrictions
					new DataColumnSpecCreator("atom3", IntCell.TYPE).createSpec(),
					new DataColumnSpecCreator("angle", DoubleCell.TYPE).createSpec(),
					new DataColumnSpecCreator("factor", DoubleCell.TYPE).createSpec(),
			};
			container = exec.createDataContainer(new DataTableSpec(allColSpecs));

			// Parse distance file
			try(BufferedReader br = new BufferedReader(new FileReader(cmd.getFile("-od")))) {

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
		}


		container.close();
		return new BufferedDataTable[]{container.getTable()};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
			throws InvalidSettingsException {

		return new DataTableSpec[]{null};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {

		this.param_retain_hydrogen_atoms.saveSettingsTo(settings);
		this.param_find_alternative_contacts.saveSettingsTo(settings);
		this.param_cut_off_radius.saveSettingsTo(settings);
		this.param_min_dist.saveSettingsTo(settings);
		this.param_atoms_margin.saveSettingsTo(settings);
		this.param_bonds.saveSettingsTo(settings);
		this.param_damp.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		this.param_retain_hydrogen_atoms.loadSettingsFrom(settings);
		this.param_find_alternative_contacts.loadSettingsFrom(settings);
		this.param_cut_off_radius.loadSettingsFrom(settings);
		this.param_min_dist.loadSettingsFrom(settings);
		this.param_atoms_margin.loadSettingsFrom(settings);
		this.param_bonds.loadSettingsFrom(settings);
		this.param_damp.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		this.param_retain_hydrogen_atoms.validateSettings(settings);
		this.param_find_alternative_contacts.validateSettings(settings);
		this.param_cut_off_radius.validateSettings(settings);
		this.param_min_dist.validateSettings(settings);
		this.param_atoms_margin.validateSettings(settings);
		this.param_bonds.validateSettings(settings);
		this.param_damp.validateSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
	CanceledExecutionException {

		// Nothing to be done here
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
	CanceledExecutionException {

		// Nothing to be done here
	}

	@Override
	protected String getExecutableName() {

		return "dist.exe";
	}
}
