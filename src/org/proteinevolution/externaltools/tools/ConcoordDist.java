package org.proteinevolution.externaltools.tools;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.proteinevolution.externaltools.base.CommandLine;
import org.proteinevolution.externaltools.parameters.Parameter;
import org.proteinevolution.externaltools.parameters.StringSelectionParameter;
import org.proteinevolution.externaltools.parameters.validators.RangeValidator;
import org.proteinevolution.models.interfaces.Writeable;

public class ConcoordDist extends ExternalToolInvocation<Writeable[], File[]> {

	// Static Members
	private static final Map<String, String> atomsMarginsParam;
	private static final Map<String, String> bondsParam;
	static {
		atomsMarginsParam = new LinkedHashMap<String, String>(6);
		atomsMarginsParam.put("OPLS-UA (united atoms)", "oplsua");
		atomsMarginsParam.put("OPLS-AA (all atoms)", "oplsaa");
		atomsMarginsParam.put("PROLSQ repel", "repel");
		atomsMarginsParam.put("Yamber2", "yamber2");
		atomsMarginsParam.put("Li et al.", "li");
		atomsMarginsParam.put("OPLS-X", "oplsx");

		bondsParam = new LinkedHashMap<String, String>(2);
		bondsParam.put("Concoord default", ".noeh");
		bondsParam.put("Engh-Huber", "");
	}

	public ConcoordDist(final Path concoordPath) throws IOException {

		
		super(concoordPath.resolve(Paths.get("bin", "dist.exe")).toFile());
				
		this.addEnvKeyValue(new EnvKeyValue() {
			
			private Path tempLib = null;
			
			@Override
			public void close() throws Exception {

				if (this.tempLib != null) {
					
					ExternalToolInvocation.deleteRecursively(this.tempLib);
				}
			}

			@Override
			public String getKey() {

				return "CONCOORDLIB";
			}

			@Override
			public String getValue() throws IOException {

				// Copy lib directory to a temporary location (because some files need to be renamed there)
				this.tempLib = Files.createTempDirectory(String.valueOf(ThreadLocalRandom.current().nextInt()));

				ExternalToolInvocation.copyRecursively(concoordPath.resolve("lib"), tempLib);

				String atomMarginSuffix = atomsMarginsParam.get(ConcoordDist.this.atoms_margin.get());

				Files.copy(this.tempLib.resolve(String.format("ATOMS_%s.DAT", atomMarginSuffix)),
						this.tempLib.resolve("ATOMS.DAT"), StandardCopyOption.REPLACE_EXISTING);

				Files.copy(this.tempLib.resolve(String.format("MARGINS_%s.DAT",atomMarginSuffix)),
						this.tempLib.resolve("MARGINS.DAT"), StandardCopyOption.REPLACE_EXISTING);

				Files.copy(this.tempLib.resolve(String.format("BONDS.DAT%s", bondsParam.get(ConcoordDist.this.bonds.get()))),
						this.tempLib.resolve("BONDS.DAT"), StandardCopyOption.REPLACE_EXISTING);

				return this.tempLib.toAbsolutePath().toFile().getCanonicalPath();
			}
		});
	}

	// Parameters
	public final StringSelectionParameter atoms_margin = new StringSelectionParameter("OPLS-UA (united atoms)", atomsMarginsParam, "Van-der-Waals parameters");
	public final StringSelectionParameter bonds = new StringSelectionParameter("Concoord default", bondsParam, "bond/angle parameters");
	public final Parameter<Boolean> retain_hydrogen_atoms = new Parameter<>(false, "Retain hydrogen atoms");
	public final Parameter<Boolean> fix_zero_occ = new Parameter<>(false, "Interpret zero occupancy as atom to keep fixed");
	public final Parameter<Boolean> find_alternative_contacts = new Parameter<>(false, "Try to find alternatives for non-bonded interactions (by default the native contacts will be preserved). Warning: EXPERIMENTAL!");
	public final Parameter<Double> cut_off_radius = new Parameter<>(4.0, "Cut-off radius (Angstroms) for non-bonded interacting pairs (default 4.0)", new RangeValidator<>(0.0, 20.0));
	public final Parameter<Integer> min_dist = new Parameter<>(50, "Minimum nr of distances to be defined for each atom (default 50, or 1 with -noe)", new RangeValidator<>(1, 200));
	public final Parameter<Double> damp = new Parameter<>(1.0, "Multiply each distance margin by value (default 1.0)", new RangeValidator<>(1.0, 10.0));


	@Override
	public File[] getResult(final CommandLine cmd, final File standardOut) {

		File[] result = new File[2];
		result[0] = cmd.getFile("-op");
		result[1] = cmd.getFile("-od");
		return result;
	}

	@Override
	protected void setCmd(final CommandLine cmd) throws IOException {
		
		cmd.addFlag("-r", this.retain_hydrogen_atoms.get());
		cmd.addFlag("-nb", this.find_alternative_contacts.get());
		cmd.addFlag("-q", this.fix_zero_occ.get());
		cmd.addOption("-c", this.cut_off_radius.get());
		cmd.addOption("-m", this.min_dist.get());
		cmd.addOption("-damp", this.damp.get());
		cmd.addOutputFile("-od", ".dat");
		cmd.addOutputFile("-op", ".pdb");
	
		cmd.addFile("-p", this.input[0]);
		/*
		if (this.input.length > 1 && this.input[1] != null) {

			cmd.addFile("-noe", input[1]);
		}
		*/
	}
}
