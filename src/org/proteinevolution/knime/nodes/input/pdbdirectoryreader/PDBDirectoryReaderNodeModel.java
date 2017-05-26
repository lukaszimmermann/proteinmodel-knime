package org.proteinevolution.knime.nodes.input.pdbdirectoryreader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.MissingCell;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.def.StringCell.StringCellFactory;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;


/**
 * This is the model implementation of PDBDirReader.
 * Reads PDB files from a directory into a table with the corresponding file path.
 *
 * @author Lukas Zimmermann
 */
public class PDBDirectoryReaderNodeModel extends NodeModel {

	// Input Directory
	public static final String INPUT_CFGKEY = "INPUT";
	public static final String INPUT_DEFAULT = "";
	public static final String INPUT_HISTORY = "INPUT_HISTORY";
	private final SettingsModelString input = new SettingsModelString(INPUT_CFGKEY,INPUT_DEFAULT);


	/**
	 * Constructor for the node model.
	 */
	protected PDBDirectoryReaderNodeModel() {

		super(0, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {

		String inputPath = this.input.getStringValue();		
		if (inputPath == null) {
			
			throw new IllegalArgumentException("You have not selected a valid directory!");
		}
		File inputFile = new File(inputPath);
		
		if ( ! inputFile.isDirectory()) {
			
			throw new IllegalArgumentException("You have not selected a valid directory!");
		}
		
		// file name and Path spec
		DataColumnSpec[] allColSpecs = new DataColumnSpec[] {

				new DataColumnSpecCreator("filename", StringCell.TYPE).createSpec(),
				new DataColumnSpecCreator("path", StringCell.TYPE).createSpec(),
				new DataColumnSpecCreator("PDBID", StringCell.TYPE).createSpec(),
		};
		DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
		BufferedDataContainer container = exec.createDataContainer(outputSpec);

		// List content of selected directory        
		File[] content = inputFile.listFiles(new FileFilter() {

			@Override
			public boolean accept(File pathname) {

				String path = pathname.getName();
				return path.endsWith("pdb") || path.endsWith("ent");
			}
		});

		int rowCounter = 0;
		for(File currentFile : content) {
			
			BufferedReader br = new BufferedReader(new FileReader(currentFile));
			String line;
			String pdbid = null;
			while ((line = br.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("HEADER")) { 			
					pdbid = line.substring(62,66);
				} else {

					break;
				}
			}
			br.close();
			br = null;

			container.addRowToTable(
					new DefaultRow(
							"Row" + rowCounter++,
							new DataCell[] {
									StringCellFactory.create(currentFile.getName()),
									StringCellFactory.create(currentFile.getAbsolutePath()),
									(pdbid != null) ? StringCellFactory.create(pdbid) : new MissingCell("PDBID not found")
							}));

		}
		// once we are done, we close the container and return its table
		container.close();
		return new BufferedDataTable[]{container.getTable()};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {

		// Nothing to do here
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
			throws InvalidSettingsException {

		// TODO: check if user settings are available, fit to the incoming
		// table structure, and the incoming types are feasible for the node
		// to execute. If the node can execute in its current state return
		// the spec of its output data table(s) (if you can, otherwise an array
		// with null elements), or throw an exception with a useful user message

		return new DataTableSpec[]{null};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {

		this.input.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		this.input.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		this.input.validateSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
	CanceledExecutionException {

		// Not used here
		// Everything handed to output ports is loaded automatically (data
		// returned by the execute method, models loaded in loadModelContent,
		// and user settings set through loadSettingsFrom - is all taken care 
		// of). Load here only the other internals that need to be restored
		// (e.g. data used by the views).
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
	CanceledExecutionException {

		// Not used here
		// Everything written to output ports is saved automatically (data
		// returned by the execute method, models saved in the saveModelContent,
		// and user settings saved through saveSettingsTo - is all taken care 
		// of). Save here only the other internals that need to be preserved
		// (e.g. data used by the views).
	}
}

