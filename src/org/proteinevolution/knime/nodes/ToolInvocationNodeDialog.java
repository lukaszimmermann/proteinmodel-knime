package org.proteinevolution.knime.nodes;

import java.util.List;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.DialogComponentBoolean;
import org.knime.core.node.defaultnodesettings.DialogComponentNumber;
import org.knime.core.node.defaultnodesettings.DialogComponentNumberEdit;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.proteinevolution.externaltools.parameters.BooleanParameter;
import org.proteinevolution.externaltools.parameters.DoubleBoundedParameter;
import org.proteinevolution.externaltools.parameters.IntegerBoundedParameter;
import org.proteinevolution.externaltools.parameters.IntegerParameter;
import org.proteinevolution.externaltools.parameters.Parameter;
import org.proteinevolution.externaltools.parameters.StringSelectionParameter;

public final class ToolInvocationNodeDialog extends DefaultNodeSettingsPane {

	public ToolInvocationNodeDialog(final List<SettingsModel> settingsModels, final List<Parameter<?>> params) {

		assert params.size() == settingsModels.size();

		for (int i = 0; i < params.size(); ++i) {

			Parameter<?> param = params.get(i);
			SettingsModel s = settingsModels.get(i);
			String label = params.get(i).getLabel();

			if (param instanceof BooleanParameter) {

				this.addDialogComponent(new DialogComponentBoolean((SettingsModelBoolean) s, label));
			}
			else if (param instanceof DoubleBoundedParameter) {

				this.addDialogComponent(new DialogComponentNumberEdit((SettingsModelDoubleBounded) s, label));
			}
			else if (param instanceof IntegerBoundedParameter) {

				this.addDialogComponent(new DialogComponentNumber((SettingsModelIntegerBounded) s,label,1));

			}
			else if (param instanceof IntegerParameter) {

				this.addDialogComponent(new DialogComponentNumber((SettingsModelInteger) s, label, 1));
			}
			else if (param instanceof StringSelectionParameter) {

				this.addDialogComponent(
						new DialogComponentStringSelection(
								(SettingsModelString) s,
								label,
								((StringSelectionParameter) param).getOptionKeys(), 
								false));
			}
		}
	}
}
