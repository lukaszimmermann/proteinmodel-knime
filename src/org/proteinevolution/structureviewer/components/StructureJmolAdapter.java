package org.proteinevolution.structureviewer.components;

import java.util.Map;

import org.jmol.api.JmolAdapter;
import org.jmol.api.JmolAdapterAtomIterator;
import org.jmol.api.JmolAdapterBondIterator;
import org.jmol.api.JmolAdapterStructureIterator;
import org.jmol.api.JmolFilesReaderInterface;



public class StructureJmolAdapter extends JmolAdapter {

	@Override
	public boolean coordinatesAreFractional(Object atomSetCollection) {
		// TODO Auto-generated method stub
		
		return false;
	}

	@Override
	public void finish(Object atomSetCollection) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getAtomCount(Object atomSetCollection) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public JmolAdapterAtomIterator getAtomIterator(Object atomSetCollection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getAtomSetAuxiliaryInfo(Object atomSetCollection, int atomSetIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAtomSetCollection(Object atomSetCollectionReader) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<String, Object> getAtomSetCollectionAuxiliaryInfo(Object atomSetCollection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAtomSetCollectionFromDOM(Object DOMNode, Map<String, Object> htParams) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAtomSetCollectionFromReader(String fname, Object reader, Map<String, Object> htParams)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAtomSetCollectionFromSet(Object readers, Object atomSets, Map<String, Object> htParams) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAtomSetCollectionName(Object atomSetCollection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAtomSetCollectionReader(String name, String type, Object bufferedReader,
			Map<String, Object> htParams) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getAtomSetCollectionReaders(JmolFilesReaderInterface fileReader, String[] names, String[] types,
			Map<String, Object> htParams, boolean getReadersOnly) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getAtomSetCount(Object atomSetCollection) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getAtomSetName(Object atomSetCollection, int atomSetIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getAtomSetNumber(Object atomSetCollection, int atomSetIndex) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public JmolAdapterBondIterator getBondIterator(Object atomSetCollection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[][] getBondList(Object atomSetCollection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFileTypeName(Object atomSetCollection) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getHydrogenAtomCount(Object atomSetCollection) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public JmolAdapterStructureIterator getStructureIterator(Object atomSetCollection) {
		// TODO Auto-generated method stub
		return null;
	}

		
	
	
}
