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

import org.proteinevolution.models.interfaces.ISequenceAlignment;

/**
 * EmptySequenceAlignment.java
 *
 *
 * Created: Wed Apr 19 17:23:30 2000
 *
 * @author Phillip Lord
 * @version $Id: EmptySequenceAlignment.java,v 1.7 2001/04/11 17:04:43 lord Exp $
 */

public class EmptySequenceAlignment implements ISequenceAlignment
{
  private static final EmptySequenceAlignment instance = new EmptySequenceAlignment();
  
  public static EmptySequenceAlignment getInstance()
  {
    return instance;
  }
  
  private EmptySequenceAlignment()
  {
  }
  
  private NoSuchSequenceException createSequenceException( int index )
  {
    return new NoSuchSequenceException
      ( "Empty sequence does not do much", this, index );
  }
  
  public int getInset( int index )
  {
    throw createSequenceException( index );
  }
  
  public int getLength()
  {
    return 0;
  }
  
  public int getNumberSequences()
  {
    return 0;
  }
  
  public GappedSequence getSequenceAt( int index )
  {
    throw createSequenceException( index );
  }
  
  public int getSequenceIndex( GappedSequence seq )
  {
    return -1;
  }
      //setModel( new SequenceAlignmentBoundRangeModelBridge( seq ) );
  public SequenceType getSequenceType()
  {
    return EmptySequenceType.getInstance();
  }
  
  public ISequenceAlignment getSubAlignment( int a, int b, int c, int d )
  {
    if( a != 0 || b != 0 || c != 0 || d != 0 )
      throw createSequenceException( a );
    
    else return this;
  }
  
  public ISequenceAlignment getSubAlignment( SequenceAlignmentRectangle rect )
  {
    return getSubAlignment( rect.getX(), rect.getWidth(), rect.getY(), rect.getHeight() );
  }
  
  public void setInset(int seqIndex, int size) 
  {
    // no implementation necessary
  }
  
  public void setInsetQuietly( int seqIndex, int inset )
  {
    // no implementation necessary
  }

  public void addSequence(GappedSequence seq, int inset) 
  {
    // no implementation necessary
  }
  
  public GappedSequence removeSequence(int seqIndex) 
  {
    throw createSequenceException(seqIndex);
  }
  
  public void setLengthQuietly( int length )
  {
    // no implementation necessary
  }
  
  // we need no implementation here. There is only one instance and it
  // has a static reference to it. As this alignment can not change
  // there is no point keeping references to it. Worse still if we do
  // keep references it will keep the entire listener from GC'ing at
  // any point. 
  public void addAlignmentListener(AlignmentListener listener)
  {
  }
  
  public void removeAlignmentListener(AlignmentListener listener)
  {
  }
  
  public void addVetoableAlignmentListener(VetoableAlignmentListener listener)
  {
  }
  
  public void removeVetoableAlignmentListener(VetoableAlignmentListener listener)
  {
  }

  public void vetoableChangeOccurred( VetoableSequenceEvent event )
  {
  }
  
  public void changeOccurred( SequenceEvent event )
  {
  }
  
  public void addSequenceListener( SequenceListener listener )
  {
  }
  
  public void addVetoableSequenceListener( VetoableSequenceListener listener )
  {
  }
  
  public void removeSequenceListener( SequenceListener listener )
  {
  }
  
  public void removeVetoableSequenceListener( VetoableSequenceListener listener )
  {
  }
  
  // this is immutable so we can use a shared instance. 
  private static Identifier ident = new NoIdentifier();
  
  public Identifier getIdentifier()
  {
    return ident;
  }
} // EmptySequenceAlignment



/*
 * ChangeLog
 * $Log: EmptySequenceAlignment.java,v $
 * Revision 1.7  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.6  2001/01/23 17:58:57  lord
 * Added getSubAlignment( SequenceAlignmentRectangle ) method because I
 * thought it would be useful.
 *
 * Revision 1.5  2000/10/26 12:42:49  jns
 * o added editing facilities to SA - this includes insertion/deletion of gaps,
 * addition/removal of sequences from an alignment. It involved resolving some
 * conflicts with the group stuff.
 *
 * Revision 1.4  2000/09/15 17:24:01  lord
 * This class has a very nasty bug in it. It was extending from
 * AbstractSequenceAlignment. Which meant it was storing listeners that
 * were adding to it. The problem was that the instance has a static
 * reference to it (as it stored as a singleton). This was causing a bad
 * memory leak in my code, which took me two days to find.
 *
 * The irony is that I only extended from AbstractSequenceAlignment
 * because I wrote the code on a friday night and was being lazy, and I
 * only used the singleton because for reasons of efficiency. Combining
 * an optimisation and friday night code naturally produced an appalling
 * bug.
 *
 * Revision 1.3  2000/09/11 13:18:13  lord
 * Added identifier support
 *
 * Revision 1.2  2000/08/01 17:16:20  lord
 * Now returns EmptySequenceType rather than null
 *
 * Revision 1.1  2000/04/20 14:40:16  lord
 * Initial checkin
 *
 */
