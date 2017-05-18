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
 * NoSuchSequenceException.java
 *
 * Alerts to an invalid sequence index within a sequence alignment.
 *
 * Created: Fri Mar  3 12:22:15 2000
 *
 * @author Julian Selley
 * @version $Id: NoSuchSequenceException.java,v 1.2 2001/04/11 17:04:43 lord Exp $
 */

public class NoSuchSequenceException extends RuntimeException
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3769243046807399372L;

	public NoSuchSequenceException()
	{
		super();
	}

	public NoSuchSequenceException( String message )
	{
		super( message );
	}

	public NoSuchSequenceException( ISequenceAlignment msa, int index )
	{
		super( "Attempt to access sequence " + index + " of alignment " + msa + " which does not exist" );
	}

	public NoSuchSequenceException( String message, ISequenceAlignment msa, int index )
	{
		super( message + ": " + msa + " at "  + index );
	}
} // NoSuchSequenceException



/*
 * ChangeLog
 * $Log: NoSuchSequenceException.java,v $
 * Revision 1.2  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.1  2000/03/10 12:11:44  jns
 * initial code.
 *
 *
 */
