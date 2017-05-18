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
import javax.swing.JSlider;
import javax.swing.plaf.SliderUI;
import javax.swing.UIManager;
import java.awt.Dimension;
import uk.ac.man.bioinf.sequence.alignment.SequenceAlignment;
import javax.swing.DefaultBoundedRangeModel;
import uk.ac.man.bioinf.sequence.alignment.event.AlignmentListener;
import uk.ac.man.bioinf.sequence.alignment.event.AlignmentEvent;
import uk.ac.man.bioinf.sequence.alignment.EmptySequenceAlignment;


/**
 * JAlignmentRuler.java
 *
 * 
 *
 * Created: Thu Mar 23 12:33:50 2000
 *
 * @author Phillip Lord
 * @version $Id: JAlignmentRuler.java,v 1.6 2001/04/11 17:04:42 lord Exp $
 */

public class JAlignmentRuler extends JSlider
{
  public static final String uiClassID = "AlignmentRulerUI";
  private int widthPerBase;
  protected int preferredHeight = 50;
  private ISequenceAlignment seq;
  
  public JAlignmentRuler()
  {
    this( EmptySequenceAlignment.getInstance() );
  }
  
  public JAlignmentRuler( ISequenceAlignment seq )
  {
    this( seq, JSlider.HORIZONTAL );
  }
  
  public JAlignmentRuler( ISequenceAlignment seq, int orientation )
  {
    this( seq, orientation, 1 );
  }
  
  public JAlignmentRuler( ISequenceAlignment seq, int orientation, int widthPerBase )
  {
    super();
    this.seq = seq;
    setModel( new SequenceAlignmentBoundRangeModelBridge( seq ) );
    setOrientation( orientation );
    calcDefaultLabelTable();
  }
  
  public void setSequenceAlignment( ISequenceAlignment msa )
  {
    seq = msa;
    setModel( new SequenceAlignmentBoundRangeModelBridge( msa ) );
    calcDefaultLabelTable();
    firePropertyChange( "sequenceAlignment", msa, seq );
  }

  private void calcDefaultLabelTable()
  {
    setMinorTickSpacing(2);
    setMajorTickSpacing(10);
    setPaintTicks(true);
    setPaintLabels(true);
    setLabelTable(createStandardLabels(10));
  }
  
  public ISequenceAlignment getSequenceAlignment()
  {
    return seq;
  }
  
  public Dimension getMinimumSize()
  {
    return getPreferredSize();
  }
  
  public Dimension getMaximumSize()
  {
    return getPreferredSize();
  }
  
  public int getPreferredHeight()
  {
    return preferredHeight;
  }
  
  public int getPreferredWidthPerBase()
  {
    return widthPerBase;
  }
  
  public void setPreferredWidthPerBase( int widthPerBase )
  {
    this.widthPerBase = widthPerBase;
  }
  
  public String getUIClassID()
  {
    return uiClassID;
  }
  
  public void setUI( SliderUI ui )
  {
    super.setUI( ui );
  }
  
  public void updateUI()
  {
    setUI( (SliderUI)UIManager.getUI( this ) );
  }
  
  class SequenceAlignmentBoundRangeModelBridge 
    extends DefaultBoundedRangeModel implements AlignmentListener

  {
    private ISequenceAlignment seq;
    
    public SequenceAlignmentBoundRangeModelBridge( ISequenceAlignment seq )
    {
      this( seq, 1, 0 );
    }
    
    public SequenceAlignmentBoundRangeModelBridge( ISequenceAlignment seq, int value, int extent )
    {
      // set the values of the super class. Starts at 1, ends at one
      // plus length
      super( value, extent, 1, seq.getLength() + 1 );
      this.seq = seq;
      seq.addAlignmentListener( this );
    }
    
    public ISequenceAlignment getSequenceAlignment()
    {
      return seq;
    }
    
    public void changeOccurred( AlignmentEvent event )
    {
      // (PENDING:- PL) This can be changed to check for specific
      // events and only signal when necessary. 
      super.setRangeProperties( 1, 0, 1, seq.getLength(), false );
      calcDefaultLabelTable();
    }
    
    public void setRangeProperties( int a, int b, int c, int d, boolean adjusting )
    {
      // I really really hate to do this but. 
      if( c != SequenceAlignmentBoundRangeModelBridge.this.getMinimum() || 
	  d != SequenceAlignmentBoundRangeModelBridge.this.getMaximum() ){
	throw new RuntimeException( "You reset the min and max of this model. Its fixed for ever and a day.");
      }
    }
    
    public void setMinimum( int a )
    {
      // I really really hate to do this but. 
      throw new RuntimeException( "You reset the min of this model. Its fixed for ever and a day.");
    }
    
    public void setMaximum( int b )
    {
      // I really really hate to do this but. 
      throw new RuntimeException( "You reset the max of this model. Its fixed for ever and a day.");
    }
    
  }
  
  static
  {
    // ensure that UI delegates are known to swing
    Class c = Install.class;
  }
} // JAlignmentRuler



/*
 * ChangeLog
 * $Log: JAlignmentRuler.java,v $
 * Revision 1.6  2001/04/11 17:04:42  lord
 * Added License agreements to all code
 *
 * Revision 1.5  2000/07/18 11:09:25  lord
 * Import rationalisation.
 *
 * Revision 1.4  2000/06/27 16:01:49  lord
 * Fixed bug in the event handling so that this class does really listen
 * to the alignment now.
 *
 * Revision 1.3  2000/04/20 14:17:05  lord
 * Fixed bug in model constructor.
 * Moved tick calculation to private method, as these need to be called
 * on resetting the MSA
 *
 * Revision 1.2  2000/04/19 17:21:06  lord
 * Put in default cons, put in standard tick marks, and added load of
 * install class
 *
 * Revision 1.1  2000/04/18 17:43:56  lord
 * All files moved from uk.ac.man.bioinf.viewer package
 *
 * Revision 1.4  2000/04/06 15:46:25  lord
 * Changes to support a cursor, with proper cursor movement
 *
 * Revision 1.3  2000/04/03 13:52:48  lord
 * Fixed bug with upper limit of model being one too small
 *
 * Revision 1.2  2000/03/29 14:52:42  lord
 * Now calculates preferred size. Has a preferred width per base
 * option.
 *
 * Revision 1.1  2000/03/27 18:59:04  lord
 * Initial checkin
 *
 */
