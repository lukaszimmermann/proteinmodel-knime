package org.proteinevolution.knime.nodes.base;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.proteinevolution.externaltools.parameters.Parameter;
import org.proteinevolution.externaltools.tools.ExternalToolInvocation;
import org.proteinevolution.knime.KNIMEAdapter;

public abstract class ToolInvocationNodeFactory<A, B> extends NodeFactory<ToolInvocationNodeModel<A, B>> {

	private final ExternalToolInvocation<A, B> tool;
	private final List<SettingsModel> settingModels;
	private final List<Parameter<?>> params;

	protected abstract ExternalToolInvocation<A, B> initTool() throws IOException;
	protected abstract KNIMEAdapter<A, B> getAdapter();
		
	protected void check() throws IllegalStateException {
		
	}

	protected ToolInvocationNodeFactory() {

		this.check();
		this.settingModels = new ArrayList<SettingsModel>();
		this.params = new ArrayList<Parameter<?>>();
		
		//  Create all the SettingsModel for this tool
		try {
			this.tool = initTool();
			for (final Field f : tool.getClass().getFields()) {

				if(Parameter.class.isAssignableFrom(f.getType())) {

					// Name of the CONFIG KEY
					String configName = f.getName().toUpperCase();

					Parameter<?> param = (Parameter<?>) f.get(tool);
					this.params.add(param);
					
					this.settingModels.add(ParameterAdapter.paramToSettingsModel(param, configName));
				}
			}
		} catch(IllegalAccessException | IOException e) {
			
			throw new RuntimeException(e);
		}
	}


	@Override
	public ToolInvocationNodeModel<A, B> createNodeModel() {

		return new ToolInvocationNodeModel<A,B>(this.getAdapter(), this.tool, this.settingModels, this.params);
	}

	@Override
	protected int getNrNodeViews() {

		return 0;
	}

	@Override
	public NodeView<ToolInvocationNodeModel<A,B>> createNodeView(final int viewIndex, final ToolInvocationNodeModel<A,B> nodeModel) {

		// Do not assume view per default
		return null;
	}

	@Override
	protected boolean hasDialog() {

		//  TODO Check tool whether parameter are present
		return true;
	}

	@Override
	protected NodeDialogPane createNodeDialogPane() {

		return new ToolInvocationNodeDialog(this.settingModels, this.params);
	}
}
