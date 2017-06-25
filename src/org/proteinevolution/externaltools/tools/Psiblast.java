package org.proteinevolution.externaltools.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.proteinevolution.externaltools.base.CommandLine;
import org.proteinevolution.externaltools.parameters.DoubleBoundedParameter;
import org.proteinevolution.externaltools.parameters.IntegerBoundedParameter;
import org.proteinevolution.externaltools.parameters.PathParameter;
import org.proteinevolution.models.interfaces.ISequenceAlignment;

public final class Psiblast extends ExternalToolInvocation<ISequenceAlignment, Path> {

	public Psiblast(File executable) throws IOException {

		super(executable);
	}

	// Parameters
	public final PathParameter database = new PathParameter(Paths.get(""), "Database", "pal");
	public final DoubleBoundedParameter inclusion_etresh = 
			new DoubleBoundedParameter(0.001, 0.0, 1000.0, "Inclusion E-value threshold (-inclusion_ethresh)");
	public final IntegerBoundedParameter n_iterations = 
			new IntegerBoundedParameter(3, 1, 8, "Number of iterations");
	
	public final IntegerBoundedParameter n_alignments = 
			new IntegerBoundedParameter(250, 0, 5000, "Number of alignments");
	
	public final IntegerBoundedParameter n_descriptions =
			new IntegerBoundedParameter(250, 0, 5000, "Number of descriptions");
	
	
	@Override
	protected Path getResult(final CommandLine cmd, final File standardOut) {
		
		return cmd.getFile("-out_pssm").toPath();
	}

	@Override
	protected void setCmd(final CommandLine cmd) throws IOException {
				
		String db = this.database.get().toAbsolutePath().toString();
		cmd.addOption("-db", db.substring(0, db.length() - 4));
		cmd.addFile(this.input.getNumberSequences() == 1 ? "-query" : "-in_msa", this.input);
		
		cmd.addOption("-inclusion_ethresh", this.inclusion_etresh.get());
		cmd.addOption("-num_iterations", this.n_iterations.get());
		cmd.addOption("-num_alignments", this.n_alignments.get());
		cmd.addOption("-num_descriptions", this.n_descriptions.get());
		cmd.addOutputFile("-out_pssm", ".chk");	
	}
}
