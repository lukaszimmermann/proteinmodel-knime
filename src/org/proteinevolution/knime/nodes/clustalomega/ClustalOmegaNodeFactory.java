package org.proteinevolution.knime.nodes.clustalomega;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.knime.core.data.uri.IURIPortObject;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortType;
import org.proteinevolution.ProteinevolutionNodePlugin;
import org.proteinevolution.externaltools.tools.ClustalOmega;
import org.proteinevolution.externaltools.tools.ExternalToolInvocation;
import org.proteinevolution.knime.KNIMEAdapter;
import org.proteinevolution.knime.nodes.base.ToolInvocationNodeFactory;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentContent;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentPortObject;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentPortObjectSpec;
import org.proteinevolution.models.spec.AlignmentFormat;
import org.proteinevolution.preferences.PreferencePage;

import com.genericworkflownodes.knime.base.data.port.FileStoreURIPortObject;

/**
 * <code>NodeFactory</code> for the "ClustalOmega" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public final class ClustalOmegaNodeFactory 
        extends ToolInvocationNodeFactory<Path, Path> {

	@Override
	protected ExternalToolInvocation<Path, Path> initTool() throws IOException {
	
		return new ClustalOmega(Paths.get(
					ProteinevolutionNodePlugin
					.getDefault()
					.getPreferenceStore()
					.getString(PreferencePage.CLUSTALO_EXECUTABLE)).toFile());
	}

	@Override
	protected KNIMEAdapter<Path, Path> getAdapter() {
		
		return new KNIMEAdapter<Path, Path>() {

			@Override
			public Path portToInput(PortObject[] ports) {
				
				return Paths.get(((FileStoreURIPortObject) ports[0]).getURIContents().get(0).getURI());
			}

			@Override
			public PortObject[] outputToPort(final Path result, final ExecutionContext exec) throws IOException {
				
				final SequenceAlignmentContent sequenceAlignmentOut = SequenceAlignmentContent.fromFASTA(result.toAbsolutePath().toString());
				final AlignmentFormat sequenceAlignmentOutFormat = sequenceAlignmentOut.getAlignmentFormat();	
				return new PortObject[]{
						new SequenceAlignmentPortObject(
								sequenceAlignmentOut,
								new SequenceAlignmentPortObjectSpec(SequenceAlignmentContent.TYPE, sequenceAlignmentOutFormat))
				};
			}

			@Override
			public PortType[] getOutputPortType() {

				return new PortType[] {SequenceAlignmentPortObject.TYPE};
			}

			@Override
			public PortType[] getInputPortType() {

				return new PortType[] {IURIPortObject.TYPE};
			}
		};
	}
}

