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

package org.proteinevolution.cinema; // Package name inserted by JPack

/**
 * VetoableSequenceListener.java
 *
 * When a sequence wishes to change it should signal listeners of this
 * type first before it makes the change. If none of these listeners
 * veto the change, then it make occur after which listeners to type
 * SequenceChangeListener (including all of the
 * VetoableSequenceListeners ) should be signalled, at which
 * stage the change is unvetoable. Because there are two types of
 * listener it is not necessary to resignal the 
 * VetoableSequenceListener's if a change is latter veto'd
 *
 * Created: Tue Feb 15 19:28:56 2000
 *
 * @author Phillip Lord
 * @version $Id: VetoableSequenceListener.java,v 1.4 2001/04/11 17:04:43 lord Exp $ 
 */

public interface VetoableSequenceListener extends SequenceListener
{
  /**
   * Method signalled before a prospective change occurs
   * @param event the event
   * @exception SequenceVetoException if the listener does not wish
   * the event to occur. 
   */
  public void vetoableChangeOccurred( VetoableSequenceEvent event ) 
    throws SequenceVetoException;
  
}// VetoableSequenceListener


/*
 * ChangeLog
 * $Log: VetoableSequenceListener.java,v $
 * Revision 1.4  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.3  2000/06/27 16:10:55  lord
 * Removed backtick from the comments. The new sentance is now
 * ungrammatical but does not confuse Emacs' sexp matching algorithm
 *
 * Revision 1.2  2000/03/08 17:26:36  lord
 * To many changes to document
 *
 * Revision 1.1  2000/02/26 18:28:45  jns
 * Initial coding of file
 *
 *
 */





