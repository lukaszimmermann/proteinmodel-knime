package org.proteinevolution.externaltools.tools;

import java.io.File;
import java.io.IOException;

import org.proteinevolution.externaltools.base.CommandLine;
import org.proteinevolution.externaltools.parameters.DoubleBoundedParameter;
import org.proteinevolution.models.interfaces.Writeable;

public class HHsearch extends ExternalToolInvocation<Writeable[], File[]> {

	public HHsearch(final File executable) throws IOException {
		super(executable);
	}


	public final DoubleBoundedParameter evalue = new DoubleBoundedParameter(0.001, 0.0, 1.0, "E-value cut-off");
	public final DoubleBoundedParameter min_seq_identity_with_master = 
			new DoubleBoundedParameter(0.0, 0.0, 100.0, "Minimum sequence identity with master sequence (%)");
	public final DoubleBoundedParameter min_coverage_with_master = 
			new DoubleBoundedParameter(0.0, 0.0, 100.0, "Minimum coverage with master sequence (%)");



	// HHsuite database
	//public static final String HHSUITEDB_CFGKEY = "HHSUITEDB";
	//public static final String[] HHSUITEDB_DEFAULT = new String[0];
	//private final SettingsModelStringArray param_hhsuitedb = new SettingsModelStringArray(HHSUITEDB_CFGKEY, HHSUITEDB_DEFAULT);


	@Override
	protected File[] getResult(final CommandLine cmd, final File standardOut) {

		return new File[] {

				cmd.getFile("-o"),
				cmd.getFile("-oa3m")
		};
	}

	@Override
	protected void setCmd(final CommandLine cmd) throws IOException {
		
		cmd.addFile("-i", this.input[0]);
		cmd.addOption("-e", this.evalue.get());
		cmd.addOption("-qid", this.min_seq_identity_with_master.get());
		cmd.addOption("-cov", this.min_coverage_with_master.get());
		cmd.addOutputFile("-o");
		cmd.addOutputFile("-oa3m");
	}
}
