package org.proteinevolution.externaltools.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.proteinevolution.externaltools.base.CommandLine;

public class PsipredChkparse extends ExternalToolInvocation<Path, Path> {

	public PsipredChkparse(File executable) throws IOException {
		
		super(executable);
	}

	@Override
	protected Path getResult(final CommandLine cmd, final File standardOut) {
		
		return standardOut.toPath();
	}

	@Override
	protected void setCmd(final CommandLine cmd) throws IOException {
		
		cmd.addFile("", this.input.toFile());
	}
}
