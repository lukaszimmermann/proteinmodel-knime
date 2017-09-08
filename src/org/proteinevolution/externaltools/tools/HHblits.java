package org.proteinevolution.externaltools.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.proteinevolution.externaltools.base.CommandLine;
import org.proteinevolution.externaltools.parameters.validators.RangeValidator;
import org.proteinevolution.models.interfaces.Writeable;
import org.proteinevolution.externaltools.parameters.Parameter;

public final class HHblits extends ExternalToolInvocation<Writeable[], File[]> {

	public HHblits(final File executable) throws IOException {
		
		super(executable);
	}
	
	public final Parameter<Integer> cpus = new Parameter<>(2, "CPUs to use");
	public final Parameter<Integer> number_of_iterations = new Parameter<>(2, "Number of iteratons", new RangeValidator<>(0, 100));
	public final Parameter<Double> evalue = new Parameter<>(0.001, "E-value inclusion cut-off", RangeValidator.probability);	
	public final Parameter<Double> min_seqid_with_master = new Parameter<>(0.0, "Minimum sequence identity with master (%)", new RangeValidator<>(0.0, 100.0));	
	public final Parameter<Double> min_coverage_with_master = new Parameter<>(0.0, "Minimum coverage with master (%)", new RangeValidator<>(0.0, 100.0));
	//public final Parameter<Integer> num_target_sequences = new Parameter<>(250, "Number of target sequences", RangeValidator.natural);
	
	
	public final Parameter<Path> database = new Parameter<Path>(Paths.get(""), "Database path", (path) -> {
		
		// Database file prefix, terminated by '_' or '.'
		final String filename = path.getFileName().toString().trim();
		if ("".equals(filename)) {
			
			return Paths.get(filename);
		}
		int idx2 = filename.indexOf('.');
		idx2 = idx2 == -1 ? Integer.MAX_VALUE : idx2;
		return path.getParent().resolve(filename.substring(0, idx2));
	});
	
	// HHsuite database
	//	public static final String HHSUITEDB_CFGKEY = "HHSUITEDB";
	//	public static final String[] HHSUITEDB_DEFAULT = new String[0];
	//	private final SettingsModelStringArray param_hhsuitedb = new SettingsModelStringArray(HHSUITEDB_CFGKEY, HHSUITEDB_DEFAULT);

	@Override
	protected File[] getResult(final CommandLine cmd, final File standardOut) {
	
		return new File[] {
				
				cmd.getFile(0),
				cmd.getFile(1)
		};
	}

	@Override
	protected void setCmd(final CommandLine cmd) throws IOException {
		
		cmd.addOption("-cpu", this.cpus.get());
		cmd.addOption("-d", this.database.get().toAbsolutePath().toString());
		cmd.addFile("-i", this.input[0]);
		cmd.addOption("-n", this.number_of_iterations.get());
		cmd.addOption("-e", this.evalue.get());
		cmd.addOption("-qid", this.min_seqid_with_master.get());
		cmd.addOption("-cov", this.min_coverage_with_master.get());
		cmd.addOutputFile("-o");
		cmd.addOutputFile("-oa3m");
	}	
}
