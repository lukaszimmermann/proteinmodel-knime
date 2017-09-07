package org.proteinevolution.externaltools.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.proteinevolution.externaltools.base.CommandLine;
import org.proteinevolution.externaltools.parameters.Parameter;
import org.proteinevolution.externaltools.parameters.validators.RangeValidator;
import org.proteinevolution.models.interfaces.ISequenceAlignment;

public final class Psiblast extends ExternalToolInvocation<ISequenceAlignment, Path> {

	public Psiblast(File executable) throws IOException {

		super(executable);
	}
	
	// Parameters
	//public final PathParameter database = new PathParameter(Paths.get(""), "Database", "pal");
	public final Parameter<Double> inclusion_etresh = 
			new Parameter<Double>(0.001, "Inclusion E-value threshold (-inclusion_ethresh)", new RangeValidator<>(0.0, 1000.0));
	
	public final Parameter<Integer> n_iterations = 
			new Parameter<Integer>(3, "Number of iterations", new RangeValidator<>(1, 8));
	
	public final Parameter<Integer> n_alignments = 
			new Parameter<Integer>(250, "Number of alignments", new RangeValidator<>(0, 5000));
	
	public final Parameter<Integer> n_descriptions =
			new Parameter<Integer>(250, "Number of descriptions", new RangeValidator<>(0, 5000));
	
	
	@Override
	protected Path getResult(final CommandLine cmd, final File standardOut) {
		
		return cmd.getFile("-out_pssm").toPath();
	}

	@Override
	protected void setCmd(final CommandLine cmd) throws IOException {
				
		//String db = this.database.get().toAbsolutePath().toString();
		//cmd.addOption("-db", db.substring(0, db.length() - 4));
		cmd.addFile(this.input.getNumberSequences() == 1 ? "-query" : "-in_msa", this.input);
		
		cmd.addOption("-inclusion_ethresh", this.inclusion_etresh.get());
		cmd.addOption("-num_iterations", this.n_iterations.get());
		cmd.addOption("-num_alignments", this.n_alignments.get());
		cmd.addOption("-num_descriptions", this.n_descriptions.get());
		cmd.addOutputFile("-out_pssm", ".chk");	
	}
}
