package org.proteinevolution.models.knime.hhsuitedb;

import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NotConfigurableException;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.SettingsModelStringArray;
import org.knime.core.node.port.PortObjectSpec;

public class DialogComponentHHsuiteDBSelection extends DialogComponent  {

	
	 /** Contains all column names matching the given given filter class. */
    private final JList<String> m_chooser;
    private final JLabel m_label;
    private final int m_specIndex;
    private String[] names;

  
    public DialogComponentHHsuiteDBSelection(
    		final SettingsModelStringArray model,
            final String label, 
            final int specIndex) {
        
    	super(model);
        
        this.m_label = new JLabel(label);
        this.getComponentPanel().add(m_label);
        
        this.m_chooser = new JList<String>(new DefaultListModel<String>());
        this.m_chooser.setEnabled(true);
        this.m_chooser.setVisible(true);
        this.m_chooser.setVisibleRowCount(4);
        
        this.m_chooser.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				updateModel();
			}
		});
        
        getComponentPanel().add(m_chooser);
        
        this.m_specIndex = specIndex;      
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateComponent() {
    	
    	// Sets the names of the databases again in the ListModel of the selectionn component
    	DefaultListModel<String> lm = (DefaultListModel<String>) this.m_chooser.getModel();
    	lm.removeAllElements();
    	for (String name : this.names) {
    		
    		lm.addElement(name);
    	}
    	this.m_chooser.setModel(lm);
   
    	// update the enable status (required by contract)
    	this.setEnabledComponents(this.getModel().isEnabled());
    }

    
    /**
     * Transfers the selected value from the component into the settings model.
     */
    private void updateModel() {
    	
    	List<String> valuesList = this.m_chooser.getSelectedValuesList();
    	String[] valuesArray = new String[valuesList.size()];
    	valuesArray = valuesList.toArray(valuesArray);	
    	((SettingsModelStringArray) this.getModel()).setStringArrayValue(valuesArray);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void checkConfigurabilityBeforeLoad(final PortObjectSpec[] specs)
            throws NotConfigurableException {
        
        if ((specs == null) || (specs.length <= m_specIndex)) {
            throw new NotConfigurableException("Need input table spec to "
                    + "configure dialog. Configure or execute predecessor "
                    + "nodes.");
        }
        if ( ! (specs[m_specIndex] instanceof HHsuiteDBPortObjectSpec)) {
        	
        	throw new NotConfigurableException("Input at port with number " + m_specIndex + " is not a HHsuite database input.");
        }
        this.names = ((HHsuiteDBPortObjectSpec) specs[m_specIndex]).getNames();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettingsBeforeSave()
            throws InvalidSettingsException {
    	
    	this.updateModel();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void setEnabledComponents(final boolean enabled) {
        this.m_chooser.setEnabled(enabled);
        this.m_label.setEnabled(enabled);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setToolTipText(final String text) {
        this.m_chooser.setToolTipText(text);
        this.m_label.setToolTipText(text);
    }
}
