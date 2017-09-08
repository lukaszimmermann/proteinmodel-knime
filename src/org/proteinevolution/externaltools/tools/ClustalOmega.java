package org.proteinevolution.externaltools.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.proteinevolution.externaltools.base.CommandLine;
import org.proteinevolution.externaltools.parameters.Parameter;
import org.proteinevolution.externaltools.parameters.validators.RangeValidator;

public final class ClustalOmega extends ExternalToolInvocation<Path, Path> {

	public ClustalOmega(File executable) throws IOException {
		super(executable);
	}

	public final Parameter<Integer> threads = new Parameter<>(1, "No. of threads to use", RangeValidator.natural);
	public final Parameter<Boolean> dealign = new Parameter<>(false, "Dealign input sequences");
	
		
	@Override
	protected Path getResult(CommandLine cmd, File standardOut) {
		
		return cmd.getFile(0).toPath().toAbsolutePath();
	}

	@Override
	protected void setCmd(final CommandLine cmd) throws IOException {
		
		cmd.addFlag("--force");
		cmd.addFile("-i", this.input.toFile());
		cmd.addFlag("--dealign", this.dealign.get());
		cmd.addOutputFile("-o", "fasta");
	}	
}
