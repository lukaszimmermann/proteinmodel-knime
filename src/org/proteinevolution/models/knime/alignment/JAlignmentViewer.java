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

			
			

			
			Color foreground, background;
			if (isSelected) {
				//foreground = Color.YELLOW;
				background = Color.GRAY;
				
			}  else {
				
				if (row % 2 == 0) {
					//foreground = Color.BLUE;
					background = Color.WHITE;
				}  else {
					//foreground = Color.WHITE;
					background = Color.LIGHT_GRAY;
				}
			}
			renderer.setBackground(background);
			return renderer;
		}
	}

	/*
	public void renderAlignmentViewerCell
	( Graphics g, int x, int y, int width, int height, 
			JAlignmentViewer viewer, Element element, 
			SequenceAlignmentPoint location, Color bgColor, 
			boolean isSelected, boolean hasFocus, boolean isAtPoint )
	{
		if( bgColor == null || element == Gap.GAP )
			bgColor = viewer.getBackground();

		Color borderColor = Color.black;

		if( element != null ){


			//draw the contents colour
			g.setColor( bgColor );
			g.fillRect( x + 1, y + 1, width - 2, height - 2 );



			// now draw the char. 
			// (PENDING:- PL) Need to work out the metrics properly. 
			g.setColor( Color.black );
			FontMetrics mets = g.getFontMetrics();
			int  yOff, xOff;
			int charWidth = mets.charWidth( element.toChar() );
			xOff = (width - charWidth ) / 2;
			yOff = (height + mets.getHeight() - mets.getDescent()) / 2;

			charArray[ 0 ] = element.toChar();
			g.drawChars( charArray, 0, 1, x + xOff, y + yOff );
			//if( Debug.debug ) Debug.message( "Drawing cell within " + x + " " + y + " " 
			//			       + width + " " + height + " .Char is at " + x
			//			       + " " + (y + yOff) );

		}
		else{
			borderColor = bgColor;
			g.setColor( viewer.getBackground() );
			g.fillRect( x, y, width, height );
		}

		// choose the border colour
		if( isSelected ){
			borderColor = Color.red;
		}
		if( isAtPoint ){
			borderColor = Color.green;
		}
		//draw the border
		g.setColor( borderColor );
		g.drawRect( x, y, width - 1, height - 1 );
	} */



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
		this.table.setFont(new Font("monospaced", Font.PLAIN, 12));

		this.add(this.table);
	}
}
