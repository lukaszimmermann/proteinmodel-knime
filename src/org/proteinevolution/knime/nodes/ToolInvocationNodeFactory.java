package org.proteinevolution.knime.nodes;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.knime.core.node.NodeDialogPane;
import org.knime.core.node.NodeFactory;
import org.knime.core.node.NodeView;
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
import org.proteinevolution.externaltools.tools.ExternalToolInvocation;
import org.proteinevolution.knime.KNIMEAdapter;

public abstract class ToolInvocationNodeFactory<A, B> extends NodeFactory<ToolInvocationNodeModel<A, B>> {

	private final ExternalToolInvocation<A, B> tool;
	private final List<SettingsModel> settingModels;
	private final List<Parameter<?>> params;

	protected abstract ExternalToolInvocation<A, B> initTool();
	protected abstract KNIMEAdapter<A, B> getAdapter();
	protected abstract void check() throws IllegalStateException;


	protected ToolInvocationNodeFactory(){

		this.check();
		this.tool = initTool();
		this.settingModels = new ArrayList<SettingsModel>();
		this.params = new ArrayList<Parameter<?>>();
		//  Create all the SettingsModel for this tool

		try {
			for (Field f : tool.getClass().getFields()) {

				if(Parameter.class.isAssignableFrom(f.getType())) {

					// Name of the CONFIG KEY
					String configName = f.getName().toUpperCase();

					Parameter<?> param = (Parameter<?>) f.get(tool);
					this.params.add(param);

					if (param instanceof BooleanParameter) {

						this.settingModels.add(new SettingsModelBoolean(configName, ((BooleanParameter) param).get()));
					}
					else if (param instanceof DoubleBoundedParameter) {

						DoubleBoundedParameter doubleBoundedParam = (DoubleBoundedParameter) param;
						this.settingModels.add(new SettingsModelDoubleBounded(
								configName,
								doubleBoundedParam.get(),
								doubleBoundedParam.getLower(),
								doubleBoundedParam.getUpper()));
					}
					else if (param instanceof IntegerBoundedParameter) {

						IntegerBoundedParameter intBoundedParam = (IntegerBoundedParameter) param;
						this.settingModels.add(new SettingsModelIntegerBounded(
								configName,
								intBoundedParam.get(), 
								intBoundedParam.getLower(),
								intBoundedParam.getUpper()));
					}
					else if (param instanceof IntegerParameter) {
						this.settingModels.add(new SettingsModelInteger(configName, ((IntegerParameter) param).get()));
					}
					else if (param instanceof StringSelectionParameter) {

						this.settingModels.add(new SettingsModelString(configName, ((StringSelectionParameter) param).get()));

					}
				}
			}
		} catch(IllegalAccessException e) {


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
