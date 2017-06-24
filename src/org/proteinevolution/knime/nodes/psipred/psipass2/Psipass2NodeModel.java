package org.proteinevolution.knime.nodes.psipred.psipass2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.uri.URIContent;
import org.knime.core.data.uri.URIPortObject;
import org.knime.core.data.uri.URIPortObjectSpec;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.knime.core.node.port.PortTypeRegistry;
import org.proteinevolution.externaltools.base.CommandLine;
import org.proteinevolution.knime.nodes.base.ExecutableNodeModel;
import org.proteinevolution.knime.nodes.psipred.PSIPREDBaseNodeModel;
import org.proteinevolution.models.spec.FileExtensions;
import org.proteinevolution.models.util.URIUtils;


/**
 * This is the model implementation of Psipass2.
 * 
 *
 * @author Lukas Zimmermann
 */
public class Psipass2NodeModel extends PSIPREDBaseNodeModel {

	// the logger instance
	private static final NodeLogger logger = NodeLogger
			.getLogger(Psipass2NodeModel.class);


	public static final String ITERCOUNT_CFGKEY = "ITERCOUNT_CFGKEY";
	private final SettingsModelInteger param_itercount = new SettingsModelInteger(ITERCOUNT_CFGKEY, 1);
	
	public static final String DCA_CFGKEY = "DCA_CFGKEY";
	private final SettingsModelDouble param_dca = new SettingsModelDouble(DCA_CFGKEY, 1.0);
	
	public static final String DCB_CFGKEY = "DCB_CFGKEY";
	private final SettingsModelDouble param_dcb = new SettingsModelDouble(DCB_CFGKEY, 1.0);
	
	
	/**
	 * Constructor for the node model.
	 */
	protected Psipass2NodeModel() throws InvalidSettingsException {

		super(new PortType[] {PortTypeRegistry.getInstance().getPortType(URIPortObject.class)},
				new PortType[] {PortTypeRegistry.getInstance().getPortType(URIPortObject.class),
						PortTypeRegistry.getInstance().getPortType(URIPortObject.class)});
	}
	

	// $execdir/psipass2 $datadir/weights_p2.dat 1 1.0 1.0 $rootname.ss2 $rootname.ss > $rootname.horiz

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected PortObject[] execute(final PortObject[] inData,
			final ExecutionContext exec) throws Exception {

		File ss2_file = null;
		File horiz_file = null;
		
		// Urics for SS2 file
		List<URIContent> ss2_urics = new ArrayList<URIContent>(1);
		
		// Urics for horiz file
		List<URIContent> horiz_urics = new ArrayList<URIContent>(1);
		
		File executable = this.getExecutable();
	
		// Make assumption on location of datadir (TODO maybe not optimal)
		File datadir = new File(executable.getParentFile().getParent(), "data");
		
		try(CommandLine cmd = new CommandLine(executable)) {

			
			cmd.addOption("", new File(datadir, "weights_p2.dat").getAbsolutePath());
			cmd.addOption("", this.param_itercount.getIntValue());
			cmd.addOption("", this.param_dca.getDoubleValue());
			cmd.addOption("", this.param_dcb.getDoubleValue());
			
			// Create output files in filestore
			File storeFile = exec.createFileStore("psipass2").getFile();
			storeFile.mkdirs();
			
			ss2_file = new File(storeFile, "out.ss2");
			horiz_file = new File(storeFile, "out.horiz");
	
			cmd.addOption("", ss2_file.getAbsolutePath());
			
			// SS input file
			cmd.addOption("", ((URIPortObject) inData[0]).getURIContents().get(0).getURI().getPath());
			
			Process process = ExecutableNodeModel.exec(cmd.toString(), exec, null, null);
			// Add uric of ss2 file
			ss2_urics.add(new URIContent(ss2_file.toURI(), FileExtensions.SS2));
			
			// Add uric of horiz file
			FileUtils.copyInputStreamToFile(process.getInputStream(), horiz_file);
			horiz_urics.add(new URIContent(horiz_file.toURI(), FileExtensions.HORIZ));	
		}
		
		return new URIPortObject[] {

				new URIPortObject(new URIPortObjectSpec(FileExtensions.SS2), ss2_urics),
				new URIPortObject(new URIPortObjectSpec(FileExtensions.HORIZ), horiz_urics)
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

		URIUtils.checkURIExtension(inSpecs[0], FileExtensions.SS);
		return new DataTableSpec[]{null, null};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void saveSettingsTo(final NodeSettingsWO settings) {

		this.param_itercount.saveSettingsTo(settings);
		this.param_dca.saveSettingsTo(settings);
		this.param_dcb.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		this.param_itercount.loadSettingsFrom(settings);
		this.param_dca.loadSettingsFrom(settings);
		this.param_dcb.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		this.param_itercount.validateSettings(settings);
		this.param_dca.validateSettings(settings);
		this.param_dcb.validateSettings(settings);
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

	@Override
	protected String getExecutableName() {

		return "psipass2";
	}
}

