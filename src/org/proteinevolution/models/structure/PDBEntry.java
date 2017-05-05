package org.proteinevolution.models.structure;

import java.util.HashMap;
import java.util.Map;

public final class PDBEntry {

	// No direct instantiation
	private PDBEntry(String pdbid) {
		
		this.pdbid = pdbid;
	}
	private static Map<String, PDBEntry> entries = new HashMap<String, PDBEntry>();
	
	
	private final String pdbid;
	
	public String getID() {
		
		return this.pdbid;
	}
	
	
	public static PDBEntry of(String pdbid) {
		
		// TODO Validate that the pdbid is valid
		
		if (entries.containsKey(pdbid)) {
			
			return entries.get(pdbid);
		}
		PDBEntry newEntry = new PDBEntry(pdbid);
		entries.put(pdbid, newEntry);
		return newEntry;
	}
}
