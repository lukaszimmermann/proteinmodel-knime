package org.proteinevolution.externaltools.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.proteinevolution.externaltools.base.CommandLine;

public final class PsipredPsipred extends ExternalToolInvocation<Path, Path> {

	private final File dataDir;
	
	public PsipredPsipred(final File executable) throws IOException {
		super(executable);
		this.dataDir = new File(executable.getParentFile().getParent(), "data");
	}

	@Override
	protected Path getResult(final CommandLine cmd, final File standardOut) {
		
		return standardOut.toPath();
	}

	@Override
	protected void setCmd(final CommandLine cmd) throws IOException {
		
		cmd.addOption("", this.input.toFile().getAbsolutePath());;
		
		// add weights
		cmd.addOption("", new File(this.dataDir, "weights.dat").getAbsolutePath());
		cmd.addOption("", new File(this.dataDir, "weights.dat2").getAbsolutePath());
		cmd.addOption("", new File(this.dataDir, "weights.dat3").getAbsolutePath());	
	}	
}
