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
 * Sequences.java
 *
 *
 * Created: Tue Mar 14 13:43:59 2000
 *
 * @author Phillip Lord
 * @version $Id: Sequences.java,v 1.15 2001/04/11 17:04:43 lord Exp $
 */

public class Sequences
{
  private Sequences()
  {
    //no objects!!
  }

  /**
   * Returns a GappedSequence with the Elements in the array, which
   * can included gaps. All other elements must be contained in the
   * SequenceType however.
   * @param elements the elements
   * @param type the sequence type
   * @return the sequence
   */
  public static GappedSequence getElementsAsGappedSequence( Element[] elements, SequenceType type )
  {
    return DefaultGappedSequence.getElementsAsGappedSequence( elements, type );
  }

  /**

   * Returns a GappedSequence with the Elements in the array, which
   * can included gaps. All other elements must be contained in the
   * SequenceType however.
   *
   * @param elements the elements
   * @param type the sequence type
   * @param iface the biointerface
   * @return the sequence

   */
  public static GappedSequence getElementsAsGappedSequence
    (Element[] elements, SequenceType type, Identifier ident )
  {
    return DefaultGappedSequence.getElementsAsGappedSequence(elements, type, ident );
  }

  public static Sequence getElementsAsSequence( Element[] elements, SequenceType type )
  {
    return new DefaultSequence( elements, type );
  }
  
  public static Sequence getElementsAsSequence
    ( Element[] elements, SequenceType type, Identifier ident )
  {
    return new DefaultSequence( elements, type, ident );
  }
  
  /**
   * This removes all of the leading gaps from a gapped sequence, and
   * then returns the number of gaps removed. 
   * @param seq the sequence to chomp
   * @return the number of gaps
   */
  public static int chompLeadingGaps( GappedSequence seq ) throws SequenceVetoException
  {
    int i = 0;
    // (PENDING:- PL) Quick and dirty implementation. Should be
    // possible to do this with a single method invocation.
    while( seq.getLength() > 1 && seq.getGappedElementAt( 1 ) == Gap.GAP ){
      seq.deleteGapAt( 1 );
      i++;
    }

    return i;
  }

  /**
   * Removes and returns the number of trailing gaps off a gapped
   * sequence.
   *
   * @param seq the sequence
   * @return the number of gaps
   */
  public static int chompTrailingGaps(GappedSequence seq) 
    throws SequenceVetoException 
  {
    int numberGapsRemoved = 0;
    
    while (seq.getGappedElementAt(seq.getGappedLength()) == Gap.GAP) {
      seq.deleteGapAt(seq.getGappedLength());
      numberGapsRemoved++;
    }
    
    return numberGapsRemoved;
  }

  /**
   * Returns the number of gaps upstream from a given index in the
   * given sequence. If the index is out of bounds, it returns zero.
   * @param index the location in the sequence to start counting
   * upstream from
   * @param seq the sequence
   * @return the number of gaps upstream for the given location
   */
  public static int getNumberGapsUpstreamFrom(int index, GappedSequence seq) 
  {
    // N.B. Sequence numbers start at 1 *NOT* 0
    if ((index < 1) || (index > seq.getGappedLength()))
      return 0;
    
    int numberGaps = 0;
    // while we are still in the sequence, move from index upstream
    // until we meet a non-gap element, incrementing the number of
    // gaps
    while ((numberGaps + index < seq.getGappedLength()) && 
	   (seq.getGappedElementAt(numberGaps + index) == Gap.gap))
      numberGaps++;
    
    // return the calculated number of gaps
    return numberGaps;
  }
  
  /**
   * Returns the number of gaps downstream from a given index in the
   * given sequence. If the index is out of bounds, it returns zero.
   * @param index the location in the sequence to start counting
   * downstream from
   * @param seq the sequence
   * @return the number of gaps downstream for the given location
   */
  public static int getNumberGapsDownstreamFrom(int index, GappedSequence seq) 
  {
    // N.B. Sequence numbers start at 1 *NOT* 0
    if ((index < 1) || (index > seq.getGappedLength()))
      return 0;
    
    int numberGaps = 0;
    // while we are still in the sequence, move from index, downstream
    // until we meet a non-gap element, incrementing the number of
    // gaps
    while ((index - numberGaps > 1) && 
	   (seq.getGappedElementAt(index - numberGaps) == Gap.gap))
      numberGaps++;
    
    // return the calculated number of gaps
    return numberGaps;
  }    

  public static String getSequenceAsString( Sequence seq )
  {
    return String.valueOf( seq.getSequenceAsChars() );
  }

  public static String getSubSequenceAsString( Sequence seq, int start, int length )
  {
    return getSequenceAsString( seq.getSubSequence( start, length ) );
  }
  
  public static String getGappedSequenceAsString( GappedSequence seq )
  {
    return String.valueOf( seq.getGappedSequenceAsChars() );
  }

  public static String getGappedSubSequenceAsString( GappedSequence seq, int start, int length )
  {
    return getGappedSequenceAsString( seq.getGappedSubSequence( start, length ) );
  }
  
  public static String getElementsAsString( Element[] elem )
  {
    char[] chrs = new char[ elem.length ];
    for( int i = 0; i < chrs.length; i++ ){
      chrs[ i ] = elem[ i ].toChar();
    }
    return String.valueOf( chrs );
  }

  public static String getComplexSequenceAsString( ComplexSequence seq )
  {
    return String.valueOf( seq.getComplexSequenceAsChars() );
  }

  public static ComplexElement[] getResiduesAsComplexElements( Residue[] elements )
  {
    ComplexElement[] complex = new ComplexElement[ elements.length ];
    
    for( int i = 0; i < elements.length; i++ ){
      complex[ i ] = ComplexElementFactory.getComplexElement( elements[ i ] );
    }

    return complex;  
  }
  
  
  public static void printAlignment( ISequenceAlignment align )
  {
    System.out.print( "Alignment " + "Size: " + align.getNumberSequences() + " Length: " + align.getLength() );
    System.out.println();


    for( int i = 1; i < align.getNumberSequences() + 1; i++ ){
      GappedSequence seq = align.getSequenceAt( i );
      
      System.out.println( "Sequence " + i + ": " + seq.getIdentifier().getTitle() + " Length: " + seq.getGappedLength() );
      for( int j = 0; j < align.getInset( i ); j++ ){
	System.out.print( "+" );
      }

      printSequence( seq );
    }
  }

  public static void printSequence( GappedSequence seq )
  {
    System.out.println( getGappedSequenceAsString( seq ) );
  }

  public static void printSequence( ComplexSequence seq )
  {
    System.out.println( getComplexSequenceAsString( seq ) );
  }

  public static void printSequence( GappedComplexSequence seq )
  {
    printSequence( (GappedSequence)seq );
    printSequence( (ComplexSequence)seq );
  }
  
  public static void printSequence( Sequence seq )
  {
    System.out.println( getSequenceAsString( seq ) );
  }

  public static void printElements( Element[] elem )
  {
    System.out.println( getElementsAsString( elem ) );
  }
} // Sequences



/*
 * ChangeLog
 * $Log: Sequences.java,v $
 * Revision 1.15  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.14  2001/02/19 17:46:49  lord
 * Some new methods
 *
 * Revision 1.13  2000/12/13 16:33:55  lord
 * ComplexElement and ComplexSequence support methods
 *
 * Revision 1.12  2000/11/27 18:18:19  lord
 * Added methods to print ComplexSequences
 *
 * Revision 1.11  2000/11/02 14:52:08  jns
 * o added functions to calculate the gaps from a specific gapped
 * locaiton, upstream and downstream.
 *
 * Revision 1.10  2000/09/11 13:09:48  lord
 * SequenceIdentifier renamed to Identifier
 *
 * Revision 1.9  2000/08/01 12:47:05  jns
 * o removed references to BioInterface and BioObject.
 *
 * Revision 1.8  2000/07/18 12:32:52  lord
 * Import rationalisation
 * Changes due to BioInterface removal
 *
 * Revision 1.7  2000/06/22 16:02:26  jns
 * o added a chompTrailingGaps function to complement the chompLeadingGaps
 * function.
 *
 * Revision 1.6  2000/06/13 10:59:46  lord
 * Now more descriptive when printing out
 *
 * Revision 1.5  2000/06/08 12:34:54  jns
 * o added method to get a gapped sequence as elements *with* the biointerface
 * supplied. It is merely over-riding the previous method which didn't offer
 * addition of the biointerface - even though it exists in the default gapped
 * sequence.
 *
 * Revision 1.4  2000/06/05 14:24:06  lord
 * Added print alignment method
 *
 * Revision 1.3  2000/05/30 16:04:18  lord
 * Have rationalised and sorted all the import statements
 *
 * Revision 1.2  2000/05/18 17:04:48  lord
 * A couple of new methods
 *
 * Revision 1.1  2000/03/14 15:11:46  lord
 * Initial checkin
 *
 */
