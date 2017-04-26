package org.proteinevolution.models.knime;

import java.util.HashSet;
import java.util.Set;

import org.knime.core.node.NodeLogger;
import org.knime.core.node.defaultnodesettings.DialogComponent;
import org.knime.core.node.defaultnodesettings.DialogComponentStringListSelection;
import org.knime.core.node.defaultnodesettings.DialogComponentStringSelection;

/**
 * Allows to keep track of all chains that are part of a PDB file
 * 
 * @author lzimmermann
 *
 */
public class PDBChainStringSelection implements LineParser {

    // the logger instance
    private static final NodeLogger logger = NodeLogger
            .getLogger(PDBChainStringSelection.class);

	private final DialogComponent dialogComponent;
	private final Set<String> chains;

	public PDBChainStringSelection(DialogComponent dialogComponent) {
		
		if (    ! (dialogComponent instanceof DialogComponentStringSelection)
			 && ! (dialogComponent instanceof DialogComponentStringListSelection)) {
			
			throw new IllegalArgumentException("DialogComponent must be either DialogComponentStringSelection or DialogComponentStringListSelection");
		}
		this.dialogComponent = dialogComponent;
		this.chains = new HashSet<String>();
	}


	@Override
	public void parse(String line) {

		// Only consider ATOM lines
		if (line.startsWith("ATOM")) {

			// Fetch the chain
			
			String chain = line.substring(21, 22);
			if( ! " ".equals(chain)) {
				this.chains.add(chain);
			}
		}
	}

	@Override
	public void finish() {
		
		if (this.dialogComponent instanceof DialogComponentStringSelection) {
			
			((DialogComponentStringSelection) this.dialogComponent).replaceListItems(this.chains, null);
			
		} else if(this.dialogComponent instanceof DialogComponentStringListSelection) {
			
			((DialogComponentStringListSelection) this.dialogComponent).replaceListItems(this.chains, (String[]) null);
		}
		this.chains.clear();
	}
}
