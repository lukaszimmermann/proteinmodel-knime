package org.proteinevolution.knime.nodes.base;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
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
import org.proteinevolution.externaltools.parameters.Parameter;
import org.proteinevolution.externaltools.parameters.StringSelectionParameter;
import org.proteinevolution.externaltools.parameters.validators.RangeValidator;
import org.proteinevolution.externaltools.tools.ExternalToolInvocation;
import org.proteinevolution.knime.KNIMEAdapter;


public final class ToolInvocationNodeModel<A, B> extends NodeModel {
	
	private static class KNIMESentinel implements Sentinel {

		private boolean isHappy = true;
		private final ExecutionContext exec;
		
		public KNIMESentinel(final ExecutionContext exec) {
			
			this.exec = exec;
		}
		
		@Override
		public boolean isHappy() {
			
			if ( ! this.isHappy) {
				
				return false;
			}
			try {
				exec.checkCanceled();
				
			}  catch(final CanceledExecutionException e) {
				
				this.isHappy = false;
			}
			return this.isHappy;
		}
	}
	
	
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

	@SuppressWarnings("unchecked")
	@Override
	protected PortObject[] execute(final PortObject[] inObjects, final ExecutionContext exec) throws Exception {

		// Transfer setting parameters to the tool
		for (int i = 0; i < this.settingsModels.size(); ++i) {

			final Parameter<?> param = this.parameters.get(i);
			final Object value = param.get();
			final boolean isRangeValidator = param.getValidator() instanceof RangeValidator;
			
			final SettingsModel s = this.settingsModels.get(i);

			if (value instanceof Boolean) {

				((Parameter<Boolean>) param).set(((SettingsModelBoolean) s).getBooleanValue());				
			}
			else if (value instanceof Double && isRangeValidator) {

				((Parameter<Double>) param).set(((SettingsModelDoubleBounded) s).getDoubleValue());
			}
			else if (value instanceof Integer && isRangeValidator) {

				((Parameter<Integer>) param).set(((SettingsModelIntegerBounded) s).getIntValue());
			}
			else if (value instanceof Path) {
				
				((Parameter<Path>) param).set(Paths.get(((SettingsModelString) s).getStringValue()));
			}
			else if (value instanceof Integer) {

				((Parameter<Integer>) param).set(((SettingsModelInteger) s).getIntValue());
			}
			else if (param instanceof StringSelectionParameter) {

				((StringSelectionParameter) param).set(((SettingsModelString) s).getStringValue());

			} else {

				throw new Exception("Parameter class not handled in node model. Cannot execute!");
			}
		}
		this.tool.setInput(this.knimeAdapter.portToInput(inObjects));
		this.tool.setSentinel(new KNIMESentinel(exec));
		FutureTask<B> futureTask = new FutureTask<>(this.tool);
		Thread t = new Thread(futureTask);
		t.start();
		B result = futureTask.get();
		PortObject[] out = this.knimeAdapter.outputToPort(result, exec); 
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
