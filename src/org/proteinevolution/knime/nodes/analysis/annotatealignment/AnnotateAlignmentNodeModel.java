package org.proteinevolution.knime.nodes.analysis.annotatealignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.knime.core.data.DataTableSpec;
import org.knime.core.data.uri.IURIPortObject;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentContent;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentPortObject;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentPortObjectSpec;
import org.proteinevolution.models.spec.FileExtensions;
import org.proteinevolution.models.util.URIUtils;


/**
 * This is the model implementation of AnnotateAlignment.
 * 
 *
 * @author Lukas Zimmermann
 */
public class AnnotateAlignmentNodeModel extends NodeModel {


	/**
	 * Constructor for the node model.
	 */
	protected AnnotateAlignmentNodeModel() {

		super(new PortType[] {SequenceAlignmentPortObject.TYPE, IURIPortObject.TYPE},
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
		String urc = ((IURIPortObject) inData[1]).getURIContents().get(0).getURI().getPath();

		List<Character> chars = new ArrayList<Character>();
		char[] result = null;
		
		// Configure must guaruantee that no other filetypes are encountered here
		if (urc.endsWith(FileExtensions.SS) || urc.endsWith(FileExtensions.SS2)) {

			try(BufferedReader bufferedReader = new BufferedReader(new FileReader(urc))) {

				String line;
				while ( (line = bufferedReader.readLine()) != null) {

					line = line.trim();
					
					// Ignore comment and empty lines
					if (line.startsWith("#") || line.isEmpty()) {
						continue;
					}
					chars.add(line.split("\\s+")[2].toCharArray()[0]);
				}
			}
			result = new char[chars.size()];

			for (int i = 0; i < result.length; ++i) {

				result[i] = chars.get(i).charValue();
			}
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

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs)
			throws InvalidSettingsException {

		Set<String> allowedExtensions = new HashSet<String>(2);
		allowedExtensions.add(FileExtensions.SS);
		allowedExtensions.add(FileExtensions.SS2);
		
		URIUtils.checkURIExtension(inSpecs[1], allowedExtensions);
		
		return new DataTableSpec[]{null};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {

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

