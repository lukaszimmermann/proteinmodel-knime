package org.proteinevolution.nodes.hhsuite;

import java.io.File;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.def.DoubleCell.DoubleCellFactory;
import org.knime.core.data.def.IntCell.IntCellFactory;
import org.knime.core.data.def.StringCell.StringCellFactory;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.port.PortType;
import org.proteinevolution.ProteinevolutionNodePlugin;
import org.proteinevolution.models.spec.HHR;
import org.proteinevolution.preferences.hhsuite.HHSuitePreferencePage;

public abstract class HHSuiteNodeModel extends NodeModel {


	/**
	 * Returns the datatable specification of an HHR file
	 * 
	 * @return DataTableSpec of an HHR file
	 */
	public static final DataTableSpec getHHRDataTableSpec() {

		return new DataTableSpec(new DataColumnSpec[] {

				new DataColumnSpecCreator("No", IntCell.TYPE ).createSpec(),
				new DataColumnSpecCreator("Hit", StringCell.TYPE ).createSpec(),
				new DataColumnSpecCreator("Probability", DoubleCell.TYPE ).createSpec(),
				new DataColumnSpecCreator("E-value", DoubleCell.TYPE ).createSpec(),
				new DataColumnSpecCreator("P-value", DoubleCell.TYPE ).createSpec(),
				new DataColumnSpecCreator("Score", DoubleCell.TYPE ).createSpec(),
				new DataColumnSpecCreator("SS", DoubleCell.TYPE ).createSpec(),
				new DataColumnSpecCreator("Cols", IntCell.TYPE ).createSpec(),
				new DataColumnSpecCreator("Query_start", IntCell.TYPE ).createSpec(),
				new DataColumnSpecCreator("Query_end", IntCell.TYPE ).createSpec(),
				new DataColumnSpecCreator("Template_start", IntCell.TYPE ).createSpec(),
				new DataColumnSpecCreator("Template_end", IntCell.TYPE ).createSpec(),
				new DataColumnSpecCreator("Ref", IntCell.TYPE ).createSpec(),
		});
	}
	
	public static final DataCell[] getHHRRow(final String line) {
		
		// Get the last integer (remove the parentheses)
		String ref = line.substring(HHR.REF_START).trim();
		ref = ref.substring(1, ref.length() - 1);
		
		return new DataCell[] {

				IntCellFactory.create(line.substring(HHR.NO_START,HHR.NO_END).trim()),
				StringCellFactory.create(line.substring(HHR.HIT_START, HHR.HIT_END).trim()),
				DoubleCellFactory.create(line.substring(HHR.PROB_START, HHR.PROB_END).trim()),
				DoubleCellFactory.create(line.substring(HHR.EVAL_START, HHR.EVAL_END).trim()),
				DoubleCellFactory.create(line.substring(HHR.PVAL_START, HHR.PVAL_END).trim()),
				DoubleCellFactory.create(line.substring(HHR.SCORE_START, HHR.SCORE_END).trim()),
				DoubleCellFactory.create(line.substring(HHR.SS_START, HHR.SS_END).trim()),
				IntCellFactory.create(line.substring(HHR.COLS_START, HHR.COLS_END).trim()),
				IntCellFactory.create(line.substring(HHR.QUERY_START_START, HHR.QUERY_START_END).trim()),
				IntCellFactory.create(line.substring(HHR.QUERY_END_START, HHR.QUERY_END_END).trim()),
				IntCellFactory.create(line.substring(HHR.TEMPLATE_START_START, HHR.TEMPLATE_START_END).trim()),
				IntCellFactory.create(line.substring(HHR.TEMPLATE_END_START, HHR.TEMPLATE_END_END).trim()),
				IntCellFactory.create(ref)
		};	
	}
	

	protected HHSuiteNodeModel(int nrInDataPorts, int nrOutDataPorts) throws InvalidSettingsException {
		super(nrInDataPorts, nrOutDataPorts);

		File file = this.getExecutable();
		if (file == null) {

			throw new InvalidSettingsException("Executable of " + this.getExecutableName() + " could not be found. Check HHsuite configuration.");
		}
		file.setExecutable(true);
	}

	protected HHSuiteNodeModel(PortType[] inPortTypes, PortType[] outPortTypes) throws InvalidSettingsException {
		super(inPortTypes, outPortTypes);

		File file = this.getExecutable();
		if (file == null) {

			throw new InvalidSettingsException("Executable of " + this.getExecutableName() + " could not be found. Check HHsuite configuration.");
		}
		file.setExecutable(true);
	}


	/**
	 * Returns the name of the executable of the HHsuite tool.
	 * 
	 * @return Name of the executable  of the HHsuite tool.
	 */
	protected abstract String getExecutableName();


	/**
	 * Returns the full absolute path to the HHsuite tool executable.
	 * If the methods fails doing so, it returns null;
	 * 
	 * @return Path to the HHsuite tool executable or null if this is not possible.
	 */
	protected final File getExecutable() {

		File file = new File(
				ProteinevolutionNodePlugin.getDefault().getPreferenceStore().getString(HHSuitePreferencePage.HHSUITE_EXECUTABLE_PATH),
				this.getExecutableName());
		if ( ! file.exists() || ! file.isFile()) {

			return null;
		}
		return file;
	}
}
