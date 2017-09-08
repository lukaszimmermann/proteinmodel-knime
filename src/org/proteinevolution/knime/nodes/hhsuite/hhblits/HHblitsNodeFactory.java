package org.proteinevolution.knime.nodes.hhsuite.hhblits;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.knime.core.data.uri.IURIPortObject;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortType;
import org.proteinevolution.ProteinevolutionNodePlugin;
import org.proteinevolution.externaltools.tools.ExternalToolInvocation;
import org.proteinevolution.externaltools.tools.HHblits;
import org.proteinevolution.knime.KNIMEAdapter;
import org.proteinevolution.knime.nodes.base.ToolInvocationNodeFactory;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentPortObject;
import org.proteinevolution.models.interfaces.Writeable;
import org.proteinevolution.preferences.PreferencePage;

import com.genericworkflownodes.knime.base.data.port.FileStoreURIPortObject;


/**
 * <code>NodeFactory</code> for the "HHblits" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public final class HHblitsNodeFactory extends ToolInvocationNodeFactory<Writeable[], File[]> {


	@Override
	protected ExternalToolInvocation<Writeable[], File[]> initTool() {

		try{
			return new HHblits(
					Paths.get(ProteinevolutionNodePlugin.getDefault().getPreferenceStore().getString(PreferencePage.HHSUITE_EXECUTABLE_PATH),
					"hhblits").toFile());

		} catch(IOException e) {

			throw new IllegalStateException(e);
		}
	}

	@Override
	protected KNIMEAdapter<Writeable[], File[]> getAdapter() {

		return new KNIMEAdapter<Writeable[], File[]>() {

			@Override
			public PortObject[] outputToPort(File[] result, ExecutionContext exec) throws IOException {

				// File to sequence Alignment (TODO HHblits does currently not compute alignment files)
				//SequenceAlignmentContent sequenceAlignmentOut = SequenceAlignmentContent.fromFASTA(result[1].getAbsolutePath());
				//AlignmentFormat sequenceAlignmentOutFormat = sequenceAlignmentOut.getAlignmentFormat();	
				final FileStoreURIPortObject out = new FileStoreURIPortObject(exec.createFileStore("HHblitsHHblitsNode"));
				final File outFile =  out.registerFile(HHblitsNodeFactory.class.getSimpleName() + ".fasta");
				Files.copy(result[1].toPath().toAbsolutePath(), outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
				return new PortObject[] {out};
			}

			@Override
			public Writeable[] portToInput(PortObject[] ports) {

				return new Writeable[] {

						((SequenceAlignmentPortObject) ports[0]).getAlignment()
				};
			}

			@Override
			public PortType[] getOutputPortType() {

				return new PortType[] {IURIPortObject.TYPE};
			}

			@Override
			public PortType[] getInputPortType() {

				return new PortType[] {SequenceAlignmentPortObject.TYPE};
			}
		};
	}
}
