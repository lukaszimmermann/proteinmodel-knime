package org.proteinevolution.knime.util;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.knime.core.node.defaultnodesettings.SettingsModelString;


/**
 * 
 * 
 * @author lzimmermann
 *
 */
abstract public class SettingsModelStringChangeListener implements ChangeListener {


	protected String lastValue;
	
	@Override
	public void stateChanged(ChangeEvent e) {
		
		this.lastValue = ( (SettingsModelString) e.getSource()).getStringValue();
	}
}
