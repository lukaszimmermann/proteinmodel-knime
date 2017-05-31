package org.proteinevolution.models.interfaces;


public interface ISequenceAlignmentAnnotated extends ISequenceAlignment {
	
	public char[] getAnnotationAt(final int index);
	
	public int getNumAnnotations();
} 
