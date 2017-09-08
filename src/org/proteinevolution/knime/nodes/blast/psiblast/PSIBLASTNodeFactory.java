package org.proteinevolution.knime.nodes.blast.psiblast;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.knime.core.data.uri.IURIPortObject;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.port.PortObject;
import org.knime.core.node.port.PortType;
import org.proteinevolution.ProteinevolutionNodePlugin;
import org.proteinevolution.externaltools.tools.ExternalToolInvocation;
import org.proteinevolution.externaltools.tools.Psiblast;
import org.proteinevolution.knime.KNIMEAdapter;
import org.proteinevolution.knime.nodes.base.ToolInvocationNodeFactory;
import org.proteinevolution.knime.porttypes.alignment.SequenceAlignmentPortObject;
import org.proteinevolution.models.interfaces.ISequenceAlignment;
import org.proteinevolution.preferences.PreferencePage;

import com.genericworkflownodes.knime.base.data.port.FileStoreURIPortObject;

/**
 * <code>NodeFactory</code> for the "PSIBLAST" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public class PSIBLASTNodeFactory  extends ToolInvocationNodeFactory<ISequenceAlignment, Path>{

	@Override
	protected ExternalToolInvocation<ISequenceAlignment, Path> initTool() {

		try{
			return new Psiblast(Paths.get(
					ProteinevolutionNodePlugin.getDefault().getPreferenceStore().getString(PreferencePage.BLAST_EXECUTABLE_PATH), "psiblast").toFile());
		} catch(IOException e) {

			throw new IllegalStateException(e.getMessage());
		}
	}

	@Override
	protected KNIMEAdapter<ISequenceAlignment, Path> getAdapter() {


		return new KNIMEAdapter<ISequenceAlignment, Path>() {

			@Override
			public ISequenceAlignment portToInput(final PortObject[] ports) {

				return ((SequenceAlignmentPortObject) ports[0]).getAlignment();
			}

			@Override
			public PortObject[] outputToPort(final Path result, final ExecutionContext exec) throws IOException {

				FileStoreURIPortObject out = new FileStoreURIPortObject(exec.createFileStore("PsiblastNodeFactory"));
		        File outFile = out.registerFile(PSIBLASTNodeFactory.class.getSimpleName() + ".chk");
		        Files.copy(result, outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		        return new PortObject[] {out};
			}

			@Override
			public PortType[] getInputPortType() {

				return new PortType[] {SequenceAlignmentPortObject.TYPE};
			}

			@Override
			public PortType[] getOutputPortType() {

				return new PortType[] {IURIPortObject.TYPE};
			}
		};
	}

	@Override
	protected void check() throws IllegalStateException {
	}
}
