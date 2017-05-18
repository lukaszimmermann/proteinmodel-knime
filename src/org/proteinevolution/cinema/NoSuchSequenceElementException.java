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

package uk.ac.man.bioinf.sequence; // Package name inserted by JPack


/**
 * NoSuchSequenceElementException.java
 *
 *
 * Created: Fri Mar  3 12:22:15 2000
 *
 * @author Phillip Lord
 * @version $Id: NoSuchSequenceElementException.java,v 1.2 2001/04/11 17:04:43 lord Exp $
 */

public class NoSuchSequenceElementException extends RuntimeException
{
  public NoSuchSequenceElementException()
  {
    super();
  }
  
  public NoSuchSequenceElementException( String message )
  {
    super( message );
  }
  
  public NoSuchSequenceElementException( Sequence seq, int index )
  {
    super( "Attempt to access element " + index + " of sequence " + seq + " which does not exist" );
  }
  
  public NoSuchSequenceElementException( String message, Sequence seq, int index )
  {
    super( message + ": " + seq + " at "  + index );
  }
} // NoSuchSequenceElementException



/*
 * ChangeLog
 * $Log: NoSuchSequenceElementException.java,v $
 * Revision 1.2  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.1  2000/03/08 17:26:35  lord
 * To many changes to document
 *
 */
