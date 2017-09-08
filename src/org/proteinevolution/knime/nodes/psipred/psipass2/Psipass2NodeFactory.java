package org.proteinevolution.knime.nodes.psipred.psipass2;

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
import org.proteinevolution.externaltools.tools.PsipredPsipass2;
import org.proteinevolution.knime.KNIMEAdapter;
import org.proteinevolution.knime.nodes.base.ToolInvocationNodeFactory;
import org.proteinevolution.preferences.PreferencePage;

import com.genericworkflownodes.knime.base.data.port.FileStoreURIPortObject;

/**
 * <code>NodeFactory</code> for the "Psipass2" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public final class Psipass2NodeFactory extends ToolInvocationNodeFactory<Path, Path[]> {

	@Override
	protected ExternalToolInvocation<Path, Path[]> initTool() throws IOException {
		
		return new PsipredPsipass2(Paths.get(
					ProteinevolutionNodePlugin
					.getDefault()
					.getPreferenceStore()
					.getString(PreferencePage.PSIPRED_EXECUTABLE_PATH), "psipass2").toFile());
	}

	@Override
	protected KNIMEAdapter<Path, Path[]> getAdapter() {
		
		return new KNIMEAdapter<Path, Path[]>() {

			@Override
			public Path portToInput(final PortObject[] ports) {
				
				return Paths.get(((FileStoreURIPortObject) ports[0]).getURIContents().get(0).getURI());
			}

			@Override
			public PortObject[] outputToPort(Path[] result, ExecutionContext exec) throws IOException {
				
				final FileStoreURIPortObject outSS2Port = new FileStoreURIPortObject(exec.createFileStore("PsipredPsipass2NodeSS2"));
				final FileStoreURIPortObject outHorizPort = new FileStoreURIPortObject(exec.createFileStore("PsipredPsipass2NodeHoriz"));
				
				final File outSS2 = outSS2Port.registerFile(Psipass2NodeFactory.class.getSimpleName() + ".ss2");
				final File outHoriz = outHorizPort.registerFile(Psipass2NodeFactory.class.getSimpleName() + ".horiz");
				Files.copy(result[0], outSS2.toPath(), StandardCopyOption.REPLACE_EXISTING);
				Files.copy(result[1], outHoriz.toPath(), StandardCopyOption.REPLACE_EXISTING);
				return new PortObject[] {outSS2Port, outHorizPort};
			}

			@Override
			public PortType[] getInputPortType() {
				
				return new PortType[] {IURIPortObject.TYPE}; 
			}

			@Override
			public PortType[] getOutputPortType() {
				
				return new PortType[] {IURIPortObject.TYPE, IURIPortObject.TYPE};
			}
		};
	}
}

