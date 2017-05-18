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
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;


/**
 * BasicAlignmentViewerUI.java
 *
 *
 * Created: Tue Mar 14 16:30:36 2000
 *
 * @author Phillip Lord
 * @version $Id: BasicAlignmentViewerUI.java,v 1.28 2001/04/11 17:04:42 lord Exp $
 */

public class BasicAlignmentViewerUI extends AlignmentViewerUI implements PropertyChangeListener, MouseInputListener {
	
	
	// the main panel on which to render things, defeating upwards
	// propogation of the paint, and validate calls
	private CellRendererPane rendererPane;
	// all of these come from the JAlignmentViewer, which we store here
	// to save time. 
	private JAlignmentViewer viewer;

	private int cellWidth;
	private int cellHeight;
	
	private AlignmentViewerCellRenderer renderer;
	private FastAlignmentViewerCellRenderer fastRenderer;
	private AlignmentSelectionRenderer selectionRenderer;
	private ISequenceAlignment alignment;
	private SequenceAlignmentPoint cursorLocation = new SequenceAlignmentPoint();  
	private AlignmentSelectionModel selectionModel;
	private boolean selectingToggle = false;

	public BasicAlignmentViewerUI()
	{
	}

	public static ComponentUI createUI( JComponent comp )
	{
		return new BasicAlignmentViewerUI();
	}


	@Override
	public void installUI( JComponent comp )
	{
		// make a new CellRendererPane. This class defeats upwards
		// propogation of paint, and validate requests which we dont
		// want. 
		//using new renderer pane
		rendererPane = new CellRendererPane();
		comp.add( rendererPane );
		// store the main component.
		viewer = (JAlignmentViewer)comp;
		// store the cell height and width. We will definately use,
		// repeatedly, so we might as well save them to save method invocations
		cellWidth = viewer.getCellWidth();
		cellHeight = viewer.getCellHeight();
		// like wise the cell renderer and alignment
		renderer = viewer.getCellRenderer();
		fastRenderer = viewer.getFastCellRenderer();
		selectionRenderer = viewer.getAlignmentSelectionRenderer();
		alignment = viewer.getSequenceAlignment();
		//ruler = viewer.getJAlignmentRuler();
		//rendererPane.add( ruler );
		selectionModel = viewer.getSelectionModel();

		installListeners();
	}

	public void uninstallUI( JComponent comp )
	{
		uninstallListeners();
	}


	public void installListeners()
	{
		viewer.addPropertyChangeListener( this );
		installKeyboardActions( viewer );
		viewer.addMouseListener(this);
		viewer.addMouseMotionListener(this);
		viewer.addPropertyChangeListener( this );
		viewer.addFocusListener
		( new FocusListener(){
			public void focusGained( FocusEvent event )
			{
				BasicAlignmentViewerUI.this.viewer.repaint();
			}

			public void focusLost( FocusEvent event )
			{
				BasicAlignmentViewerUI.this.viewer.repaint();
			}
		});
		viewer.addCursorChangeListener( new PointListener() );
	}

	public void uninstallListeners()
	{
		viewer.removePropertyChangeListener( this );
		viewer.removeMouseListener(this);
		viewer.removeMouseMotionListener(this);
	}


	// this is instance variable just to save on the object allocation
	private SequenceAlignmentPoint rendererLocation = new SequenceAlignmentPoint();

	public Rectangle paintFocusRect( Graphics g, Rectangle rect )
	{
		g.setColor( Color.black );
		g.drawRect( rect.x, rect.y, rect.width - 1, rect.height - 1 );
		rect.x -= 1;
		rect.y -= 1;
		rect.height -= 2;
		rect.width -= 2;
		return rect;
	}

	public void paint( Graphics g, JComponent c )
	{
		Rectangle rect = g.getClipBounds();

		if( c.hasFocus() ){
			rect = paintFocusRect( g, rect );
		}

		paintCells( g, c );
		paintSelection( g, c );
	}

	protected void paintCells( Graphics g, JComponent c )
	{
		Rectangle rect = g.getClipBounds();

		// calculate where to start drawing. We need to draw 1 either side
		// of the actuall clipping area though. 
		int topLeftIndex = getSequencePointAtPoint( rect.x, rect.y ).getY() - 1;
		topLeftIndex = (topLeftIndex < 1) ? 1: topLeftIndex;
		int topLeftPosition = getSequencePointAtPoint( rect.x, rect.y ).getX() - 1;
		topLeftPosition = ( topLeftPosition < 1 ) ? 1 : topLeftPosition;
		int topRightPosition = getSequencePointAtPoint( rect.x + rect.width, rect.y ).getX() + 1;
		int bottomLeftIndex = getSequencePointAtPoint( rect.x, rect.y + rect.height ).getY() + 1;

		// calc the start Y position.. We substract 1 because we
		// want cell number 1 to draw at position zero
		int currY = (cellHeight * (topLeftIndex - 1)) + c.getInsets().top;

		// iterate through all of the cells. 
		for( int j = topLeftIndex; j < bottomLeftIndex; j++ ){

			// we now iterate through the sequence elements in three
			// steps. First the nulls at the
			int startSeq = topLeftPosition;
			int endSeq = topRightPosition;

			// if we are off the bottom of the sequences then just pretend
			// that we never get to the sequence

			if( j >= alignment.getNumberSequences() + 1){
				startSeq = topRightPosition;
			}
			else{
				int temp;
				// else work out where the sequence starts at least if we are
				// not already into it.
				if( topLeftPosition < (temp = alignment.getInset( j ) + 1) ){
					startSeq = temp;
				}
				// and then work out where the end of the sequence is at least
				// if we get to it. 
				if( topRightPosition > (temp = alignment.getInset( j ) + alignment.getSequenceAt( j ).getGappedLength() + 1) ){
					endSeq = temp;
				}
			}

			for( int i = topLeftPosition; i < startSeq -1; i ++ ){

				rendererLocation.setLocation( i, j );

				// is code reuse really worth this????
				renderCell( g,  (i - 1) * cellWidth, currY, cellWidth, cellHeight, rect, rendererLocation, null );
			}

			for( int i = startSeq; i < endSeq; i ++ ){

				rendererLocation.setLocation( i, j );
				Element element =  alignment.getSequenceAt( j ).getGappedElementAt
						( i - alignment.getInset( j ) );

				// is code reuse really worth this????
				renderCell( g, (i - 1) * cellWidth, currY, cellWidth, cellHeight, rect, rendererLocation, element );
			}

			for( int i = endSeq + 1; i < topRightPosition; i++ ){

				rendererLocation.setLocation( i, j );

				// is code reuse really worth this????
				renderCell( g, (i - 1) * cellWidth, currY, cellWidth, cellHeight, rect, rendererLocation, null );
			}

			currY += cellHeight;
		}
	}

	protected void paintSelection( Graphics g, JComponent c )
	{
		Rectangle rect = g.getClipBounds();
		//g.setClip( rect.x, rect.y, rect.width, rect.height );
		//g.clipRect( rect.x, rect.y, rect.width, rect.height );

		SequenceAlignmentRectangle clipSeqRect = getSequenceAlignmentRectangleAtRectangle( rect );

		for( int i = 0; i < selectionModel.getNumberSelections(); i++ ){
			SequenceAlignmentRectangle selRect = selectionModel.getSelectionAt( i );
			if( true ){ //selRect.intersects( clipSeqRect ) ){
				Point leftTop = getPointAtSequencePoint( selRect.getX(), selRect.getY() );
				Point bottomRight = getPointAtSequencePoint( (selRect.getWidth() + selRect.getX()),
						(selRect.getHeight() + selRect.getY() ) );

				// g.setClip( rect.x, rect.y, rect.width, rect.height );
				//  g.setClip( leftTop.x, leftTop.y, (bottomRight.x - leftTop.x), (bottomRight.y - leftTop.y) );
				//g.clipRect( rect.x, rect.y, rect.width, rect.height );
				selectionRenderer.renderAlignmentSelection
				( g, leftTop.x, leftTop.y, (bottomRight.x - leftTop.x), (bottomRight.y - leftTop.y),
						viewer, selRect );
			}
		}
	}

	protected void repaint( SequenceAlignmentPoint point )
	{
		viewer.repaint( getCellBounds( point ) );
	}

	protected void paintImmediately( SequenceAlignmentPoint point )
	{
		viewer.paintImmediately( getCellBounds( point ) );
	}

	protected void renderCell
	( Graphics g, int currX, int currY, int cellWidth, int cellHeight,
			Rectangle rect, SequenceAlignmentPoint rendererLocation, Element element )
	{
		//if( true ) return ;
		// not quite sure what this does, but it looks good. Hopefully
		// though it should stop naughty renderers from doing evil
		// things to the layout. 
		//g.setClip( currX, currY, cellWidth, cellHeight );
		//g.clipRect( rect.x, rect.y, rect.width, rect.height );

		Color bgColor = viewer.getColorMap().getColorAt(alignment, element, rendererLocation);
		boolean isSelected = selectionModel.isPointSelected( rendererLocation );
		boolean hasFocus = viewer.hasFocus();
		boolean isAtPoint = rendererLocation.equals( cursorLocation );

		if( renderer == null ){
			fastRenderer.renderAlignmentViewerCell
			( g, currX, currY, cellWidth, cellHeight,
					viewer, element, rendererLocation,
					bgColor, isSelected, hasFocus, isAtPoint );
		}
		else{      
			Component cell = renderer.getAlignmentViewerCellRendererComponent
					( viewer,
							element,
							rendererLocation,
							bgColor,
							isSelected,
							hasFocus, 
							isAtPoint );

			rendererPane.paintComponent( g, cell, viewer, currX, currY,
					cellWidth, cellHeight, true );

		}

		// support for an XOR cursor blink. If the we are drawing the
		// right cell just XOR it...
		if( getBlinkOn() && rendererLocation.equals( cursorBlinkPoint ) ){
			// now that we have drawn do the XOR as the blink is on.
			g.setXORMode( Color.white );
			g.fillRect( currX, currY, cellWidth, cellHeight );
			g.setPaintMode();
		}
	}

	private SequenceAlignmentRectangle seqRect = new SequenceAlignmentRectangle();
	public SequenceAlignmentRectangle getSequenceAlignmentRectangleAtRectangle( Rectangle rect )
	{
		seqRect.setLocation( getSequencePointAtPoint( (int)rect.getX(), (int)rect.getY() ) );

		SequenceAlignmentPoint loc = getSequencePointAtPoint
				( (int)(rect.getX() + rect.getWidth()), (int)(rect.getY() + rect.getHeight()) );


		seqRect.setSize( loc.getX() - seqRect.getX(), loc.getY() - seqRect.getY() );
		return seqRect;
	}


	private SequenceAlignmentPoint loc = new SequenceAlignmentPoint( 0, 0 );
	public SequenceAlignmentPoint getSequencePointAtPoint( int x, int y )
	{
		// need to think about this. Is there a problem here with
		// downwards rounding???

		// also this ignores the insets issue for the moment, which is
		// something else I need to think about. 
		// ignore all the insets for the moment.

		int sequencePos = (x / cellWidth) + 1;
		int alignmentLoc = (y / cellHeight) + 1;

		loc.setLocation
		( sequencePos, alignmentLoc );
		return loc;
	}

	public Point getPointAtSequencePoint( int x, int y )
	{ 
		return new Point
				( (x - 1) * cellWidth, 
						(y - 1) * cellHeight );
	}

	public Point getPointAtSequencePoint( SequenceAlignmentPoint point )
	{
		return getPointAtSequencePoint
				( point.getX(), 
						point.getY() );
	}

	public Rectangle getCellBounds( SequenceAlignmentPoint point )
	{
		return getCellBounds( point, null );
	}

	public Rectangle getCellBounds( SequenceAlignmentPoint point, Rectangle rect )
	{
		if( rect == null ) rect = new Rectangle();
		Point physPoint = getPointAtSequencePoint( point );
		rect.setLocation( physPoint.x, physPoint.y );
		rect.setSize( cellWidth, cellHeight );
		return rect;
	}

	// (PENDING:- PL) These should be protected methods...
	private Rectangle blinkedCursor = new Rectangle();
	private SequenceAlignmentPoint cursorBlinkPoint;
	private boolean blinkOn = false;
	private CursorBlinkThread cursorThread;

	// I've put these into private methods so that I could debug when
	// the values were being changed
	private synchronized void setBlinkOn( boolean on )
	{
		blinkOn = on;
	}

	private boolean getBlinkOn()
	{
		return blinkOn;
	}

	public synchronized void setCursorBlink()
	{
		if( cursorThread == null ){
			cursorThread = new CursorBlinkThread();
			cursorThread.start();
		}

		// cache the current 
		SequenceAlignmentPoint oldCursor
		= cursorBlinkPoint;

		//get the cursor position
		cursorBlinkPoint = 
				viewer.getCursorModel().getPoint();

		if( getBlinkOn() ){
			// we do not want to unset the cursor again!
			cursorThread.interrupt();
			// need to wipe old blink. Do this by just painting over it. 
			paintImmediately( oldCursor );
		}


		getCellBounds( cursorBlinkPoint, blinkedCursor );
		setBlinkOn( true );
		// this should repaint with XOR
		repaint( cursorBlinkPoint );


		cursorThread.notifyImpl();
	}

	public synchronized void unsetCursorBlink()
	{
		setBlinkOn( false );
		paintImmediately( cursorBlinkPoint );
	}

	class CursorBlinkThread extends Thread
	{
		public void run()
		{
			while( true ){
				try{
					sleep( 75 );
					BasicAlignmentViewerUI.this.unsetCursorBlink();
					waitImpl();
				}
				catch( InterruptedException iep ){
					// we can safely ignore this.
				}
				catch( Throwable t ){
					t.printStackTrace();
				}
			}
		}

		public synchronized void waitImpl() throws InterruptedException
		{
			this.wait();
		}

		public synchronized void notifyImpl()
		{
			this.notify();
		}
	}

	// implementation of java.beans.PropertyChangeListener interface
	public void propertyChange( PropertyChangeEvent pce )
	{
		if( pce.getPropertyName().equals( "cellHeight" ) ){

			Rectangle rectangle = viewer.getVisibleRect();
			// this is the sequence point before we have changed the cellHeight.
			SequenceAlignmentPoint seqPoint = getSequencePointAtPoint
					( rectangle.getLocation() );

			cellHeight = ((Integer)pce.getNewValue()).intValue();

			// now get the new location of the old SequenceAlignmentPoint
			Point newPoint = getPointAtSequencePoint( seqPoint );

			// which is the point that we want to make the top left. The old
			// size and so forth are correct. 
			rectangle.setLocation( newPoint );

			viewer.scrollRectToVisible( rectangle );
		}

		else if( pce.getPropertyName().equals( "cellWidth" ) ){

			Rectangle rectangle = viewer.getVisibleRect();
			// this is the sequence point before we have changed the cellHeight.
			SequenceAlignmentPoint seqPoint = getSequencePointAtPoint
					( rectangle.getLocation() );

			cellWidth = ((Integer)pce.getNewValue()).intValue();

			// now get the new location of the old SequenceAlignmentPoint
			Point newPoint = getPointAtSequencePoint( seqPoint );

			// which is the point that we want to make the top left. The old
			// size and so forth are correct. 
			rectangle.setLocation( newPoint );

			viewer.scrollRectToVisible( rectangle );
		}

		else if( pce.getPropertyName().equals( "fastCellRenderer" ) ){
			fastRenderer = (FastAlignmentViewerCellRenderer)pce.getNewValue();
		}

		else if( pce.getPropertyName().equals( "cellRenderer" ) ){
			renderer = (AlignmentViewerCellRenderer)pce.getNewValue();
		}

		else if( pce.getPropertyName().equals( "alignmentSelectionRenderer" ) ){
			selectionRenderer = (AlignmentSelectionRenderer)pce.getNewValue();
		}

		else if( pce.getPropertyName().equals( "sequenceAlignment" ) ){
			alignment = (ISequenceAlignment)pce.getNewValue();
		}

		else if( pce.getPropertyName().equals( "alignmentSelectionModel" ) ){
			selectionModel = (AlignmentSelectionModel)pce.getNewValue();
		}

		// if read-only change
		else if (pce.getPropertyName().equals("readonly")) {
			if (((Boolean)pce.getNewValue()).booleanValue())
				// uninstall the mouse motion listener
				viewer.removeMouseMotionListener(this);
			else
				// install the mouse motion listener
				viewer.addMouseMotionListener(this);
		}
	}

	protected void installKeyboardActions( JAlignmentViewer viewer )
	{
		viewer.registerKeyboardAction
		( new SelectionToggler(), KeyStroke.getKeyStroke
				( KeyEvent.VK_ENTER, 0 ), JComponent.WHEN_IN_FOCUSED_WINDOW );

		viewer.registerKeyboardAction
		( new SelectionClearer(), KeyStroke.getKeyStroke
				( KeyEvent.VK_BACK_SPACE, 0 ), JComponent.WHEN_IN_FOCUSED_WINDOW );

		// key ' ' to insert a gap at the cursor
		viewer.registerKeyboardAction
		(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				try {
					BasicAlignmentViewerUI.this.viewer.insertGapsAtCursor(1);
					
				} catch (SequenceVetoException ex) {

					ex.printStackTrace();

				} catch (AlignmentVetoException ex) {

					ex.printStackTrace();
				}
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

		// key 'DEL' to remove a gap at the cursor
		viewer.registerKeyboardAction
		(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				try {
					BasicAlignmentViewerUI.this.viewer.deleteGapsAtCursor(1);
				} catch (SequenceVetoException ex) {
					
					ex.printStackTrace();
					
				} catch (AlignmentVetoException ex) {
					
					ex.printStackTrace();
				}
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

		registerKey(KeyEvent.VK_RIGHT, KeyScroller.RIGHT );
		registerKey(KeyEvent.VK_LEFT, KeyScroller.LEFT );
		registerKey(KeyEvent.VK_DOWN, KeyScroller.DOWN );
		registerKey(KeyEvent.VK_UP, KeyScroller.UP );

		registerKey("KP_RIGHT", KeyScroller.RIGHT );
		registerKey("KP_LEFT", KeyScroller.LEFT );
		registerKey("KP_DOWN", KeyScroller.DOWN );
		registerKey("KP_UP", KeyScroller.UP );
	}

	//(PENDING:- PL) This needs to be more generic, so that it can
	// handle to end/to begining events and so forth. Keep it simple at first!!!
	protected void registerKey( String keyEvent, int direction )
	{
		viewer.registerKeyboardAction
		( new KeyScroller( viewer, direction ), 
				KeyStroke.getKeyStroke( keyEvent ),
				JComponent.WHEN_IN_FOCUSED_WINDOW );
	}

	protected void registerKey( int keyEvent, int direction )
	{
		viewer.registerKeyboardAction
		( new KeyScroller( viewer, direction ), 
				KeyStroke.getKeyStroke( keyEvent, 0 ),
				JComponent.WHEN_IN_FOCUSED_WINDOW );
	}

	public void mouseClicked(MouseEvent e) 
	{
		viewer.moveCursor(getSequencePointAtPoint(e.getPoint()));
	}

	public void mouseEntered(MouseEvent e) 
	{
		/* Currently this method is not used. Code may be provided for
		 * functionality at a later stage.
		 */
	}

	public void mouseExited(MouseEvent e) 
	{
		/* Currently this method is not used. Code may be provided for
		 * functionality at a later stage.
		 */
	}

	private SequenceAlignmentPoint originalSequencePoint = null;
	private SequenceAlignmentPoint currentSequencePoint = null;
	private int originalMouseY = 0;

	// this is the last place that point we signalled as an extended
	// selection. If the new event it the same place we can ignore it. 
	private SequenceAlignmentPoint lastExtendedSelectionPoint = new SequenceAlignmentPoint();

	public void mousePressed(MouseEvent e) 
	{
		// if button 2 is down, then we want to extend the selection 
		if( ( e.getModifiers() & InputEvent.BUTTON2_MASK ) == InputEvent.BUTTON2_MASK ){

			// clear the selection model it is not selecting. 
			if( !selectionModel.isSelecting() ){
				selectionModel.clearSelection();
			}

			// signal the change
			selectionModel.extendSelection
			( new SequenceAlignmentPoint
					( getSequencePointAtPoint( e.getX(), e.getY() ) ) );

			// and remember this point.
			lastExtendedSelectionPoint.setLocation
			( getSequencePointAtPoint( e.getX(), e.getY() ) );

		}
		else{
			// set the original sequence point at mouse down
			originalSequencePoint = new SequenceAlignmentPoint
					(getSequencePointAtPoint(e.getX(), e.getY()));
			// store the Y location to prevent drifting
			originalMouseY = e.getY();
		}
	}

	public void mouseReleased(MouseEvent e) 
	{
		// if button 2 is down, then we want to stop the selection
		if( ( e.getModifiers() & InputEvent.BUTTON2_MASK ) == InputEvent.BUTTON2_MASK ){
			selectionModel.stopSelection
			( new SequenceAlignmentPoint
					( getSequencePointAtPoint( e.getX(), e.getY() ) ) );
		}
		else{
			// move the cursor to the current location
			if (currentSequencePoint != null)
				viewer.moveCursor(currentSequencePoint);

			// reset the variables
			originalSequencePoint = null;
			originalMouseY = 0;
			currentSequencePoint = null;
		}
	}

	public void mouseDragged(MouseEvent e) 
	{ 
		// if button 2 is down, then we want to extend the selection 
		if( ( e.getModifiers() & InputEvent.BUTTON2_MASK ) == InputEvent.BUTTON2_MASK ){
			// where have we dragged to. 
			SequenceAlignmentPoint nowAtPoint = getSequencePointAtPoint( e.getX(), e.getY() );

			// if drag has moved us into a new region signal an event
			if( !nowAtPoint.equals( lastExtendedSelectionPoint ) ){
				selectionModel.extendSelection
				( new SequenceAlignmentPoint( nowAtPoint ) );
				lastExtendedSelectionPoint.setLocation( nowAtPoint );
			}
		}
		else{
			// if we are still in the viewer
			if ((viewer.contains(e.getPoint())) || (originalSequencePoint != null)) {
				// get the current sequence point
				currentSequencePoint = new SequenceAlignmentPoint
						(getSequencePointAtPoint(e.getX(), originalMouseY));

				// the difference between the two points
				int difference = currentSequencePoint.getX() - 
						originalSequencePoint.getX();

				// return if we havent gone anywhere significant
				if (difference == 0)
					return;

				// if inserting gaps
				if (difference > 0) {
					try {
						// insert the gaps
						viewer.insertGapsAt(originalSequencePoint, difference);
						// set the current position to the original insert point to
						// avoid odd behavior
						originalSequencePoint = new SequenceAlignmentPoint
								(currentSequencePoint);
					} catch (SequenceVetoException excep) {
						/* (PENDING: JNS) 31.10.00 Wasnt sure what to do with this,
						 * so for the time being it is just being chucked out to the
						 * debug.
						 */
						//Debug.both(this, "Cannot insert a gap here - it was vetoed!", excep);
					} catch (AlignmentVetoException excep) {
						/* (PENDING: JNS) 31.10.00 Wasnt sure what to do with this,
						 * so for the time being it is just being chucked out to the
						 * debug.
						 */
						//` Debug.both(this, "Cannot insert a gap here - it was vetoed!", excep);
					}
					// if deleting gaps
				} else if (difference < 0) {
					try {
						// get the number of gaps downstream
						int maxNumberGapsUpstream = 
								Sequences.getNumberGapsUpstreamFrom
								(currentSequencePoint.getX() - 
										alignment.getInset(originalSequencePoint.getY()), 
										alignment.getSequenceAt(originalSequencePoint.getY()));

						// if able to delete all gaps requested (this inc. the inset gaps)
						if (((currentSequencePoint.getX() < 
								alignment.getInset(originalSequencePoint.getY()) + 1) && 
								(-(difference) < alignment.
										getInset(originalSequencePoint.getY()) + 1)) || 
								(maxNumberGapsUpstream > -(difference)))
							// delete the gaps
							viewer.deleteGapsAt(currentSequencePoint.getX(), 
									originalSequencePoint.getY(), 
									-(difference));
						// otherwise remove the max number of gaps possible
						else
							viewer.deleteGapsAt(currentSequencePoint.getX(),
									originalSequencePoint.getY(),
									maxNumberGapsUpstream);

						/* if gaps were deleted (i.e., maxNumberGapsUpstream != 0
						 * then move the original insert point, otherwise don't
						 */
						if (maxNumberGapsUpstream != 0)
							// set the current position to the original insert point
							originalSequencePoint = new SequenceAlignmentPoint
							(currentSequencePoint);
					} catch (NoGapAtThisPositionException excep) {
						// this should not happen (I think)
						
						excep.printStackTrace();
						
					} catch (SequenceVetoException excep) {
						
						excep.printStackTrace();
						
					} catch (AlignmentVetoException excep) {
						
						excep.printStackTrace();						
					}
				} else return;
			}
		}
	}

	public void mouseMoved(MouseEvent e) 
	{
		/* Currently this method is not used. Code may be provided for
		 * functionality at a later stage.
		 */
	}

	public class PointListener implements ChangeListener
	{
		public void stateChanged( ChangeEvent event )
		{
			BasicAlignmentViewerUI.this.cursorLocation = 
					BasicAlignmentViewerUI.this.viewer.getPoint();

			if( BasicAlignmentViewerUI.this.selectingToggle ){
				BasicAlignmentViewerUI.this.viewer.extendSelection
				( BasicAlignmentViewerUI.this.viewer.getPoint() );
			}
			BasicAlignmentViewerUI.this.setCursorBlink();
		}
	}

	public class SelectionClearer implements ActionListener
	{
		public void actionPerformed( ActionEvent event )
		{
			BasicAlignmentViewerUI.this.viewer.clearSelection();
			BasicAlignmentViewerUI.this.selectingToggle = false;
		}
	}

	public class SelectionToggler implements ActionListener
	{
		public void actionPerformed( ActionEvent event )
		{
			if( BasicAlignmentViewerUI.this.selectingToggle ){
				BasicAlignmentViewerUI.this.selectingToggle = false;
			}
			else{
				BasicAlignmentViewerUI.this.selectingToggle = true;
				BasicAlignmentViewerUI.this.viewer.extendSelection
				( BasicAlignmentViewerUI.this.viewer.getPoint() );
			}
		}
	}

	public class KeyScroller implements ActionListener
	{
		private JAlignmentViewer viewer;
		private int direction;

		public static final int UP = 1;
		public static final int DOWN = 2;
		public static final int RIGHT = 3;
		public static final int LEFT = 4;

		public KeyScroller( JAlignmentViewer viewer, int direction )
		{
			this.viewer = viewer;
			this.direction = direction;
		}

		public void actionPerformed( ActionEvent event )
		{
			switch (direction){

			case UP:
				viewer.moveCursorUp( 1 );
				break;
			case DOWN:
				viewer.moveCursorDown( 1 );
				break;
			case RIGHT:
				viewer.moveCursorRight( 1 );
				break;
			case LEFT:
				viewer.moveCursorLeft( 1 );
				break;
			}
		}
	}
} // BasicAlignmentViewerUI



/*
 * ChangeLog
 * $Log: BasicAlignmentViewerUI.java,v $
 * Revision 1.28  2001/04/11 17:04:42  lord
 * Added License agreements to all code
 *
 * Revision 1.27  2001/03/12 17:05:00  lord
 * Selection Renderer support
 *
 * Revision 1.26  2001/02/19 17:44:37  lord
 * Import sort
 *
 * Revision 1.25  2001/01/31 17:59:08  lord
 * Fixed horrible rendering bug. Essentially it was drawing in the wrong
 * place. Nasty
 *
 * Revision 1.24  2001/01/19 19:52:04  lord
 * Improved the mouse handling. Produces many fewer events now during selection
 *
 * Revision 1.23  2001/01/15 18:48:25  lord
 * Mouse-2 selection handling
 *
 * Revision 1.22  2000/12/20 16:47:17  jns
 * o code added to deal with readonly state of the JAlignmentViewer -
 * basically as a quick fix it (un)installs the mouse motion listeners.
 *
 * Revision 1.21  2000/12/14 17:07:26  jns
 * o added code to prevent a bug of gap insertion slipage.
 *
 * Revision 1.20  2000/12/11 17:25:32  jns
 * o bug-fix: there was a bug that happened when opening gaps using the
 * mouse - basically any vertical movement of the mouse was taken into
 * account, and as a consequence gaps were not always inserted into the
 * sequence intended. This is now fixed, simply by storing the Y location
 * of the mouse when the mouse button is pressed, and this being used to
 * keep track of the sequence the gaps are being opened in.
 *
 * Revision 1.19  2000/12/05 15:54:37  lord
 * Import rationalisation
 *
 * Revision 1.18  2000/11/13 18:20:32  jns
 * o hopefully this UI now takes account of insets, and therefore draws
 * itself properly.
 *
 * Revision 1.17  2000/11/09 16:21:08  lord
 * Qualified inner class this calls because jikes complains otherwise
 *
 * Revision 1.16  2000/11/03 18:52:10  lord
 * Blinking cursor installed
 *
 * Revision 1.15  2000/11/02 16:35:04  jns
 * o bug fixing so that it delete's insets properly
 *
 * Revision 1.14  2000/11/02 14:57:38  jns
 * o put in mouse drag stuff
 * o changed key for deleting residue to more logical DEL key
 *
 * Revision 1.13  2000/10/26 17:21:27  jns
 * o added mouse click to move cursor
 *
 * Revision 1.12  2000/10/26 12:42:49  jns
 * o added editing facilities to SA - this includes insertion/deletion of gaps,
 * addition/removal of sequences from an alignment. It involved resolving some
 * conflicts with the group stuff.
 *
 * Revision 1.11  2000/09/27 16:20:22  jns
 * o reverted back to single fast cell renderer, because of the ability
 * to generate a multiplexer cell renderer that will render multiple cell
 * renderers at one time.
 *
 * Revision 1.10  2000/09/18 18:03:59  jns
 * o changes to support multiple cell renderers and drawing them one after another,
 * on top of each other.
 * o NB: Therefore, the order of the renderers *is* important - the first renderer
 * is used first, and all subsequent ones draw over it!
 *
 * Revision 1.9  2000/08/01 17:13:41  lord
 * Now shows the same bit of the alignment if the CellHeight or Width is
 * shown. This requires scrolling to the new region showing this. At the
 * moment this makes the screen jump a little, but I'm not sure that its
 * worth the effort to fix this cosmetic problem.
 *
 * Revision 1.8  2000/07/18 11:09:45  lord
 * Import rationalisation.
 *
 * Revision 1.7  2000/06/13 11:12:22  lord
 * Fixed getLength/getGappedLength bug.
 *
 * Revision 1.6  2000/06/05 14:23:50  lord
 * Fixed bug, now showing last sequence as well
 *
 * Revision 1.5  2000/05/30 16:04:18  lord
 * Have rationalised and sorted all the import statements
 *
 * Revision 1.4  2000/05/24 15:40:34  lord
 * Minor algorithmic fiddling. Probably need to rewrite the paint
 * algorithm here.
 *
 * Revision 1.3  2000/05/18 17:06:05  lord
 * Fixed bug with display of gapped sequences
 *
 * Revision 1.2  2000/05/08 17:05:13  lord
 * Redraws entire viewable area now, even if off the bottom
 * of the sequence. This seems to make it work better with the
 * performance speed ups that we are using
 *
 * Revision 1.1  2000/04/18 17:45:24  lord
 * All files moved from uk.ac.man.bioinf.viewer package
 *
 * Revision 1.14  2000/04/18 17:32:28  lord
 * Modified to take Fast renderer as well as normal one
 *
 * Revision 1.13  2000/04/13 12:46:47  lord
 * Support for selection
 *
 * Revision 1.12  2000/04/12 13:42:49  jns
 * o added in color mapping code
 *
 * Revision 1.11  2000/04/11 17:17:35  lord
 * Elementary selection support added. Probably buggy at the moment
 *
 * Revision 1.10  2000/04/06 15:46:25  lord
 * Changes to support a cursor, with proper cursor movement
 *
 * Revision 1.9  2000/04/03 13:54:53  lord
 * Fixed various sizing bugs
 *
 * Revision 1.8  2000/03/31 16:26:58  lord
 * Removed the links to JAlignmentRuler, and its drawing. Also have
 * changed the CellRenderer iteration code as it was drawing an extra
 * cell at the edges that it shouldnt be.
 *
 * Revision 1.7  2000/03/29 15:50:47  lord
 * Updated to use new sequence.geom package
 *
 * Revision 1.6  2000/03/29 14:58:51  lord
 * Should now be able to cope with a null ruler, though I havent actually
 * tested it out yet.
 *
 * Revision 1.5  2000/03/27 18:51:24  lord
 * Added initial support for JAlignmentRuler
 *
 * Revision 1.4  2000/03/23 19:50:42  lord
 * Unwound for loops for efficiency of drawing
 *
 * Revision 1.3  2000/03/21 18:52:47  lord
 * Now copes with insets, removed hack element
 *
 * Revision 1.2  2000/03/21 13:42:06  lord
 * Lots of changes. Have basic system set up
 *
 * Revision 1.1  2000/03/16 16:19:20  lord
 * Initial checkin
 *
 */



