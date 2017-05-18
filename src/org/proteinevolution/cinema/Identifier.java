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
 * SequenceIdentifier.java
 *
 *
 * Created: Mon Jul  3 16:07:39 2000
 *
 * @author Phillip Lord
 * @version $Id: Identifier.java,v 1.3 2001/05/24 15:36:13 lord Exp $
 */

public interface Identifier 
{
  /**
   * This returns the source of the sequence. This might be a File
   * object if the sequence has been read straight from file, or a URL
   * from the internet. Alternatively it could be a Java Obect
   * reference if the sequence has been retrieved by calculation, say
   * of a Consensus. 
   * @return
   */
  public Source getSource();
  
  public void setSource( Source source );
  /**
   * Gets a human readable title for the Sequence
   * @return
   */
  public String getTitle();
} // SequenceIdentifier



/*
 * ChangeLog
 * $Log: Identifier.java,v $
 * Revision 1.3  2001/05/24 15:36:13  lord
 * The source is not mutable.
 *
 * Revision 1.2  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.1  2000/09/11 13:06:43  lord
 * Changed name of SequenceIdentifier, and SequenceSource to
 * Identifier and Source, which makes these a little bit more generic
 *
 * Revision 1.1  2000/07/18 12:38:31  lord
 * Initial checkin
 *
 */

