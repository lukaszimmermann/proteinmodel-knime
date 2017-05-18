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
 * AlignmentVetoExcception.java
 *
 * An exception thrown when an object vetos an alignment event.
 *
 * Created: Tue Feb 15 21:16:35 2000
 *
 * @author J Selley
 * @version $Id: AlignmentVetoException.java,v 1.3 2001/04/11 17:04:43 lord Exp $
 */

public class AlignmentVetoException extends Exception {
	
	private static final long serialVersionUID = -3942569080832351583L;
	private AlignmentEvent event;

	/**
	 * Constructor; includes reference to the event which spawned the veto.
	 */
	public AlignmentVetoException(String message, AlignmentEvent event)
	{
		super( message );
		this.event = event;
	}

	/**
	 * Returns the multiple sequence event that spawned the veto.
	 *
	 * @return the event
	 */
	public AlignmentEvent getEvent()
	{
		return event;
	}
} // AlignmentVetoExcception



/*
 * ChangeLog
 * $Log: AlignmentVetoException.java,v $
 * Revision 1.3  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.2  2000/11/09 16:42:13  lord
 * Package update
 *
 * Revision 1.1  2000/03/02 19:23:21  jns
 * initial code.
 *
 *
 */
