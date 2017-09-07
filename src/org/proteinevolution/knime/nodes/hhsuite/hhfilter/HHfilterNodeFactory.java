package org.proteinevolution.knime.nodes.hhsuite.hhfilter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.knime.core.node.ExecutionContext;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortType;
import org.proteinevolution.ProteinevolutionNodePlugin;
import org.proteinevolution.externaltools.tools.ExternalToolInvocation;
import org.proteinevolution.externaltools.tools.HHfilter;
import org.proteinevolution.knime.KNIMEAdapter;
import org.proteinevolution.knime.nodes.ToolInvocationNodeFactory;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentContent;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentPortObject;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentPortObjectSpec;
import org.proteinevolution.models.interfaces.Writeable;
import org.proteinevolution.models.spec.AlignmentFormat;
import org.proteinevolution.preferences.PreferencePage;

/**
 * <code>NodeFactory</code> for the "HHfilter" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class HHfilterNodeFactory  extends ToolInvocationNodeFactory<Writeable[], File[]> {

	@Override
	protected ExternalToolInvocation<Writeable[], File[]> initTool() {

		try{
			return new HHfilter(Paths.get(
					ProteinevolutionNodePlugin.getDefault().getPreferenceStore().getString(PreferencePage.HHSUITE_EXECUTABLE_PATH), "hhfilter").toFile());
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

				SequenceAlignmentContent sequenceAlignmentOut = SequenceAlignmentContent.fromFASTA(result[0].getAbsolutePath());
				AlignmentFormat sequenceAlignmentOutFormat = sequenceAlignmentOut.getAlignmentFormat();	

				return new PortObject[]{
						new SequenceAlignmentPortObject(
								sequenceAlignmentOut,
								new SequenceAlignmentPortObjectSpec(SequenceAlignmentContent.TYPE, sequenceAlignmentOutFormat))
				};
			}

			@Override
			public PortType[] getInputPortType() {

				return new PortType[] {SequenceAlignmentPortObject.TYPE};
			}

			@Override
			public PortType[] getOutputPortType() {
				return new PortType[] {SequenceAlignmentPortObject.TYPE};
			}
		};
	}

	@Override
	protected void check() throws IllegalStateException {
	}
}
