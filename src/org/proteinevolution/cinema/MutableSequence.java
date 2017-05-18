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
 * This software was written by Julian Selley (j.selley@man.ac.uk)
 * whilst at the University of Manchester as a Research Assistant. 
 *
 * The initial code base is copyright by the University of Manchester. 
 * Modifications to the initial code base are copyright of their 
 * respective authors, or their employers as appropriate. Authorship 
 * of the modifications may be determined from the ChangeLog placed 
 * at the end of this file
 */

package org.proteinevolution.cinema; // Package name inserted by JPack



/**
 * MutableSequence.java
 *
 * A marker class to present a sequence which can be changed without 
 * the necessity for generating a new sequence. By extending 
 * SequenceEventProvider, the ability to listen to the sequence has 
 * been added.
 *
 * Created: Fri Feb 25 20:07:16 2000
 *
 * @author J Selley
 * @version $Id: MutableSequence.java,v 1.3 2001/04/11 17:04:43 lord Exp $ 
 */

public interface MutableSequence extends Sequence, SequenceEventProvider
{

}// MutableSequence


/*
 * ChangeLog
 * $Log: MutableSequence.java,v $
 * Revision 1.3  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.2  2000/03/02 18:33:04  jns
 * by the addition of SequenceEventProvider, this class now becomes a marker
 * class for mutable sequences. The reason behind the creation of
 * SequenceEventProvider is that the methods in the MSA class require the
 * same names to access events of particular sequences.
 *
 * Revision 1.1.1.1  2000/02/26 16:51:33  jns
 * The repository for the bioinf java classes
 * 
 */

