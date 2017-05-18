/*
 *This library is free software; you can redistribute it and/or
 *modify it under the terms of the GNU Lesser General Public
 *License as published by the Free Software Foundation; either
 *version 2.1 of the License, or (at your option) any later version.
 *
 *This library is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *Lesser General Public License for more details.
 *
 *You should have received a copy of the GNU Lesser General Public
 *License along with this library; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

/* 
 * This software was written by Phillip Lord (p.lord@hgmp.mrc.ac.uk)
 * whilst at the University of Manchester as a Pfizer post-doctoral 
 * Research Fellow. 
 *
 * The initial code base is copyright by Pfizer, or the University
 * of Manchester. Modifications to the initial code base are copyright
 * of their respective authors, or their employers as appropriate. 
 * Authorship of the modifications may be determined from the ChangeLog
 * placed at the end of this file
 */

package org.proteinevolution.cinema; // Package name inserted by JPack


/**
 * AlignmentSelectionModel.java
 *
 * This defines the selection model for the JAlignmentViewer. The main
 * three methods of this class, namely stopSelection(),
 * extendSelection() and clearSelection() are called by the
 * JAlignmentViewer, or one of its helper classes and usually
 * considered to be advisory. The model is free to ignore these method
 * calls if it is appropriate behaviour. 
 *
 * Created: Mon Apr 10 11:32:26 2000
 *
 * @author Phillip Lord
 * @version $Id: AlignmentSelectionModel.java,v 1.4 2001/04/11 17:04:42 lord Exp $
 */

public interface AlignmentSelectionModel {
  /**
   * is the current selection in the process of being selected.
   * @return true is selection is changing
   */
  public boolean isSelecting();

  /**
   * Returns the current selection or null if there is no
   * selection. If there is more than one selection this should return
   * the most recently made selection
   * @return the selection
   */
  public SequenceAlignmentRectangle getCurrentSelection();
  
  /**
   * Get the number of selections.
   * @return the number of selections
   */
  public int getNumberSelections();
  
  /**
   * Gets the given selection. The index should reflect the
   * chronological order in which the selections were made, with the
   * smallest index first
   * @param index the index
   * @return the selection array index at the given index
   * @exception ArrayIndexOutOfBoundsException if the index is out of bounds
   */
  public SequenceAlignmentRectangle getSelectionAt( int index ) throws IndexOutOfBoundsException;
  
  /**
   * Is the point within one of the selection rectangles
   * @param point the point
   * @return true if point is within a rectangle
   */
  public boolean isPointSelected( SequenceAlignmentPoint point );
  
  /**
   * Clear all the selections in this model
   */
  public void clearSelection();
  
  /**
   * Get the first selection rectangle which surrounds the point, or
   * null if there is not one.
   * @param point the point in question
   * @return the rectangle surrounding point
   */
  public SequenceAlignmentRectangle getRectangleAtPoint( SequenceAlignmentPoint point );
   
  /**
   * Get all selection rectangles which surround this point, or null
   * if there is not one
   * @param point the point in question
   * @return the rectangles surrounding point
   */
  public SequenceAlignmentRectangle[] getRectanglesAtPoint( SequenceAlignmentPoint point );
  
  /**
   * Start a selection at the given point
   * @param point the start of the selection
   */
  public void extendSelection( SequenceAlignmentPoint point );
  
  /**
   * Complete the selection at the given point
   * @param point the point to complete the selection
   */
  public void stopSelection( SequenceAlignmentPoint point );
  
  public void addAlignmentSelectionListener( AlignmentSelectionListener listener );
  
  public void removeAlignmentSelectionListener( AlignmentSelectionListener listener );
  
} // AlignmentSelectionModel



/*
 * ChangeLog
 * $Log: AlignmentSelectionModel.java,v $
 * Revision 1.4  2001/04/11 17:04:42  lord
 * Added License agreements to all code
 *
 * Revision 1.3  2001/01/15 18:54:21  lord
 * Changed exception type to less specific one.
 * Removed one method which seems extraneous.
 *
 * Revision 1.2  2001/01/04 14:55:56  lord
 * Extended documentation
 *
 * Revision 1.1  2000/04/18 17:43:55  lord
 * All files moved from uk.ac.man.bioinf.viewer package
 *
 * Revision 1.1  2000/04/11 16:56:23  lord
 * Initial version. It compiles, but that does not mean it works
 *
 */
