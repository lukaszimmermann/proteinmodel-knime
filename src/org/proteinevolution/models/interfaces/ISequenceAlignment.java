package org.proteinevolution.models.interfaces;


/**
 * ISequenceAlignment.java
 *
 * This interface represents multiple sequence alignments. Implementors are currently required to
 * offer the sequence as char arrays, which is subject to change (maybe CharSequence instead).
 * 
 * A sequence alignment as defined by this interface must adhere to the following characteristics:
 * 
 * <ul>
 * <li> All sequences have the same length, which is the length of the alignment.</li>
 * <li> Sequences are <em>ordered</em>, starting from the index 0.</li>
 * <li> The alignment must contain at least one sequence.</li>
 * <li> The interface does not make assumptions of the underlying alphabet</li>
 * </ul>
 *
 * @author Lukas Zimmermann
 */

public interface ISequenceAlignment {

  /**
   * Provides the sequence at a particular index in the alignment.
   * @param index The index of the sequence which should be returned (0-based).
   * @return      The sequence which is located at <code>index</code>.
   */
  public char[] getSequenceAt(final int index);
  
  /**
   * Returns the total number of sequences in the alignment.
   *
   * @return The number of sequences in the alignment.
   */
  public int getNumberSequences();

  /**
   * Returns the length of the alignment.
   *
   * @return The length of the alignment.
   */
  public int getLength();
} 
