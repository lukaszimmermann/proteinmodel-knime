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
 * VetoableSequenceEvent.java
 *
 *
 * Created: Fri Feb 18 17:38:50 2000
 *
 * @author Phillip Lord
 * @version $Id: VetoableSequenceEvent.java,v 1.3 2001/04/11 17:04:43 lord Exp $
 */

public class VetoableSequenceEvent extends SequenceEvent {

	private static final long serialVersionUID = 670304992610790445L;

	public VetoableSequenceEvent( Object src, int location, SequenceEventType type ) {

		super( src, location, type );
	}

	public VetoableSequenceEvent( Object src, int start, int length, SequenceEventType type ) {
		super( src, start, length, type );
	}

} // VetoableSequenceEvent



/*
 * ChangeLog
 * $Log: VetoableSequenceEvent.java,v $
 * Revision 1.3  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.2  2000/03/08 17:26:36  lord
 * To many changes to document
 *
 * Revision 1.1  2000/02/26 18:28:45  jns
 * Initial coding of file
 *
 *
 */
