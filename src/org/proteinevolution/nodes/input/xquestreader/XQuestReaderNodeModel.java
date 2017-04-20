package org.proteinevolution.nodes.input.xquestreader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.MissingCell;
import org.knime.core.data.RowKey;
import org.knime.core.data.collection.CollectionCellFactory;
import org.knime.core.data.collection.SetCell;
import org.knime.core.data.def.BooleanCell;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.DoubleCell.DoubleCellFactory;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.def.IntCell.IntCellFactory;
import org.knime.core.data.def.StringCell.StringCellFactory;
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
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


/**
 * This is the model implementation of XQuestReader.
 * 
 *
 * @author Lukas Zimmermann
 */
public class XQuestReaderNodeModel extends NodeModel {

	// the logger instance
	private static final NodeLogger logger = NodeLogger
			.getLogger(XQuestReaderNodeModel.class);

	/** the settings key which is used to retrieve and 
        store the settings (from the dialog or from a settings file)    
       (package visibility to be usable from the dialog). */

	// Input file
	public static final String INPUT_CFGKEY = "Input File";
	public static final String INPUT_DEFAULT = "/local/work/knime_node_devel/merged_xquest.xml";
	public static final String INPUT_HISTORYKEY = "INPUT_HISTORYKEY";
	public static final String INPUT_VALIDEXT = "xml";
	
	// Decoy String
	public static final String DECOY_CFGKEY = "Decoy String";
	public static final String DECOY_DEFAULT = "decoy_reverse_";	
	
	// Number of columns
	private static final int N_COLUMNS = 15;
	
	// Attribute names
	private static final String ATTRIBUTE_TYPE = "type";
	private static final String ATTRIBUTE_SEQ1 = "seq1";
	private static final String ATTRIBUTE_SEQ2 = "seq2";
	private static final String ATTRIBUTE_SCORE = "score";
	private static final String ATTRIBUTE_XPROPHET_F = "xprophet_f";
	private static final String ATTRIBUTE_FDR = "fdr";
	private static final String ATTRIBUTE_XLINKPOSITION = "xlinkposition";
	private static final String ATTRIBUTE_PROT1 = "prot1";
	private static final String ATTRIBUTE_PROT2 = "prot2";
	private static final String ATTRIBUTE_SEARCH_HIT_RANK = "search_hit_rank";

	
	// example value: the models count variable filled from the dialog 
	// and used in the models execution method. The default components of the
	// dialog work with "SettingsModels".
	private final SettingsModelString input_file =
			new SettingsModelString(
					XQuestReaderNodeModel.INPUT_CFGKEY, 
					XQuestReaderNodeModel.INPUT_DEFAULT);
	
	private final SettingsModelString decoy_string =
			new SettingsModelString(
					XQuestReaderNodeModel.DECOY_CFGKEY, 
					XQuestReaderNodeModel.DECOY_DEFAULT);
	
	
	// Handler for the xQuest file
	private class XQuestHandler extends DefaultHandler {

		// Tag names
		private static final String TAG_SEARCHHIT = "search_hit";
		
		private BufferedDataContainer container;
		private ExecutionContext exec;
		private int key_counter;
		private String decoy_string;

		// Protein  information of current cross-link identification
		private SetCell protids1;
		private SetCell protids2;
		private boolean is_proteininterlink;  // Whether xlink was identified to link two proteins with different ID
		private boolean is_proteinintralink;  // Whether xlink was identified to interlink one protein
		private boolean is_decoy1;            // Whether seq1 has matched the decoy database
		private boolean is_decoy2;            // Whether seq2 has matched the decoy database
	
		/**
		 * Splits String by separator and returns SetCell representing the content of the String
		 * 
		 * @param input which String to split
		 * @return SetCell with content of the String
		 */
		private void computeProteinInformation(String prot1, String prot2, String sep) {
		
			this.protids1 = null;
			this.protids2 = null;
			this.is_proteininterlink = false;
			this.is_proteinintralink = false;
			this.is_decoy1 = false;
			this.is_decoy2 = false;
			
			String[] prot1_split = prot1.split(sep);
			String[] prot2_split = prot2.split(sep);
			
			List<DataCell> input_cells1 = new ArrayList<DataCell>(prot1_split.length);
			List<DataCell> input_cells2 = new ArrayList<DataCell>(prot2_split.length);
			
			for (String input_item1 : prot1_split) {
				input_cells1.add(new StringCell(input_item1));
				
				if (input_item1.startsWith(this.decoy_string))
				{
					this.is_decoy1 = true;
				}
				String clean_input_item1 = input_item1.replace(this.decoy_string, "");
				for (String input_item2 : prot2_split) {
					
					if (input_item2.startsWith(this.decoy_string)) {
						this.is_decoy2 = true;
					}
					String clean_input_item2 = input_item2.replace(this.decoy_string, "");
					
					if (clean_input_item1.equals(clean_input_item2)) {
						
						this.is_proteinintralink = true;
					} else {
						this.is_proteininterlink = true;
					}
				}
			}
			for (String input_item2 : prot2_split) {
				input_cells2.add(new StringCell(input_item2));
			}			
			this.protids1 = CollectionCellFactory.createSetCell(input_cells1);
			this.protids2 = CollectionCellFactory.createSetCell(input_cells2);	
		}
		
		public XQuestHandler(BufferedDataContainer container, ExecutionContext exec, String decoy_string) {
			this.container = container;
			this.exec = exec;
			this.key_counter = 0;
			this.decoy_string = decoy_string;			
		}
		
		@Override
		public void startElement(String uri,
				String localName,
				String qName,
				Attributes attributes)
						throws SAXException {

			if (qName.equals(TAG_SEARCHHIT)) {

				RowKey key = new RowKey("Row " + this.key_counter++);
				DataCell[] cells = new DataCell[N_COLUMNS];
				
				// Rank
				cells[0] = IntCellFactory.create(attributes.getValue(ATTRIBUTE_SEARCH_HIT_RANK));

				// Type
				String type = attributes.getValue(ATTRIBUTE_TYPE);
				cells[1] = StringCellFactory.create(type);
				
				// sequences
				cells[2] = StringCellFactory.create(attributes.getValue(ATTRIBUTE_SEQ1));
				cells[3] = StringCellFactory.create(attributes.getValue(ATTRIBUTE_SEQ2));
				
				// xlinkposition
				String[] xlinkposition = attributes.getValue(ATTRIBUTE_XLINKPOSITION).split(",");	
				cells[4] = IntCellFactory.create(xlinkposition[0]);
				cells[5] =  !type.equals("monolink") ? IntCellFactory.create(xlinkposition[1]): new MissingCell("monolink");

				// Proteininformation	
				this.computeProteinInformation(
						attributes.getValue(ATTRIBUTE_PROT1),
						attributes.getValue(ATTRIBUTE_PROT2), ",");
				
				cells[6] = this.protids1;
				cells[7] = this.protids2;
				cells[10] = this.is_decoy1 ? BooleanCell.TRUE : BooleanCell.FALSE;
				if (type.equals("xlink")) {
					cells[8] = this.is_proteininterlink ? BooleanCell.TRUE : BooleanCell.FALSE;
					cells[9] = this.is_proteinintralink ? BooleanCell.TRUE : BooleanCell.FALSE;
					cells[11] = this.is_decoy2  ? BooleanCell.TRUE : BooleanCell.FALSE;
				} else {
					
					cells[8] = new MissingCell("no xlink");
					cells[9] = new MissingCell("no xlink");
					cells[11] = new MissingCell("no xlink");
				}
				
				// Score
				cells[12] = DoubleCellFactory.create(attributes.getValue(ATTRIBUTE_SCORE));

				// Whether seen by xProphet
				String xprophet_f = attributes.getValue(ATTRIBUTE_XPROPHET_F);
				if (xprophet_f == null) {	
					cells[13] = new MissingCell("No FDR annotation.");
					
				} else {
					cells[13] = xprophet_f.equals("1") ? BooleanCell.TRUE : BooleanCell.FALSE;
				}
		
				// FDR value
				String fdr = attributes.getValue(ATTRIBUTE_FDR);
				if (fdr == null)
				{
					cells[14] = new MissingCell("No FDR annotation");
				} else {
					cells[14] = DoubleCellFactory.create(fdr);
				}
			
				container.addRowToTable(new DefaultRow(key, cells));				
				try {
					this.exec.checkCanceled();
				}
				catch(CanceledExecutionException e) {
				}
			}
		}
	}

	/**
	 * Constructor for the node model.
	 */
	protected XQuestReaderNodeModel() {
		super(0, 1);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
			final ExecutionContext exec) throws Exception {

		
		DataColumnSpec[] allColSpecs = new DataColumnSpec[N_COLUMNS];
		allColSpecs[0] = new DataColumnSpecCreator(ATTRIBUTE_SEARCH_HIT_RANK, IntCell.TYPE).createSpec();
		allColSpecs[1] = new DataColumnSpecCreator(ATTRIBUTE_TYPE, StringCell.TYPE).createSpec();
		allColSpecs[2] = new DataColumnSpecCreator(ATTRIBUTE_SEQ1, StringCell.TYPE).createSpec();
		allColSpecs[3] = new DataColumnSpecCreator(ATTRIBUTE_SEQ2, StringCell.TYPE).createSpec();
		allColSpecs[4] = new DataColumnSpecCreator("pos1", IntCell.TYPE).createSpec();
		allColSpecs[5] = new DataColumnSpecCreator("pos2", IntCell.TYPE).createSpec();
		allColSpecs[6] = new DataColumnSpecCreator(ATTRIBUTE_PROT1, SetCell.getCollectionType(StringCell.TYPE)).createSpec();
		allColSpecs[7] = new DataColumnSpecCreator(ATTRIBUTE_PROT2, SetCell.getCollectionType(StringCell.TYPE)).createSpec();
		allColSpecs[8] = new DataColumnSpecCreator("is_proteininterlink", BooleanCell.TYPE).createSpec();
		allColSpecs[9] = new DataColumnSpecCreator("is_proteinintralink", BooleanCell.TYPE).createSpec();
		allColSpecs[10] = new DataColumnSpecCreator("is_decoy1", BooleanCell.TYPE).createSpec();
		allColSpecs[11] = new DataColumnSpecCreator("is_decoy2", BooleanCell.TYPE).createSpec();
		allColSpecs[12] = new DataColumnSpecCreator(ATTRIBUTE_SCORE, DoubleCell.TYPE).createSpec();
		allColSpecs[13] = new DataColumnSpecCreator(ATTRIBUTE_XPROPHET_F, BooleanCell.TYPE).createSpec();
		allColSpecs[14] = new DataColumnSpecCreator(ATTRIBUTE_FDR, DoubleCell.TYPE).createSpec();

		DataTableSpec outputSpec = new DataTableSpec(allColSpecs);
		// the execution context will provide us with storage capacity, in this
		// case a data container to which we will add rows sequentially
		// Note, this container can also handle arbitrary big data tables, it
		// will buffer to disc if necessary.
		BufferedDataContainer container = exec.createDataContainer(outputSpec);

		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		try {
			SAXParser saxParser = factory.newSAXParser();
			File file = new File(this.input_file.getStringValue());
			saxParser.parse(file, new XQuestHandler(container, exec, this.decoy_string.getStringValue())); 			
		} catch(ParserConfigurationException e1) {
			logger.warn("ParserConfigurationException");

		} catch(SAXException e1) {
			logger.warn("SAXException");
		}
		catch(IOException e) {
			logger.warn("IOException");
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

		input_file.saveSettingsTo(settings);
		decoy_string.saveSettingsTo(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		// TODO load (valid) settings from the config object.
		// It can be safely assumed that the settings are valided by the 
		// method below.

		input_file.loadSettingsFrom(settings);
		decoy_string.loadSettingsFrom(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void validateSettings(final NodeSettingsRO settings)
			throws InvalidSettingsException {

		// TODO check if the settings could be applied to our model
		// e.g. if the count is in a certain range (which is ensured by the
		// SettingsModel).
		// Do not actually set any values of any member variables.

		input_file.validateSettings(settings);
		decoy_string.validateSettings(settings);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void loadInternals(final File internDir,
			final ExecutionMonitor exec) throws IOException,
	CanceledExecutionException {

		// TODO load internal data. 
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

		// TODO save internal models. 
		// Everything written to output ports is saved automatically (data
		// returned by the execute method, models saved in the saveModelContent,
		// and user settings saved through saveSettingsTo - is all taken care 
		// of). Save here only the other internals that need to be preserved
		// (e.g. data used by the views).

	}

}

