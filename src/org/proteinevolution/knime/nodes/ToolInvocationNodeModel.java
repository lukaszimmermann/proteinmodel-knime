package org.proteinevolution.knime.nodes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.FutureTask;

import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;
import org.knime.core.node.defaultnodesettings.SettingsModel;
import org.knime.core.node.defaultnodesettings.SettingsModelBoolean;
import org.knime.core.node.defaultnodesettings.SettingsModelDoubleBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelInteger;
import org.knime.core.node.defaultnodesettings.SettingsModelIntegerBounded;
import org.knime.core.node.defaultnodesettings.SettingsModelString;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortObjectSpec;
import org.knime.core.node.port.PortType;
import org.proteinevolution.externaltools.base.Sentinel;
import org.proteinevolution.externaltools.parameters.BooleanParameter;
import org.proteinevolution.externaltools.parameters.DoubleBoundedParameter;
import org.proteinevolution.externaltools.parameters.IntegerBoundedParameter;
import org.proteinevolution.externaltools.parameters.IntegerParameter;
import org.proteinevolution.externaltools.parameters.Parameter;
import org.proteinevolution.externaltools.parameters.PathParameter;
import org.proteinevolution.externaltools.parameters.StringSelectionParameter;
import org.proteinevolution.externaltools.tools.ExternalToolInvocation;
import org.proteinevolution.knime.KNIMEAdapter;

public final class ToolInvocationNodeModel<A, B> extends NodeModel {

	// The tool which is to be executed
	private final ExternalToolInvocation<A,B> tool;
	private final List<SettingsModel> settingsModels;
	private final List<Parameter<?>> parameters;
	private final KNIMEAdapter<A,B> knimeAdapter;

	protected ToolInvocationNodeModel(
			final KNIMEAdapter<A,B> knimeAdapter,
			final ExternalToolInvocation<A, B> tool,
			final List<SettingsModel> settingsModels,
			final List<Parameter<?>> params) {

		super(knimeAdapter.getInputPortType(), knimeAdapter.getOutputPortType());   

		this.knimeAdapter = knimeAdapter;
		this.tool = tool;
		this.settingsModels = settingsModels;
		this.parameters = params;

		if (this.knimeAdapter == null || this.tool == null || this.settingsModels == null || this.parameters == null) {

			throw new IllegalArgumentException("Constructor argument of ToolInvocationNodeModel cannot be null!");
		}
		if (this.settingsModels.size() != this.parameters.size()) {

			throw new IllegalArgumentException("SettingsModel and Parameter lists must have the same length!");
		}
	}

	@Override
	protected PortObject[] execute(final PortObject[] inObjects, final ExecutionContext exec) throws Exception {

		// Transfer setting parameters to the tool
		for (int i = 0; i < this.settingsModels.size(); ++i) {

			Parameter<?> param = this.parameters.get(i);
			SettingsModel s = this.settingsModels.get(i);

			if (param instanceof BooleanParameter) {

				((BooleanParameter) param).set(((SettingsModelBoolean) s).getBooleanValue());				
			}
			else if (param instanceof DoubleBoundedParameter) {

				((DoubleBoundedParameter) param).set(((SettingsModelDoubleBounded) s).getDoubleValue());
			}
			else if (param instanceof IntegerBoundedParameter) {

				((IntegerBoundedParameter) param).set(((SettingsModelIntegerBounded) s).getIntValue());
			}
			else if (param instanceof IntegerParameter) {

				((IntegerParameter) param).set(((SettingsModelInteger) s).getIntValue());
			}
			else if (param instanceof StringSelectionParameter) {

				((StringSelectionParameter) param).set(((SettingsModelString) s).getStringValue());

			} else if (param instanceof PathParameter) {

				((PathParameter) param).set((Paths.get( ((SettingsModelString) s).getStringValue())));
			}
			else {

				throw new Exception("Parameter class not handled in node model. Cannot execute!");
			}
		}
		this.tool.setInput(this.knimeAdapter.portToInput(inObjects));
		this.tool.setSentinel(new Sentinel() {

			@Override
			public boolean isHappy() {

				try {
					exec.checkCanceled();
					return true;
				}  catch(CanceledExecutionException e) {
					return false;
				}
			}
		});
		FutureTask<B> futureTask = new FutureTask<>(this.tool);
		Thread t = new Thread(futureTask);
		t.start();
		B result = futureTask.get();
		PortObject[] out = this.knimeAdapter.resultToPort(result, exec); 
		tool.close();
		return out;
	}


	@Override
	protected PortObjectSpec[] configure(final PortObjectSpec[] inSpecs) throws InvalidSettingsException {

		PortType[] outputPortTypes = this.knimeAdapter.getOutputPortType();
		PortObjectSpec[] result = new PortObjectSpec[outputPortTypes.length];
		for(int i = 0; i < result.length; ++i) {

			result[i] = null;
		}
		return result;
	}


	@Override
	protected void saveSettingsTo(NodeSettingsWO settings) {

		for (SettingsModel s : this.settingsModels) {	
			s.saveSettingsTo(settings);
		}
	}

	@Override
	protected void validateSettings(NodeSettingsRO settings) throws InvalidSettingsException {

		for (SettingsModel s: this.settingsModels) {

			s.validateSettings(settings);
		}
	}

	@Override
	protected void loadValidatedSettingsFrom(NodeSettingsRO settings) throws InvalidSettingsException {

		for (SettingsModel s : this.settingsModels) {

			s.loadSettingsFrom(settings);
		}
	}

	@Override
	protected void reset() {

	}

	@Override
	protected void loadInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {

		// Nothing to be done here
	}

	@Override
	protected void saveInternals(File nodeInternDir, ExecutionMonitor exec)
			throws IOException, CanceledExecutionException {

		// Nothing to be done here
	}
}
