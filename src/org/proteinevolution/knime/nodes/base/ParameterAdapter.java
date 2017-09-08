package org.proteinevolution.knime.nodes.base;

import java.nio.file.Path;

import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentFileChooser;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelDouble;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.proteinevolution.externaltools.parameters.Parameter;
import org.proteinevolution.externaltools.parameters.StringSelectionParameter;
import org.proteinevolution.externaltools.parameters.validators.RangeValidator;


final class ParameterAdapter {

	private ParameterAdapter() {
		
		throw new AssertionError();
	}
	
	static final DialogComponent paramToDialog(
			final Parameter<?> param,
			final SettingsModel s) {
		
		final Object value = param.get();
		final String label = param.getLabel();
		final boolean isRangeValidator = param.getValidator() instanceof RangeValidator;
		
		if (value instanceof Boolean) {
			
			return new DialogComponentBoolean((SettingsModelBoolean) s, label);	
		}
		if (value instanceof Integer) {
			
			return new DialogComponentNumber(
					(isRangeValidator ? ((SettingsModelIntegerBounded) s)
							: (SettingsModelInteger) s), label, 1);
		}
		if (value instanceof Double) {
			
			return new DialogComponentNumberEdit((SettingsModelDoubleBounded) s, label);
		}
		if (value instanceof Path) {
			
			return new DialogComponentFileChooser( (SettingsModelString) s, "INPUT_HISTORY", "");
		}
		
		if (param instanceof StringSelectionParameter) {
			
			return new DialogComponentStringSelection(
					(SettingsModelString) s,
					label,
					((StringSelectionParameter) param).getOptionKeys(), 
					false);
		}
		throw new RuntimeException("Parameter not supported by Dialog!");
	}
	
	
	static final SettingsModel paramToSettingsModel(final Parameter<?> param, final String configName) {
		
		final Object value = param.get();
		
		final boolean isRangeValidator = param.getValidator() instanceof RangeValidator;
		final RangeValidator<?> validator = isRangeValidator ? (RangeValidator<?>) param.getValidator() : null;
				
		
		if (value instanceof Boolean) {
			
			return new SettingsModelBoolean(configName, (Boolean) value);
		}
		if (value instanceof Double) {
			
			final Double doubleVal = (Double) value;
			return isRangeValidator ? new SettingsModelDoubleBounded(configName, doubleVal, (Double) validator.getLower(), (Double) validator.getUpper())
		                            : new SettingsModelDouble(configName, doubleVal);
		}
		if (value instanceof Path) {
						
			return new SettingsModelString(configName, ((Path) value).toAbsolutePath().toString());
		}
		
		if (value instanceof Integer) {
			
			final Integer intVal = (Integer) value;
			return isRangeValidator ? new SettingsModelIntegerBounded(configName, intVal, (Integer) validator.getLower(), (Integer) validator.getUpper())
		                            : new SettingsModelInteger(configName, intVal);
		}
		if (param instanceof StringSelectionParameter) {
			
			return new SettingsModelString(configName, (String) value);
		}
		
		throw new RuntimeException("Parameter not supported by Dialog!");
	}
	
	
	
}
