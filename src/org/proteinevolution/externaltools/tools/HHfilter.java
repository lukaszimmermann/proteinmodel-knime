package org.proteinevolution.externaltools.tools;

import java.io.File;
import java.io.IOException;

import org.proteinevolution.externaltools.base.CommandLine;
import org.proteinevolution.externaltools.parameters.Parameter;
import org.proteinevolution.externaltools.parameters.validators.RangeValidator;
import org.proteinevolution.models.interfaces.Writeable;

public class HHfilter extends ExternalToolInvocation<Writeable[], File[]>{

	public HHfilter(File executable) throws IOException {
		super(executable);
	
	}
	
	public final Parameter<Double> max_pairwise_seq_identity =
			new Parameter<Double>(90.0, "Maximum pairwise sequence identity (%)", RangeValidator.percentage);
	
	public final Parameter<Integer> min_number_diverse_sequences = 
			new Parameter<>(0, "Min. number of diverse sequences", RangeValidator.natural);

	public final Parameter<Double> min_seq_identity_with_query = 
			new Parameter<>(0.0, "Minimum sequence identity with query (%)", RangeValidator.percentage);

	public final Parameter<Double> min_coverage_with_query = 
			new Parameter<>(0.0, "Minimum coverage with query (%)", RangeValidator.percentage);
	
	
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
