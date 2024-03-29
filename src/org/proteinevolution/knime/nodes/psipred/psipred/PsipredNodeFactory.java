package org.proteinevolution.knime.nodes.psipred.psipred;

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
import org.proteinevolution.externaltools.tools.PsipredPsipred;
import org.proteinevolution.knime.KNIMEAdapter;
import org.proteinevolution.knime.nodes.base.ToolInvocationNodeFactory;
import org.proteinevolution.preferences.PreferencePage;

import com.genericworkflownodes.knime.base.data.port.FileStoreURIPortObject;

/**
 * <code>NodeFactory</code> for the "PSIPRED" Node.
 * 
 *
 * @author Lukas Zimmermann
 */
public final class PsipredNodeFactory extends ToolInvocationNodeFactory<Path, Path> {

	@Override
	protected ExternalToolInvocation<Path, Path> initTool() {

		try{
			return new PsipredPsipred(Paths.get(
					ProteinevolutionNodePlugin.getDefault().getPreferenceStore().getString(PreferencePage.PSIPRED_EXECUTABLE_PATH), "psipred").toFile());
		} catch(IOException e) {

			throw new IllegalStateException(e.getMessage());
		}
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
				
				FileStoreURIPortObject out = new FileStoreURIPortObject(exec.createFileStore("PsipredPsipredNode"));
		        File outFile = out.registerFile(PsipredNodeFactory.class.getSimpleName() + ".ss");
		        Files.copy(result, outFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
		        return new PortObject[] {out};
			}

			@Override
			public PortType[] getInputPortType() {
				
				return new PortType[] {IURIPortObject.TYPE};
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
