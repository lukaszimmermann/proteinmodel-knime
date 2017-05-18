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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.UIManager;
import javax.swing.LookAndFeel;


/**
 * Install.java
 *
 * This class provides the install information for the all of the
 * classes in the viewer package. It is needed because a Swing
 * component discovers its UIDelegate by a run time class lookup. The
 * standard Swing classes get this information from the UIManager
 * which in turn gets them from the LookAndFeel classes. Since we cant
 * over ride these this is managed here. At the moment the different
 * look and feels are not supported (which is to stay that the look
 * and feel remains the same always!). To get this class to work, just
 * load it, by calling Install.class.
 *
 * Created: Wed Mar 15 17:03:43 2000
 *
 * @author Phillip Lord
 * @version $Id: Install.java,v 1.3 2001/04/11 17:04:42 lord Exp $
 */

public class Install  implements PropertyChangeListener 
{
  private static final Install instance = new Install();
  static
  {
    instance.installBasicLookAndFeel();
    UIManager.addPropertyChangeListener( instance );
  }
  
  private LookAndFeel currentLAF;
  
  public void installBasicLookAndFeel()
  {
    // link the JAlignmentViewer to its UIDelegate...
    UIManager.put( JAlignmentViewer.uiClassID, BasicAlignmentViewerUI.class.getName() );    
    UIManager.put( JAlignmentRuler.uiClassID, BasicAlignmentRulerUI.class.getName() );    
  }
  
  public void propertyChange( PropertyChangeEvent pce ) 
  {
    if( pce.getPropertyName().equals( "lookAndFeel" ) &&
	currentLAF != pce.getNewValue() ){
      
      // currently we are not supporting the different look and feels
      // as none of the classes here have an equivalent on the
      // different operating systems. Otherwise we would call
      // different methods here for the different OS's
      if( currentLAF == null ){
	installBasicLookAndFeel();
	currentLAF = UIManager.getLookAndFeel();
      }
    }
  }
} // Install



/*
 * ChangeLog
 * $Log: Install.java,v $
 * Revision 1.3  2001/04/11 17:04:42  lord
 * Added License agreements to all code
 *
 * Revision 1.2  2000/12/18 12:13:05  jns
 * o getting rid of system.out.println to avoid noisy output out of debug
 * mode
 *
 * Revision 1.1  2000/04/18 17:43:56  lord
 * All files moved from uk.ac.man.bioinf.viewer package
 *
 * Revision 1.2  2000/03/27 18:49:11  lord
 * Support for JAlignmentRuler
 *
 * Revision 1.1  2000/03/16 16:19:20  lord
 * Initial checkin
 *
 */






