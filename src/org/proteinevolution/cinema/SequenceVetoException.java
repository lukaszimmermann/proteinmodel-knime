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



// TODO Probably also useless

/**
 * SequenceVetoException.java
 *
 * An exception thrown when an object vetos a sequence change event.
 *
 * Created: Tue Feb 15 21:08:31 2000
 *
 * @author J Selley
 * @version $Id: SequenceVetoException.java,v 1.4 2001/04/11 17:04:43 lord Exp $
 */

public class SequenceVetoException extends Exception {

	private static final long serialVersionUID = 6450966490123975481L;

	private SequenceEvent event;

	/**
	 * The constructor; storing the event as well as source
	 */
	public SequenceVetoException( String message, SequenceEvent event )
	{
		super( message );
		this.event = event;
	}

	/**
	 * Returns the sequence event that the veto objected to
	 * @return the sequence event
	 */
	public SequenceEvent getEvent() {
		return event;
	}
} // SequenceVetoException



/*
 * ChangeLog
 * $Log: SequenceVetoException.java,v $
 * Revision 1.4  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.3  2000/11/09 16:42:13  lord
 * Package update
 *
 * Revision 1.2  2000/02/26 18:43:36  jns
 * wrong package name for SequenceEvent corrected.
 *
 * Revision 1.1  2000/02/26 18:28:45  jns
 * Initial coding of file
 *
 *
 */
