package org.proteinevolution.externaltools.tools;

import java.io.File;
import java.io.IOException;

import org.proteinevolution.externaltools.base.CommandLine;
import org.proteinevolution.externaltools.parameters.DoubleBoundedParameter;
import org.proteinevolution.externaltools.parameters.IntegerBoundedParameter;
import org.proteinevolution.models.interfaces.Writeable;

public final class HHblits extends ExternalToolInvocation<Writeable[], File[]> {

	public HHblits(final File executable) throws IOException {
		
		super(executable);
	}
	
	public final IntegerBoundedParameter number_of_iterations = new IntegerBoundedParameter(2, 0, 100, "Number of iteratons");
	public final DoubleBoundedParameter evalue = new DoubleBoundedParameter(0.001, 0.0, 1.0, "E-value cut-off");	
	public final DoubleBoundedParameter min_seqid_with_master = new DoubleBoundedParameter(0.0, 0.0, 100.0, "Minimum sequence identity with master (%)");	
	public final DoubleBoundedParameter min_coverage_with_master = new DoubleBoundedParameter(0.0, 0.0, 100.0, "Minimum coverage with master (%)");
	
	// HHsuite database
	//	public static final String HHSUITEDB_CFGKEY = "HHSUITEDB";
	//	public static final String[] HHSUITEDB_DEFAULT = new String[0];
	//	private final SettingsModelStringArray param_hhsuitedb = new SettingsModelStringArray(HHSUITEDB_CFGKEY, HHSUITEDB_DEFAULT);

	@Override
	protected File[] getResult(final CommandLine cmd, final File standardOut) {
		
		return new File[] {
				
				cmd.getFile("-o"),
				cmd.getFile("-oa3m")
		};
	}

	@Override
	protected void setCmd(final CommandLine cmd) throws IOException {
		
		// TODO Add database
		cmd.addFile("-i", this.input[0]);
		cmd.addOption("-n", this.number_of_iterations.get());
		cmd.addOption("-e", this.evalue.get());
		cmd.addOption("-qid", this.min_seqid_with_master.get());
		cmd.addOption("-cov", this.min_coverage_with_master.get());
		cmd.addOutputFile("-o");
		cmd.addOutputFile("-oa3m");
	}	
}
