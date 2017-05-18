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
 * VetoableAlignmentEvent.java
 *
 * An event that may be vetoed by a listener.
 *
 * Created: Fri Feb 18 17:41:04 2000
 *
 * @author Phillip Lord
 * @version $Id: VetoableAlignmentEvent.java,v 1.2 2001/04/11 17:04:43 lord Exp $
 */

public class VetoableAlignmentEvent extends AlignmentEvent {	
	
	private static final long serialVersionUID = -5073163177676887565L;

	public VetoableAlignmentEvent(Object src, int location, 
			AlignmentEventType type)
	{
		super( src, location, type );
	}

	public VetoableAlignmentEvent(Object src, int start, int end, 
			AlignmentEventType type)
	{
		super( src, start, end, type );
	}

} // VetoableAlignmentEvent



/*
 * ChangeLog
 * $Log: VetoableAlignmentEvent.java,v $
 * Revision 1.2  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.1  2000/03/02 19:23:34  jns
 * initial code.
 *
 *
 */
