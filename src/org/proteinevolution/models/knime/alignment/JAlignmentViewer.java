package org.proteinevolution.models.knime.alignment;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.proteinevolution.models.interfaces.ISequenceAlignment;

public class JAlignmentViewer extends JPanel {

	private static final long serialVersionUID = -2942263928345287502L;

	private final class SequenceAlignmentCellRenderer extends DefaultTableCellRenderer {

		private static final long serialVersionUID = -3302145089056093181L;
		private final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();
		private ColorMap selectedColorMap;
		
		
		public  SequenceAlignmentCellRenderer(final ColorMap colorMap) {
			
			this.selectedColorMap = colorMap;
		}
		
		public void setColorMap(final ColorMap colorMap) {
			
			this.selectedColorMap = colorMap;
		}
		
		@Override 
		public Component getTableCellRendererComponent(
				final JTable table,
				final Object value,
				final boolean isSelected,
				final boolean hasFocus,
				final int row,
				final int column) {

			Component renderer = DEFAULT_RENDERER.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			renderer.setBackground(this.selectedColorMap.getColor((Character) value));
			//renderer.setBackground(background);
			return renderer;
		}
		
		
	}



	public JAlignmentViewer(final ISequenceAlignment sequenceAlignment) {

		this.setLayout(new BorderLayout());
		
		Integer[] columnNames = new Integer[sequenceAlignment.getLength()];

		for (int i = 0; i < columnNames.length; ++i) {

			columnNames[i] = i + 1;
		}
	
		// Configure table
		JTable table = new JTable(sequenceAlignment.getAllUnsafe(), columnNames);
		table.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
		
		// Combo box for color scheme selector
		JComboBox<ColorMap> colorMapSelector = new JComboBox<>(ColorMap.values());
		SequenceAlignmentCellRenderer renderer = new SequenceAlignmentCellRenderer((ColorMap) colorMapSelector.getSelectedItem());
	
		// Change listener for colorMap
		colorMapSelector.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				
				renderer.setColorMap((ColorMap) colorMapSelector.getSelectedItem());
				JAlignmentViewer.this.repaint();
			}
		});
		
		
		renderer.setHorizontalAlignment( JLabel.CENTER);

		TableColumnModel tcm = table.getColumnModel();
		// Configure the columns
		for (int i = 0; i < tcm.getColumnCount(); ++i) {

			TableColumn column = tcm.getColumn(i);

			column.setPreferredWidth(5);
			column.setCellRenderer(renderer);
		}

		JMenuBar toolbar = new JMenuBar();
		toolbar.add(colorMapSelector);
	
		this.add(toolbar, BorderLayout.NORTH);
		
		this.add(table);
	}
}
