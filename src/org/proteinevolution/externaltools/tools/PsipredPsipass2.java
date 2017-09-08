package org.proteinevolution.externaltools.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.proteinevolution.externaltools.base.CommandLine;
import org.proteinevolution.externaltools.parameters.Parameter;
import org.proteinevolution.externaltools.parameters.validators.RangeValidator;

public final class PsipredPsipass2 extends ExternalToolInvocation<Path, Path[]> {

	private final File dataDir;
	
	public PsipredPsipass2(final File executable) throws IOException {
		super(executable);
		this.dataDir = new File(executable.getParentFile().getParent(), "data");
	}
	

	final Parameter<Integer> number_iterations = new Parameter<>(1, "Number of iterations", RangeValidator.natural);
	final Parameter<Double> dca = new Parameter<>(1.0, "DCA");
	final Parameter<Double> dcb = new Parameter<>(1.0, "DCB");	
	
	
	@Override
	protected Path[] getResult(final CommandLine cmd, final File standardOut) {
	
		return new Path[] {
			
				cmd.getFile(0).toPath().toAbsolutePath(),
				standardOut.toPath().toAbsolutePath()	
		};	
	}

	@Override
	protected void setCmd(final CommandLine cmd) throws IOException {
		
		cmd.addOption("", new File(this.dataDir, "weights_p2.dat").getAbsolutePath());
		cmd.addOption("", this.number_iterations.get());
		cmd.addOption("", this.dca.get());
		cmd.addOption("", this.dcb.get());
		cmd.addOutputFile("");
		
		// TODO HACK
		cmd.appendFileToOutput(this.input.toAbsolutePath().toFile());
	}
}
