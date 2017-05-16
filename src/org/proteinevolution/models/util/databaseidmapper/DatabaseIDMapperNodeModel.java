package org.proteinevolution.models.util.databaseidmapper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModelString;


/**
 * This is the model implementation of DatabaseIDMapper.
 * 
 *
 * @author Lukas Zimmermann
 */
public class DatabaseIDMapperNodeModel extends NodeModel {

	// the logger instance
	private static final NodeLogger logger = NodeLogger
			.getLogger(DatabaseIDMapperNodeModel.class);


	public static final String INPUT_COLUMN_CFGKEY =  "INPUT_COLUMN";
	public static final String INPUT_COLUMN_DEFAULT =  "";
	private final SettingsModelString input_column = new SettingsModelString(INPUT_COLUMN_CFGKEY, INPUT_COLUMN_DEFAULT);

	private static final String UNIPROT_SERVER = "http://www.uniprot.org/";



	private static final class ParameterNameValue {

		private final String name;
		private final String value;

		public ParameterNameValue(String name, String value) throws UnsupportedEncodingException {

			this.name = URLEncoder.encode(name, "UTF-8");
			this.value = URLEncoder.encode(value, "UTF-8");
		}
	}

	/**
	 * Constructor for the node model.
	 */
	protected DatabaseIDMapperNodeModel() {

		super(1, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {

		// Getting the IDs from the table
		BufferedDataTable tab = inData[0];
		int idx = tab.getDataTableSpec().findColumnIndex(this.input_column.getStringValue());
	
		
		StringBuilder sb = new StringBuilder();
		// the data table spec of the single output table, 
		// the table will have three columns:
		DataColumnSpec[] allColSpecs = new DataColumnSpec[1];
		allColSpecs[0] =  new DataColumnSpecCreator("Accession", StringCell.TYPE).createSpec();
		
		
		for(DataRow row : tab) {
		
			sb.append(((StringCell) row.getCell(idx)).getStringValue());
			sb.append(" ");
		}
		
		StringBuilder locationBuilder = new StringBuilder(UNIPROT_SERVER +  "uploadlists/?");
		ParameterNameValue[] params = new ParameterNameValue[] {
				new ParameterNameValue("from", "P_REFSEQ_AC"),
				new ParameterNameValue("to", "ID"),
				new ParameterNameValue("format", "list"),
				new ParameterNameValue("query", sb.toString().trim())
		};
			
		for (int i = 0; i < params.length; i++) {
			
			if (i > 0) {
				locationBuilder.append('&');
			}
			locationBuilder.append(params[i].name).append('=').append(params[i].value);
		}
		// Open connection
		String location = locationBuilder.toString();
						
		HttpURLConnection conn = (HttpURLConnection) new URL(locationBuilder.toString()).openConnection();
		conn.setDoInput(true);
		conn.connect();
	
		int status = conn.getResponseCode();
		
		while (true) {
		
			int wait = 0;
			String header = conn.getHeaderField("Retry-After");
			
			if (header != null) {
				wait = Integer.valueOf(header);
			}
			if (wait == 0) {
				break;
			}
			
			conn.disconnect();
			Thread.sleep(wait * 1000);
			conn = (HttpURLConnection) new URL(location).openConnection();
			conn.setDoInput(true);
			conn.connect();
			status = conn.getResponseCode();
		}
		
		if (status == HttpURLConnection.HTTP_OK) {
			
			InputStream reader = conn.getInputStream();		
			URLConnection.guessContentTypeFromStream(reader);
			StringBuilder builder = new StringBuilder();
			int a = 0;
			while ((a = reader.read()) != -1) {
				builder.append((char) a);
			}
			logger.warn(builder.toString());
		}
		
		
		
		
		


		DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
		BufferedDataContainer container = exec.createDataContainer(outputSpec);
		// once we are done, we close the container and return its table
		container.close();
		return new BufferedDataTable[]{container.getTable()};
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

		this.input_column.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {
		
		this.input_column.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		this.input_column.validateSettings(settings);
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

