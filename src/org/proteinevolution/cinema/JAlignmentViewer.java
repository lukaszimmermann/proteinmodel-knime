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
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeListener;


/**
 * JAlignmentViewer.java
 *
 *
 * Created: Tue Mar 14 16:28:59 2000
 *
 * @author Phillip Lord
 * @version $Id: JAlignmentViewer.java,v 1.28 2002/03/08 14:53:57 lord Exp $
 */

public class JAlignmentViewer extends JComponent {

	private static final long serialVersionUID = -3204831648550371466L;
	public static final String uiClassID = "AlignmentViewerUI";
	
	
	// The actual sequence alignment
	private ISequenceAlignment alignment;
	
	// TODO
	// (PENDING:- PL) this default is not really acceptable. It should be
	// changed to something more sensible, using the preferred size of
	// the CellRenderer
	
	private Dimension prototypicalCellSize = new Dimension( 30, 30 );
	private AlignmentViewerCellRenderer renderer;
	private FastAlignmentViewerCellRenderer fastRenderer = new DefaultFastAlignmentViewerCellRenderer();
	
	private AlignmentSelectionRenderer selectionRenderer = new DefaultAlignmentSelectionRenderer();
	
	// TODO Replace by something less verbose
	private SequenceCursor cursor;
		
	private AlignmentViewerUI viewerUI;
	private AlignmentSelectionModel selectionModel;
	private ColorMap colorMap;


	public JAlignmentViewer( ISequenceAlignment alignment )
	{
		super();
		// (PENDING:- PL) We probably need to install listeners here to
		// this SequenceAlignment. Should we allow it to change outside? I
		// think so...    
		this.alignment = alignment;
		this.cursor = new DefaultSequenceCursor();
		this.colorMap = new SingleColorMap(getBackground());

		this.selectionModel = new SingleAlignmentSelectionModel();
		// bit surprising here, but this is NOT called by the super class cons
		updateUI();
	}

	// rendering properties
	public void setCellRenderer( AlignmentViewerCellRenderer renderer )
	{
		AlignmentViewerCellRenderer old = this.renderer;
		this.renderer = renderer;
		firePropertyChange( "cellRenderer", old, renderer );
	}

	public AlignmentViewerCellRenderer getCellRenderer()
	{
		return renderer;
	}

	public void setFastCellRenderer( FastAlignmentViewerCellRenderer renderer )
	{
		FastAlignmentViewerCellRenderer old = this.fastRenderer;
		this.fastRenderer = renderer;
		firePropertyChange("fastCellRenderer", old, renderer);
	}

	public FastAlignmentViewerCellRenderer getFastCellRenderer()
	{
		return fastRenderer;
	}

	public void setAlignmentSelectionRenderer( AlignmentSelectionRenderer selectionRenderer )
	{
		AlignmentSelectionRenderer old = this.selectionRenderer;
		this.selectionRenderer = selectionRenderer;
		firePropertyChange( "alignmentSelectionRenderer", old, selectionRenderer );
	}

	public AlignmentSelectionRenderer getAlignmentSelectionRenderer()
	{
		return selectionRenderer;
	}

	public void setCellHeight( int height )
	{
		int old = prototypicalCellSize.height;
		prototypicalCellSize.height =  height;

		firePropertyChange( "cellHeight", old, height );
	}

	public int getCellHeight()
	{
		return prototypicalCellSize.height;
	}

	public void setCellWidth( int width )
	{
		int old = prototypicalCellSize.width;
		prototypicalCellSize.width = width;
		firePropertyChange( "cellWidth", old, width );
	}

	public int getCellWidth()
	{
		return prototypicalCellSize.width;
	}


	// sizing methods
	private Dimension preferredSize = new Dimension( 0, 0 );
	public Dimension getPreferredSize()
	{
		// (PENDING:- PL) This does not need to be necessarily need to
		// recalculated every time. 
		preferredSize.setSize
		// (PENDING:- PL) Have to put in code to account of Ruler
		// size. Delegate this to UI?? 

		// Leave a little gap at the edge here so that we can see all of
		// the elements
		(	alignment.getLength() * getCellWidth(),
				alignment.getNumberSequences() * getCellHeight() );

		//    return preferredSize;
		return preferredSize;
	}

	public Dimension getMinimumSize()
	{
		return getPreferredSize();
	}

	public Dimension getMaximumSize()
	{
		return getPreferredSize();
	}

	// color methods and delegates
	/**
	 * Changes the color map and notifies of change.
	 *
	 * @param colorMap the new color map
	 */
	public void setColorMap(ColorMap colorMap) 
	{
		if( colorMap == null ) colorMap = new SingleColorMap( getBackground() );

		ColorMap old = this.colorMap;
		this.colorMap = colorMap;
		firePropertyChange( "colorMap", old, colorMap);
		repaint();
	}

	/**
	 * Returns the current color map.
	 *
	 * @return the color map
	 */
	public ColorMap getColorMap() 
	{
		return this.colorMap;
	}

	// cursor methods and delegates
	public void setCursorModel( SequenceCursor cursor )
	{
		SequenceCursor old = this.cursor;
		this.cursor = cursor;
		firePropertyChange( "sequenceCursor", old, cursor );
	}

	public SequenceCursor getCursorModel()
	{
		return cursor;
	}

	public void addCursorChangeListener( ChangeListener listener )
	{
		cursor.addChangeListener( listener );
	}

	public void removeCursorChangeListener( ChangeListener listener )
	{
		cursor.removeChangeListener( listener );
	}

	public void setPoint( SequenceAlignmentPoint point )
	{
		// (PENDING:- PL) Need to think clearly about what to in the case
		// that this goes out of bounds...
		if( point.getX() < 1 || point.getY() < 1 ||
				point.getX() > alignment.getLength() ||
				point.getY() > alignment.getNumberSequences() )
			return;

		SequenceAlignmentPoint oldPoint = cursor.getPoint();
		cursor.setPoint( point );
		ensureSequencePointIsVisible( point );
		repaint( getCellBounds( oldPoint ) );
		repaint( getCellBounds( point ) );
	}

	public void setMark()
	{
		cursor.setMark();
	}

	public void moveCursor(SequenceAlignmentPoint newPoint) 
	{
		SequenceAlignmentPoint point = cursor.getPoint();
		setPoint(point.move(newPoint.getX(), newPoint.getY()));
	}

	public void moveCursorUp( int number )
	{
		SequenceAlignmentPoint point = cursor.getPoint();
		setPoint( point.move( point.getX(), point.getY() - number ) );
	}

	public void moveCursorDown( int number )
	{
		SequenceAlignmentPoint point = cursor.getPoint();
		setPoint( point.move( point.getX(), point.getY() + number ) );
	}

	public void moveCursorPageUp() 
	{
		SequenceAlignmentPoint point = cursor.getPoint();
		if ((point.getY() - 
				(getVisibleSequenceRectangle().getSize().getHeight() - 2)) < 1) 
			setPoint(point.move(point.getX(), 1));
		else
			setPoint(point.move
					(point.getX(), point.getY() - 
							(getVisibleSequenceRectangle().getSize().getHeight() - 2)));
	}

	public void moveCursorPageDown() 
	{
		SequenceAlignmentPoint point = cursor.getPoint();
		if ((point.getY() + 
				(getVisibleSequenceRectangle().getSize().getHeight() - 2)) < 1) 
			setPoint(point.move(point.getX(), alignment.getNumberSequences()));
		else
			setPoint(point.move
					(point.getX(), point.getY() + 
							(getVisibleSequenceRectangle().getSize().getHeight() - 2)));    
	}

	public void moveCursorBeginningOfAlignment() 
	{
		SequenceAlignmentPoint point = cursor.getPoint();
		setPoint(point.move(1,1));
	}

	public void moveCursorEndOfAlignment() 
	{
		SequenceAlignmentPoint point = cursor.getPoint();
		setPoint(point.move
				(alignment.getSequenceAt(alignment.getNumberSequences()).getGappedLength(),
						alignment.getNumberSequences()));
	}

	public void moveCursorRight( int number )
	{
		SequenceAlignmentPoint point = cursor.getPoint();
		setPoint
		( point.move
				( point.getX() + number, point.getY() ) );
	}

	public void moveCursorLeft( int number )
	{
		SequenceAlignmentPoint point = cursor.getPoint();
		setPoint
		( point.move
				( point.getX() - number, point.getY() ) );
	}

	public void moveCursorPageRight() 
	{
		SequenceAlignmentPoint point = cursor.getPoint();
		setPoint(point.move
				(point.getX() + 
						(getVisibleSequenceRectangle().getSize().getWidth() - 2), 
						point.getY()));
	}

	public void moveCursorPageLeft() 
	{
		SequenceAlignmentPoint point = cursor.getPoint();
		setPoint(point.move
				(point.getX() - 
						(getVisibleSequenceRectangle().getSize().getWidth() - 2), 
						point.getY()));
	}

	public void moveCursorBeginningOfSequence() 
	{
		SequenceAlignmentPoint point = cursor.getPoint();
		setPoint(point.move(1, point.getY()));
	}


	public void moveCursorToSequencePoint(int x, int y) 
	{
		SequenceAlignmentPoint point = cursor.getPoint();
		setPoint(point.move(x, y));
		ensureSequencePointIsVisible(point);
	}

	public void moveCursorToSequencePoint( SequenceAlignmentPoint point )
	{
		moveCursorToSequencePoint( point.getX(), point.getY() );
	}

	public SequenceAlignmentPoint getPoint()
	{
		return cursor.getPoint();
	}

	public SequenceAlignmentPoint getMark()
	{
		return cursor.getMark();
	}

	// methods delegated to UI
	public SequenceAlignmentPoint getSequencePointAtPoint( int x, int y )
	{
		return viewerUI.getSequencePointAtPoint( x, y );
	}

	public SequenceAlignmentPoint getSequencePointAtPoint( Point point )
	{
		return getSequencePointAtPoint
				( point.x, point.y );
	}

	public Point getPointAtSequencePoint( int x, int y )
	{
		return viewerUI.getPointAtSequencePoint( x, y );
	}

	private Rectangle cacheRect;
	public Rectangle getRectangleForSequenceRectangle( SequenceAlignmentRectangle seqRect )
	{
		if( cacheRect == null ) cacheRect = new Rectangle();

		cacheRect.setLocation( getPointAtSequencePoint( seqRect.getX(), seqRect.getY() ) );
		cacheRect.setSize
		( (seqRect.getWidth()) * getCellWidth(), (seqRect.getHeight()) * getCellHeight() );
		return cacheRect;
	}

	public Rectangle getCellBounds( SequenceAlignmentPoint point )
	{
		return viewerUI.getCellBounds( point );
	}

	public void ensureSequencePointIsVisible( SequenceAlignmentPoint point )
	{
		scrollRectToVisible
		( getCellBounds( point ) );
	}

	// is the alignment viewer read-only
	protected boolean readonly = false;
	public void setReadonly(boolean readonly) 
	{
		if (this.readonly != readonly) {
			this.readonly = readonly;
			firePropertyChange("readonly", !readonly, readonly);
		}
	}

	public boolean isReadonly() 
	{
		return this.readonly;
	}



	public SequenceAlignmentRectangle getVisibleSequenceRectangle()
	{
		Rectangle rect = getVisibleRect();
		SequenceAlignmentRectangle seqRect = new SequenceAlignmentRectangle();
		seqRect.setLocation
		( getSequencePointAtPoint( rect.x, rect.y ) );
		// we need to remember the sequence point before the resize

		seqRect.add( getSequencePointAtPoint( rect.x + rect.height, rect.y + rect.width ) );

		return seqRect;
	}

	// selection model methods
	public AlignmentSelectionModel getSelectionModel()
	{
		return selectionModel;
	}

	public void setSelectionModel( AlignmentSelectionModel selectionModel )
	{
		AlignmentSelectionModel old = this.selectionModel;
		this.selectionModel = selectionModel;
		firePropertyChange( "alignmentSelectionModel", old, selectionModel );
		repaint();
	}

	public void extendSelection( SequenceAlignmentPoint point )
	{
		selectionModel.extendSelection( point );
	}

	public void stopSelection( SequenceAlignmentPoint point )
	{
		selectionModel.stopSelection( point );
	}

	public void clearSelection()
	{
		selectionModel.clearSelection();
	}

	// get data models
	public void setSequenceAlignment( ISequenceAlignment msa )
	{
		ISequenceAlignment old = alignment;
		alignment = msa;

		// reset cursor and selection (PENDING:- PL) Color map as well?
		setPoint( new SequenceAlignmentPoint() );
		setMark();
		clearSelection();

		firePropertyChange( "sequenceAlignment", old, alignment );
		// make sure that the appearance of the GUI reflects the change!
		invalidate();
		repaint();
	}

	public ISequenceAlignment getSequenceAlignment()
	{
		return alignment;
	}

	public void changeOccurred( AlignmentEvent event )
	{
		//   SequenceAlignmentRectangle visRect = getVisibleSequenceRect();

		//      int start = event.getStart();
		//      int end   = event.getEnd();

		//      // if any of the affected rows are visible redraw
		//      if( (start > visRect.getX() && start <  visRect.getX() + visRect.getWidth()) ||
		//  	(end > visRect.getX() && end < visRect.getX() + visRect.getWidth() ) ){
		//        repaint();
		//      }      

		// changed my mind. By the time we have done all the stuff above
		// we might as well just repaint()!
		repaint();
	}

	private SequenceAlignmentRectangle previousSelection;

	public void valueChanged( AlignmentSelectionEvent event )
	{
		if( event.getSelectionRectangle() != null ){
			if( previousSelection  != null ){
				repaint( getRectangleForSequenceRectangle( previousSelection ) );
			}

			// repaint the region, and store the region for next time. Make
			// this a clone to ensure that its not changed behind our back
			repaint
			( getRectangleForSequenceRectangle
					( previousSelection = new SequenceAlignmentRectangle( event.getSelectionRectangle() )  ) );

		}
	}


	// Scrollable interface
	public Dimension getPreferredScrollableViewportSize()
	{
		return getPreferredSize();
	}

	public int getScrollableUnitIncrement( Rectangle visibleRect, int orientation, int direction )
	{
		if( orientation == SwingConstants.HORIZONTAL ){
			if( direction > 0 ){
				// scrolling right. 
				SequenceAlignmentPoint seqPoint = getSequencePointAtPoint( visibleRect.x, visibleRect.y );
				Rectangle rect = getCellBounds( seqPoint );

				int inc = visibleRect.x - rect.x;
				return inc;
			}
		}
		return 0;

	}

	public int getScrollableBlockIncrement( Rectangle visibleRect, int orientation, int direction )
	{
		return 0;
	}

	public boolean getScrollableTracksViewportWidth()
	{
		return false;
	}

	public boolean getScrollableTracksViewportHeight()
	{
		return false;
	}


	// these methods tie the this class into its equivalent UI
	// delegate.
	public String getUIClassID()
	{
		return uiClassID;
	}

	public void setUI( AlignmentViewerUI ui )
	{
		super.setUI( ui );
		viewerUI = (AlignmentViewerUI)ui;
	}

	@Override
	public void updateUI()
	{
		setUI( (AlignmentViewerUI)UIManager.getUI( this ) );
	}

	static
	{
		// ensure that UI delegates are known to swing. 
		Class c = Install.class;
	}

} // JAlignmentViewer



/*
 * ChangeLog
 * $Log: JAlignmentViewer.java,v $
 * Revision 1.28  2002/03/08 14:53:57  lord
 * Cosmetic changes
 *
 * Revision 1.27  2001/05/22 15:50:53  lord
 * New convenience method
 *
 * Revision 1.26  2001/04/11 17:04:42  lord
 * Added License agreements to all code
 *
 * Revision 1.25  2001/03/12 16:34:12  lord
 * Alignment selection renderer added
 *
 * Revision 1.24  2001/01/22 14:59:30  lord
 * Fixed repaint bug on selection
 *
 * Revision 1.23  2001/01/19 19:54:02  lord
 * Updated due to changes in SequenceAlignmentRectangle
 *
 * Revision 1.22  2001/01/15 18:55:13  lord
 * Improvement to selection model handling.
 *
 * Revision 1.21  2001/01/04 14:58:40  lord
 * Sorted imports
 *
 * Revision 1.20  2001/01/04 14:26:13  jns
 * o Bug fixing: had moved ReadonlyException to a new location, but
 * forgot to deal with the package statements, etc.
 *
 * Revision 1.19  2000/12/20 16:44:10  jns
 * o added in a read-only attribute and got the insertion and deletion of
 * gaps to check this. This was added in so that the viewer can be made
 * readonly.
 *
 * o bug-fix: I had done something weird with the rectangle repaint -
 * don't ask me what I was on. Anyway, hopefully I have sorted the
 * problem and it now paints from the x,y to the edge of the viewer and
 * the height of one sequence.
 *
 * o removed some System.out's to reduce the amount of stuff being sent
 * to STDOUT - these ought to be either STDERR or debug statements. As
 * far as I could see, the comments I have removed are irrelevant stuff.
 *
 * Revision 1.18  2000/12/05 15:54:37  lord
 * Import rationalisation
 *
 * Revision 1.17  2000/12/05 14:54:20  lord
 * Documentation update
 *
 * Revision 1.16  2000/11/02 14:49:53  jns
 * o added insert and delete gaps at a specific location, rather than
 * relying on the cursor to be in the correct place.
 *
 * Revision 1.15  2000/10/31 13:22:21  jns
 * o re-did the repaint on changeOccurred (SequenceEvent event). It now
 * repaints the correct bit - hopefully
 *
 * Revision 1.14  2000/10/26 17:21:27  jns
 * o added mouse click to move cursor
 *
 * Revision 1.13  2000/10/26 12:42:49  jns
 * o added editing facilities to SA - this includes insertion/deletion of gaps,
 * addition/removal of sequences from an alignment. It involved resolving some
 * conflicts with the group stuff.
 *
 * Revision 1.12  2000/09/27 16:18:12  jns
 * o added various methods to move the cursor around the
 * alignment. Hopefully this will help with cursor movement around the
 * alignment.
 * o reverted back to single fast cell renderer, because of the ability
 * to generate a multiplexer cell renderer that will render multiple cell
 * renderers at one time.
 *
 * Revision 1.11  2000/09/18 17:58:52  jns
 * o change to support multiple renderers, in preparation for the multiplexer
 * cell renderer which uses three cell renderers to render a cell
 *
 * Revision 1.10  2000/08/01 17:12:24  lord
 * Cosmetic
 *
 * Revision 1.9  2000/07/18 11:09:33  lord
 * Import rationalisation.
 *
 * Revision 1.8  2000/06/27 16:03:30  lord
 * Small fix. Now no longer leaves a gap at the bottom of the component.
 * This means that I can use it as a single sequence viewer sensibly.
 *
 * Revision 1.7  2000/06/13 11:12:49  lord
 * Added more event support
 *
 * Revision 1.6  2000/06/05 14:23:30  lord
 * Cosmetic changes
 *
 * Revision 1.5  2000/05/30 16:23:14  lord
 * Can now cope with colour map == null
 *
 * Revision 1.4  2000/05/24 15:39:51  lord
 * Sorted imports
 * Bad (non working) implementation of Scrollable interface
 *
 * Revision 1.3  2000/04/20 14:17:54  lord
 * Can now reset MSA
 *
 * Revision 1.2  2000/04/19 17:21:32  lord
 * Default constructor added, and ensure Install class loaded
 *
 * Revision 1.1  2000/04/18 17:43:56  lord
 * All files moved from uk.ac.man.bioinf.viewer package
 *
 * Revision 1.12  2000/04/18 17:32:01  lord
 * Modified to take Fast renderer as well as normal one
 *
 * Revision 1.11  2000/04/13 12:42:26  lord
 * Added support for selection
 *
 * Revision 1.10  2000/04/12 13:41:36  jns
 * o added in color mapping code
 *
 * Revision 1.9  2000/04/11 17:17:35  lord
 * Elementary selection support added. Probably buggy at the moment
 *
 * Revision 1.8  2000/04/06 15:46:25  lord
 * Changes to support a cursor, with proper cursor movement
 *
 * Revision 1.7  2000/04/03 13:54:18  lord
 * Removed references to ruler
 * Fixed bug in size calculations
 *
 * Revision 1.6  2000/03/31 16:23:35  lord
 * Removed JAlignmentRuler references as this is now done instead by the
 * JScrollPane
 *
 * Revision 1.5  2000/03/29 14:54:48  lord
 * Moved some of the slider set up stuff into the "setJAlignmentRuler"
 * method. Also made the changes necessary to cope with preferred size of
 * the ruler
 *
 * Revision 1.4  2000/03/27 18:49:55  lord
 * Preliminary addition of SequenceRuler and support methods
 *
 * Revision 1.3  2000/03/21 18:53:23  lord
 * Removed system out's
 *
 * Revision 1.2  2000/03/21 13:41:35  lord
 * Have basic system up now. Installs UI, uses renderer etc
 *
 * Revision 1.1  2000/03/16 16:19:20  lord
 * Initial checkin
 *
 */
