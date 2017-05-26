package org.proteinevolution.knime.porttypes.alignment.view;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import org.proteinevolution.models.interfaces.ISequenceAlignment;

public class JAlignmentView extends JPanel {

	
	private static final long serialVersionUID = -5119543171206109624L;

	public JAlignmentView(final ISequenceAlignment alignment) {
		
		this.setLayout(new BorderLayout());
		
		// The Pane to draw the alignment
		JAlignmentPane pane = new JAlignmentPane(alignment);
		
		
		JComboBox<ColorMap> colorMapSelector = new JComboBox<>(ColorMap.values());
		colorMapSelector.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				
				pane.setColorMap((ColorMap) colorMapSelector.getSelectedItem());
			}
		});
		

		JMenuBar toolbar = new JMenuBar();
		toolbar.add(colorMapSelector);
	
		this.add(toolbar, BorderLayout.NORTH);
		this.add(pane);
	}	
}
