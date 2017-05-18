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
 * ReadonlyException.java
 *
 *
 * Created: Thu Dec 14 15:40:59 2000
 *
 * @author Julian Selley
 * @version $Id: ReadonlyException.java,v 1.3 2001/04/11 17:04:42 lord Exp $
 */

public class ReadonlyException extends RuntimeException
{
	private static final long serialVersionUID = 7137770228216771558L;
	private JAlignmentViewer viewer = null; // the alignment viewer causing the exception

	public ReadonlyException()
	{
		super("This component is read-only.");
	}

	public ReadonlyException(String message) 
	{
		super(message);
	}

	public ReadonlyException(JAlignmentViewer jav) 
	{
		super("This alignment viewer is in a read-only state.");
		this.viewer = jav;
	}

	public ReadonlyException(String message, JAlignmentViewer jav) 
	{
		super(message);
		this.viewer = jav;
	}

	/**
	 * Returns the <code>JAlignmentViewer</code> that caused the
	 * exception, or null if the exception came from elsewhere.
	 * @see JAlignmentViewer
	 *
	 * @return the alignment viewer
	 */
	public JAlignmentViewer getViewer() 
	{
		return this.viewer;
	}
} // ReadonlyException



/*
 * ChangeLog
 * $Log: ReadonlyException.java,v $
 * Revision 1.3  2001/04/11 17:04:42  lord
 * Added License agreements to all code
 *
 * Revision 1.2  2001/01/04 14:26:13  jns
 * o Bug fixing: had moved ReadonlyException to a new location, but
 * forgot to deal with the package statements, etc.
 *
 * Revision 1.1  2000/12/20 16:45:01  jns
 * o initial code
 *
 */
