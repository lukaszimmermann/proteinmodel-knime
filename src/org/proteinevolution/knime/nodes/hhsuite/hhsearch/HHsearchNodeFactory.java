package org.proteinevolution.knime.nodes.hhsuite.hhsearch;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortType;
import org.proteinevolution.ProteinevolutionNodePlugin;
import org.proteinevolution.externaltools.tools.ExternalToolInvocation;
import org.proteinevolution.externaltools.tools.HHsearch;
import org.proteinevolution.knime.KNIMEAdapter;
import org.proteinevolution.knime.nodes.base.ToolInvocationNodeFactory;
import org.proteinevolution.knime.nodes.hhsuite.HHSuiteUtil;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentPortObject;
import org.proteinevolution.models.interfaces.Writeable;
import org.proteinevolution.preferences.PreferencePage;

/**
 * <code>NodeFactory</code> for the "HHsearch" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public final class HHsearchNodeFactory extends ToolInvocationNodeFactory<Writeable[], File[]> {

	@Override
	protected ExternalToolInvocation<Writeable[], File[]> initTool() {

		try{
			return new HHsearch(Paths.get(
					ProteinevolutionNodePlugin.getDefault().getPreferenceStore().getString(PreferencePage.HHSUITE_EXECUTABLE_PATH), "hhsearch").toFile());
		} catch(IOException e) {

			throw new IllegalStateException(e.getMessage());
		}
	}

	@Override
	protected KNIMEAdapter<Writeable[], File[]> getAdapter() {

		return new KNIMEAdapter<Writeable[], File[]>() {

			@Override
			public Writeable[] portToInput(PortObject[] ports) {

				return new Writeable[] {

						((SequenceAlignmentPortObject) ports[0]).getAlignment()
				};
			}

			@Override
			public PortObject[] outputToPort(File[] result, ExecutionContext exec) throws IOException {

				return new PortObject[]{ HHSuiteUtil.getHHR(result[0], exec) };
			}

			@Override
			public PortType[] getInputPortType() {

				return new PortType[] {SequenceAlignmentPortObject.TYPE};
			}

			@Override
			public PortType[] getOutputPortType() {

				return new PortType[] {BufferedDataTable.TYPE};
			}
		};
	}

	@Override
	protected void check() throws IllegalStateException {

	}	
}