package org.proteinevolution.models.knime.alignment;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.proteinevolution.models.interfaces.ISequenceAlignment;

import sun.print.BackgroundLookupListener;

public class JAlignmentViewer extends JPanel {

	private static final long serialVersionUID = -2942263928345287502L;

	// The data table
	private final JTable table;



	private final class SequenceAlignmentCellRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = -3302145089056093181L;
		private final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();


		@Override 
		public Component getTableCellRendererComponent(
				final JTable table,
				final Object value,
				final boolean isSelected,
				final boolean hasFocus,
				final int row,
				final int column) {

			Component renderer = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			Color background;

			char c = ((Character) value).charValue();

			switch(c) {

			case 'A':
				background = new Color( 204 , 255 , 0 );
				break;

			case 'V':
				background = new Color( 153 , 255 , 0 );
				break;

			case 'I':
				background = new Color( 102 , 255 , 0 );
				break;

			case 'M':
				background = new Color( 0 , 255 , 0 );
				break;

			case 'P':
				background = new Color( 255 , 204 , 0 );
				break;

			case 'G':
				background =  new Color( 255 , 153 , 0 );
				break;

			case 'C':
				background = new Color( 255 , 255 , 0 );
				break;

			case 'D':
				background = new Color( 255 , 0 , 0 );
				break;

			case 'E':
				background = new Color( 255 , 0 , 102 );
				break;

			case 'F':
				background = new Color( 0 , 255 , 102 );
				break;

			case 'W':
				background = new Color( 0 , 204 , 255 );
				break;

			case 'Y':
				background = new Color( 0 , 255 , 204 );
				break;


			case 'S':
				background = new Color( 255 , 51 , 0 );
				break;

			case 'T':
				background = new Color( 255 , 102 , 0 );
				break;

			case 'N':

				background = new Color( 204 , 0 , 255 );
				break;

			case 'Q':
				background = new Color( 255 , 0 , 204 );
				break;

			case 'L':
				background =  new Color( 102 , 0 , 255 );
				break;

			case 'H':
				background =  new Color( 0 , 102 , 255 );
				break;

			case 'R':
				background = new Color( 0 , 0 , 255 );
				break;

			default:
				background = new Color( 0 , 0 , 255 );
				break;
			}
			
			renderer.setBackground(background);
			return renderer;
		}
	}





	public JAlignmentViewer(final ISequenceAlignment sequenceAlignment) {

		Integer[] columnNames = new Integer[sequenceAlignment.getLength()];

		for (int i = 0; i < columnNames.length; ++i) {

			columnNames[i] = i + 1;
		}

		this.table = new JTable(sequenceAlignment.getAllUnsafe(), columnNames);

		SequenceAlignmentCellRenderer renderer = new SequenceAlignmentCellRenderer();
		renderer.setHorizontalAlignment( JLabel.CENTER);

		TableColumnModel tcm = table.getColumnModel();

		// Configure the columns
		for (int i = 0; i < tcm.getColumnCount(); ++i) {

			TableColumn column = tcm.getColumn(i);

			column.setPreferredWidth(5);
			column.setCellRenderer(renderer);
		}

		// Set the font to monospace
		this.table.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));

		this.add(this.table);
	}
}
