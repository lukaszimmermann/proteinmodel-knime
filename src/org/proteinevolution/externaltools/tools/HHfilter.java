package org.proteinevolution.externaltools.tools;

import java.io.File;
import java.io.IOException;

import org.proteinevolution.externaltools.base.CommandLine;
import org.proteinevolution.externaltools.parameters.DoubleBoundedParameter;
import org.proteinevolution.externaltools.parameters.IntegerBoundedParameter;
import org.proteinevolution.models.interfaces.Writeable;

public class HHfilter extends ExternalToolInvocation<Writeable[], File[]>{

	public HHfilter(File executable) throws IOException {
		super(executable);
	
	}
	
	public final DoubleBoundedParameter max_pairwise_seq_identity =
			new DoubleBoundedParameter(90.0, 0.0, 100.0, "Maximum pairwise sequence identity (%)");
	
	public final IntegerBoundedParameter min_number_diverse_sequences = 
			new IntegerBoundedParameter(0, 0, 10000, "Min. number of diverse sequences");

	public final DoubleBoundedParameter min_seq_identity_with_query = 
			new DoubleBoundedParameter(0.0, 0.0, 100.0, "Minimum sequence identity with query (%)");

	public final DoubleBoundedParameter min_coverage_with_query = 
			new DoubleBoundedParameter(0.0, 0.0, 100.0, "Minimum coverage with query (%)");
	
	
	@Override
	protected File[] getResult(final CommandLine cmd, final File standardOut) {
		
		return new File[] {
			cmd.getFile("-o")
		};
	}

	@Override
	protected void setCmd(final CommandLine cmd) throws IOException {
		
		cmd.addFile("-i", this.input[0]);
		cmd.addOption("-qid", this.min_seq_identity_with_query.get());
		cmd.addOption("-cov", this.min_coverage_with_query.get());
		cmd.addOption("-id", this.max_pairwise_seq_identity.get());
		cmd.addOption("-diff", this.min_number_diverse_sequences.get());
		cmd.addOutputFile("-o");
	}	
}
