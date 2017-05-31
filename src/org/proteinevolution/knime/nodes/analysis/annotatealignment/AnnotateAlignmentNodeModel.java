package org.proteinevolution.knime.nodes.analysis.annotatealignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataTableSpec;
import org.knime.core.data.uri.URIPortObject;
import org.knime.core.data.uri.URIPortObjectSpec;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentContent;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentPortObject;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentPortObjectSpec;


/**
 * This is the model implementation of AnnotateAlignment.
 * 
 *
 * @author Lukas Zimmermann
 */
public class AnnotateAlignmentNodeModel extends NodeModel {

	// the logger instance
	private static final NodeLogger logger = NodeLogger
			.getLogger(AnnotateAlignmentNodeModel.class);


	/**
	 * Constructor for the node model.
	 */
	protected AnnotateAlignmentNodeModel() {

		super(new PortType[] {SequenceAlignmentPortObject.TYPE, PortTypeRegistry.getInstance().getPortType(URIPortObject.class)},
				new PortType[] {SequenceAlignmentPortObject.TYPE});  
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObject[] execute(final PortObject[] inData,
			final ExecutionContext exec) throws Exception {


		// Load SS file and sequence
		SequenceAlignmentContent alignment = ((SequenceAlignmentPortObject) inData[0]).getAlignment();
		String urc = ((URIPortObject) inData[1]).getURIContents().get(0).getURI().getPath();

		List<Character> chars = new ArrayList<Character>();
		char[] result;
		if (urc.endsWith("ss")) {

			try(BufferedReader bufferedReader = new BufferedReader(new FileReader(urc))) {

				String line;
				while ( (line = bufferedReader.readLine()) != null) {

					chars.add(line.split("\\s+")[3].toCharArray()[0]);
				}
			}
			result = new char[chars.size()];

			for (int i = 0; i < result.length; ++i) {

				result[i] = chars.get(i).charValue();
			}
		} else {

			throw new InvalidSettingsException("Unsupported file extension for annotation file");
		}
		alignment.addAnnotation(result);
		
		return new SequenceAlignmentPortObject[] {
				
				new SequenceAlignmentPortObject(
						alignment, 
						new SequenceAlignmentPortObjectSpec(SequenceAlignmentContent.TYPE, alignment.getAlignmentFormat()))	
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void reset() {
		// TODO Code executed on reset.
		// Models build during execute are cleared here.
		// Also data handled in load/saveInternals will be erased here.
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
			throws InvalidSettingsException {

		// Validate some properties
		List<String> extensions = ((URIPortObjectSpec) inSpecs[1]).getFileExtensions();

		if (extensions.size() != 1) {

			throw new InvalidSettingsException("AnnotateAlignment node only expects exactly one file, but either none or multiple extensions were encountered.");
		}

		return new DataTableSpec[]{null};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {

		// TODO save user settings to the config object.

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
	CanceledExecutionException {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
	CanceledExecutionException {

	}
}

