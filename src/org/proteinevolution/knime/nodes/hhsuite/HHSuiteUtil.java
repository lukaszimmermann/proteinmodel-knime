package org.proteinevolution.knime.nodes.hhsuite;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.DoubleCell;
import org.knime.core.data.def.DoubleCell.DoubleCellFactory;
import org.knime.core.data.def.IntCell;
import org.knime.core.data.def.IntCell.IntCellFactory;
import org.knime.core.data.def.StringCell;
import org.knime.core.data.def.StringCell.StringCellFactory;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.port.PortObject;
import org.proteinevolution.models.spec.HHR;

public final class HHSuiteUtil {

	private HHSuiteUtil() {

		throw new AssertionError();
	}

	public static final PortObject getHHR(final File file, final ExecutionContext exec) throws IOException {

		BufferedDataContainer container = exec.createDataContainer(new DataTableSpec(new DataColumnSpec[] {

				new DataColumnSpecCreator("No", IntCell.TYPE ).createSpec(),
				new DataColumnSpecCreator("Accession", StringCell.TYPE ).createSpec(),
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
		}));
		
		// Read HHR output file
		byte state = 0;
		int rowCounter = 0;			
		try(BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
			String line;
			while ((line = br.readLine()) != null) {

				// Empty line before hitlist
				if (line.trim().isEmpty() && state == 0) {

					// Advance by one line, we do not care about the header
					br.readLine();					
					state = 1;

					// read a line of the header block of the HHR file
				} else if (state == 1 && ! line.trim().isEmpty()) {

					// Get the last integer (remove the parentheses)
					String ref = line.substring(HHR.REF_START).trim();
					ref = ref.substring(1, ref.length() - 1);
					String[] spt = line.substring(HHR.HIT_START, HHR.HIT_END).trim().split("\\s+");

					// Add line to data table
					container.addRowToTable(new DefaultRow("Row"+rowCounter++,new DataCell[] {

							IntCellFactory.create(line.substring(HHR.NO_START,HHR.NO_END).trim()),
							StringCellFactory.create(spt[0]),
							StringCellFactory.create(spt[1]),
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
					}));

					// end of header block in HHR file
				} else if (state == 1) {

					break;
				}
			}
		} finally {

			container.close();
		}
		return container.getTable();
	}
}
