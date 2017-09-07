package org.proteinevolution.knime.nodes.base;

import java.util.List;

import org.knime.core.node.defaultnodesettings.DefaultNodeSettingsPane;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.proteinevolution.externaltools.parameters.Parameter;

public final class ToolInvocationNodeDialog extends DefaultNodeSettingsPane {

	public ToolInvocationNodeDialog(final List<SettingsModel> settingsModels, final List<Parameter<?>> params) {

		assert params.size() == settingsModels.size();

		for (int i = 0; i < params.size(); ++i) {

			this.addDialogComponent(ParameterAdapter.paramToDialog(params.get(i), settingsModels.get(i)));
		}
	}
}
