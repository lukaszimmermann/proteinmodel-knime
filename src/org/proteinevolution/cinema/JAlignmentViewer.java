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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.proteinevolution.models.interfaces.ISequenceAlignment;


/**
 * JAlignmentViewer.java
 *
 *
 * Created: Tue Mar 14 16:28:59 2000
 *
 * @author Phillip Lord
 * @version $Id: JAlignmentViewer.java,v 1.28 2002/03/08 14:53:57 lord Exp $
 */

public class JAlignmentViewer extends JPanel implements PropertyChangeListener {

	private static final long serialVersionUID = -3204831648550371466L;
	public static final String uiClassID = "AlignmentViewerUI";


	// Alignment ViewerUI and the actual alignment
	private CellRendererPane rendererPane;
	private ISequenceAlignment alignment;

	// TODO
	// (PENDING:- PL) this default is not really acceptable. It should be
	// changed to something more sensible, using the preferred size of
	// the CellRenderer

	private Dimension prototypicalCellSize;
	private AlignmentViewerCellRenderer renderer;
	private FastAlignmentViewerCellRenderer fastRenderer;
	private AlignmentSelectionRenderer selectionRenderer;

	private int cellWidth;
	private int cellHeight;

	// TODO Replace by something less verbose
	private SequenceCursor cursor;
	private SequenceAlignmentPoint cursorLocation;


	// this is instance variable just to save on the object allocation
	private SequenceAlignmentPoint rendererLocation;
	private SequenceAlignmentRectangle seqRect;


	private AlignmentSelectionModel selectionModel;
	private boolean selectingToggle = false;
	private ColorMap colorMap;


	private SequenceAlignmentPoint originalSequencePoint = null;
	private SequenceAlignmentPoint currentSequencePoint = null;
	private int originalMouseY = 0; 
	private SequenceAlignmentPoint lastExtendedSelectionPoint = new SequenceAlignmentPoint();

	private SequenceAlignmentPoint loc = new SequenceAlignmentPoint( 0, 0 );



	public JAlignmentViewer( ISequenceAlignment alignment ) {
		super();
		// (PENDING:- PL) We probably need to install listeners here to
		// this SequenceAlignment. Should we allow it to change outside? I
		// think so...    

		// Initialize the component
		this.rendererPane =  new CellRendererPane();
		this.alignment = alignment;

		// Cursor and cursor location
		this.cursor = new DefaultSequenceCursor();
		this.cursorLocation = new SequenceAlignmentPoint();  

		this.colorMap = new SingleColorMap(getBackground());
		this.prototypicalCellSize = new Dimension( 30, 30 );

		// Renderer
		this.fastRenderer = new DefaultFastAlignmentViewerCellRenderer();
		this.selectionRenderer = new DefaultAlignmentSelectionRenderer();
		this.rendererLocation = new SequenceAlignmentPoint();
		this.seqRect = new SequenceAlignmentRectangle();

		this.selectionModel = new SingleAlignmentSelectionModel();

		// Add the render pane
		this.add(this.rendererPane);

		// Register keyboard actions
		this.registerKeyboardAction( new SelectionToggler(), KeyStroke.getKeyStroke ( KeyEvent.VK_ENTER, 0 ), JComponent.WHEN_IN_FOCUSED_WINDOW );
		this.registerKeyboardAction( new SelectionClearer(), KeyStroke.getKeyStroke( KeyEvent.VK_BACK_SPACE, 0 ), JComponent.WHEN_IN_FOCUSED_WINDOW );

		this.registerKeyboardAction(  new KeyScroller( this, KeyScroller.RIGHT ), KeyStroke.getKeyStroke( KeyEvent.VK_RIGHT, 0 ), JComponent.WHEN_IN_FOCUSED_WINDOW );
		this.registerKeyboardAction(  new KeyScroller( this, KeyScroller.LEFT ), KeyStroke.getKeyStroke( KeyEvent.VK_LEFT, 0 ), JComponent.WHEN_IN_FOCUSED_WINDOW );
		this.registerKeyboardAction(  new KeyScroller( this, KeyScroller.DOWN ), KeyStroke.getKeyStroke( KeyEvent.VK_DOWN, 0 ), JComponent.WHEN_IN_FOCUSED_WINDOW );
		this.registerKeyboardAction(  new KeyScroller( this, KeyScroller.UP ), KeyStroke.getKeyStroke( KeyEvent.VK_UP, 0 ), JComponent.WHEN_IN_FOCUSED_WINDOW );

		this.registerKeyboardAction(  new KeyScroller( this, KeyScroller.RIGHT ), KeyStroke.getKeyStroke(  "KP_RIGHT"), JComponent.WHEN_IN_FOCUSED_WINDOW );
		this.registerKeyboardAction(  new KeyScroller( this, KeyScroller.LEFT ), KeyStroke.getKeyStroke(  "KP_LEFT"), JComponent.WHEN_IN_FOCUSED_WINDOW );
		this.registerKeyboardAction(  new KeyScroller( this, KeyScroller.DOWN ), KeyStroke.getKeyStroke( "KP_DOWN"), JComponent.WHEN_IN_FOCUSED_WINDOW );
		this.registerKeyboardAction(  new KeyScroller( this, KeyScroller.UP ), KeyStroke.getKeyStroke(  "KP_UP" ), JComponent.WHEN_IN_FOCUSED_WINDOW );

		// Further listener (Probably quite useless)
		//this.addMouseListener(this);
		//this.addMouseMotionListener(this);
		this.addPropertyChangeListener(this);
		this.addCursorChangeListener(new PointListener());


		// Focus listener
		this.addFocusListener
		( new FocusListener(){
			public void focusGained( FocusEvent event ) {
				repaint();
			}

			public void focusLost( FocusEvent event ) {
				repaint();
			}
		});	
	}


	public SequenceAlignmentRectangle getSequenceAlignmentRectangleAtRectangle( Rectangle rect ) {

		this.seqRect.setLocation( this.getSequencePointAtPoint( (int)rect.getX(), (int)rect.getY() ) );
		SequenceAlignmentPoint loc = getSequencePointAtPoint ( (int)(rect.getX() + rect.getWidth()), (int)(rect.getY() + rect.getHeight()) );
		seqRect.setSize( loc.getX() - seqRect.getX(), loc.getY() - seqRect.getY() );
		return seqRect;
	}


	public SequenceAlignmentPoint getSequencePointAtPoint( int x, int y ) {
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





	public Rectangle getCellBounds( SequenceAlignmentPoint point ) {
		return getCellBounds( point, null );
	}

	public Rectangle getCellBounds( SequenceAlignmentPoint point, Rectangle rect ) {
		if( rect == null ) {	
			rect = new Rectangle();
		}
		Point physPoint = getPointAtSequencePoint( point );
		rect.setLocation( physPoint.x, physPoint.y );
		rect.setSize( cellWidth, cellHeight );
		return rect;
	}




	// implementation of java.beans.PropertyChangeListener interface
	@Override
	public void propertyChange( PropertyChangeEvent pce ) {
		if( pce.getPropertyName().equals( "cellHeight" ) ){

			Rectangle rectangle = this.getVisibleRect();
			// this is the sequence point before we have changed the cellHeight.
			SequenceAlignmentPoint seqPoint = getSequencePointAtPoint
					( rectangle.getLocation() );

			cellHeight = ((Integer)pce.getNewValue()).intValue();

			// now get the new location of the old SequenceAlignmentPoint
			Point newPoint = getPointAtSequencePoint( seqPoint );

			// which is the point that we want to make the top left. The old
			// size and so forth are correct. 
			rectangle.setLocation( newPoint );

			this.scrollRectToVisible( rectangle );
		}

		else if( pce.getPropertyName().equals( "cellWidth" ) ){

			Rectangle rectangle = this.getVisibleRect();
			// this is the sequence point before we have changed the cellHeight.
			SequenceAlignmentPoint seqPoint = getSequencePointAtPoint
					( rectangle.getLocation() );

			cellWidth = ((Integer)pce.getNewValue()).intValue();

			// now get the new location of the old SequenceAlignmentPoint
			Point newPoint = getPointAtSequencePoint( seqPoint );

			// which is the point that we want to make the top left. The old
			// size and so forth are correct. 
			rectangle.setLocation( newPoint );

			this.scrollRectToVisible( rectangle );
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
			if (((Boolean)pce.getNewValue()).booleanValue()) {
				// uninstall the mouse motion listener
				//this.removeMouseMotionListener(this);
			}
			else {
				// install the mouse motion listener
				//this.addMouseMotionListener(this);
			}
		}
	}



	public Rectangle paintFocusRect( Graphics g, Rectangle rect ) {

		g.setColor( Color.black );
		g.drawRect( rect.x, rect.y, rect.width - 1, rect.height - 1 );
		rect.x -= 1;
		rect.y -= 1;
		rect.height -= 2;
		rect.width -= 2;

		return rect;
	}

	public void paint( Graphics g, JComponent c ) {
		Rectangle rect = g.getClipBounds();

		if( c.hasFocus() ){
			rect = paintFocusRect( g, rect );
		}

		paintCells( g, c );
		paintSelection( g, c );
	}


	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	protected void paintCells( Graphics g, JComponent c ) {

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
			/*
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
			 */

			for( int i = topLeftPosition; i < startSeq -1; i ++ ){

				rendererLocation.setLocation( i, j );

				// is code reuse really worth this????
				renderCell( g,  (i - 1) * cellWidth, currY, cellWidth, cellHeight, rect, rendererLocation, null );
			}

			for( int i = startSeq; i < endSeq; i ++ ){

				rendererLocation.setLocation( i, j );
				//Element element =  alignment.getSequenceAt( j ).getGappedElementAt
				//		( i - alignment.getInset( j ) );

				// is code reuse really worth this????
				//renderCell( g, (i - 1) * cellWidth, currY, cellWidth, cellHeight, rect, rendererLocation, element );
			}

			for( int i = endSeq + 1; i < topRightPosition; i++ ){

				rendererLocation.setLocation( i, j );

				// is code reuse really worth this????
				renderCell( g, (i - 1) * cellWidth, currY, cellWidth, cellHeight, rect, rendererLocation, null );
			}

			currY += cellHeight;
		}
	}


	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////



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
						this, selRect );
			}
		}
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////



	protected void renderCell ( Graphics g, int currX, int currY, int cellWidth, int cellHeight,
			Rectangle rect, SequenceAlignmentPoint rendererLocation, Element element ){
		//if( true ) return ;
		// not quite sure what this does, but it looks good. Hopefully
		// though it should stop naughty renderers from doing evil
		// things to the layout. 
		//g.setClip( currX, currY, cellWidth, cellHeight );
		//g.clipRect( rect.x, rect.y, rect.width, rect.height );

		Color bgColor = this.getColorMap().getColorAt(alignment, element, rendererLocation);
		boolean isSelected = selectionModel.isPointSelected( rendererLocation );
		boolean hasFocus = this.hasFocus();
		boolean isAtPoint = rendererLocation.equals( cursorLocation );

		if( renderer == null ){
			fastRenderer.renderAlignmentViewerCell
			( g, currX, currY, cellWidth, cellHeight,
					this, element, rendererLocation,
					bgColor, isSelected, hasFocus, isAtPoint );
		}
		else{      
			Component cell = renderer.getAlignmentViewerCellRendererComponent
					( this,
							element,
							rendererLocation,
							bgColor,
							isSelected,
							hasFocus, 
							isAtPoint );

			rendererPane.paintComponent( g, cell, this, currX, currY,
					cellWidth, cellHeight, true );

		}

		// support for an XOR cursor blink. If the we are drawing the
		// right cell just XOR it...
		/* No blinking
		if( getBlinkOn() && rendererLocation.equals( cursorBlinkPoint ) ){
			// now that we have drawn do the XOR as the blink is on.
			g.setXORMode( Color.white );
			g.fillRect( currX, currY, cellWidth, cellHeight );
			g.setPaintMode();
		}
		 */
	}





	public void mouseClicked(MouseEvent e)  {

		this.moveCursor(getSequencePointAtPoint(e.getPoint()));
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

	public SequenceAlignmentPoint getSequencePointAtPoint( Point point )
	{
		return getSequencePointAtPoint
				( point.x, point.y );
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


	public ISequenceAlignment getSequenceAlignment() {
		return this.alignment;
	}

	public void changeOccurred( AlignmentEvent event ) {
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
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}



	public int getScrollableUnitIncrement( Rectangle visibleRect, int orientation, int direction ) {
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

	/*  I think this is not needed
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
	 */


	// these methods tie the this class into its equivalent UI
	// delegate.
	/* I think this is not needed
	public String getUIClassID()
	{
		return uiClassID;
	}
	 */



	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	////////////////////////////////  Action and Event Listener
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


	public class SelectionClearer implements ActionListener
	{
		public void actionPerformed( ActionEvent event ) {

			JAlignmentViewer.this.clearSelection();
			JAlignmentViewer.this.selectingToggle = false;
		}
	}

	public class SelectionToggler implements ActionListener
	{
		public void actionPerformed( ActionEvent event )
		{
			if( JAlignmentViewer.this.selectingToggle ){
				JAlignmentViewer.this.selectingToggle = false;
			}
			else{
				JAlignmentViewer.this.selectingToggle = true;
				JAlignmentViewer.this.extendSelection( JAlignmentViewer.this.getPoint());
			}
		}
	}


	public class KeyScroller implements ActionListener {

		private JAlignmentViewer viewer;
		private int direction;

		public static final int UP = 1;
		public static final int DOWN = 2;
		public static final int RIGHT = 3;
		public static final int LEFT = 4;



		public KeyScroller( JAlignmentViewer viewer, int direction ) {

			this.viewer = viewer;
			this.direction = direction;
		}

		public void actionPerformed( ActionEvent event ) {

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

	public class PointListener implements ChangeListener {
		public void stateChanged( ChangeEvent event ) {
			JAlignmentViewer.this.cursorLocation = getPoint();

			if( JAlignmentViewer.this.selectingToggle ){
				JAlignmentViewer.this.extendSelection(getPoint() );
			}
			//setCursorBlink();  // No blinking currently
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////// Mouse events
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/*
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}


	@Override
	public void mousePressed(MouseEvent e) {
		// if button 2 is down, then we want to extend the selection 
		if( ( e.getModifiers() & InputEvent.BUTTON2_MASK ) == InputEvent.BUTTON2_MASK ){

			// clear the selection model it is not selecting. 
			if( ! this.selectionModel.isSelecting() ){

				this.selectionModel.clearSelection();
			}

			// signal the change
			this.selectionModel.extendSelection( new SequenceAlignmentPoint( getSequencePointAtPoint( e.getX(), e.getY() ) ) );

			// and remember this point.
			this.lastExtendedSelectionPoint.setLocation ( getSequencePointAtPoint( e.getX(), e.getY() ) );
		}
		else{
			// set the original sequence point at mouse down
			this.originalSequencePoint = new SequenceAlignmentPoint(getSequencePointAtPoint(e.getX(), e.getY()));
			// store the Y location to prevent drifting
			this.originalMouseY = e.getY();
		}
	}


	@Override
	public void mouseReleased(MouseEvent e) {

		// if button 2 is down, then we want to stop the selection
		if( ( e.getModifiers() & InputEvent.BUTTON2_MASK ) == InputEvent.BUTTON2_MASK ){
			this.selectionModel.stopSelection( new SequenceAlignmentPoint ( getSequencePointAtPoint( e.getX(), e.getY() ) ) );
		}
		else {
			// move the cursor to the current location
			if (currentSequencePoint != null) {

				this.moveCursor(currentSequencePoint);
			}
			// reset the variables
			originalSequencePoint = null;
			originalMouseY = 0;
			currentSequencePoint = null;
		}
	}

	@Override
	public void mouseDragged(MouseEvent e) { 

		// if button 2 is down, then we want to extend the selection 
		if( ( e.getModifiers() & InputEvent.BUTTON2_MASK ) == InputEvent.BUTTON2_MASK ) {

			// where have we dragged to. 
			SequenceAlignmentPoint nowAtPoint = getSequencePointAtPoint( e.getX(), e.getY() );

			// if drag has moved us into a new region signal an event
			if( !nowAtPoint.equals( lastExtendedSelectionPoint ) ){

				this.selectionModel.extendSelection( new SequenceAlignmentPoint( nowAtPoint ) );
				this.lastExtendedSelectionPoint.setLocation( nowAtPoint );
			}

		} else {

			// if we are still in the viewer
			if ((this.contains(e.getPoint())) || (this.originalSequencePoint != null)) {

				// get the current sequence point
				this.currentSequencePoint = new SequenceAlignmentPoint(getSequencePointAtPoint(e.getX(), originalMouseY));

				// the difference between the two points

				// We are not inserting or deleting gaps here
				//this.currentSequencePoint.getX() - this.originalSequencePoint.getX();
			}
		}
	}


	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	 */

	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//////////////////////////////// Cursor blinking stuff (currently not implemented)
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	/* 
	// (PENDING:- PL) These should be protected methods...
	private Rectangle blinkedCursor = new Rectangle();
	private SequenceAlignmentPoint cursorBlinkPoint;
	private boolean blinkOn = false;
	private CursorBlinkThread cursorThread;

	// I've put these into private methods so that I could debug when
	// the values were being changed
	private synchronized void setBlinkOn( boolean on ) {

		this.blinkOn = on;
	}

	private boolean getBlinkOn() {

		return this.blinkOn;
	}

	public synchronized void setCursorBlink() {

		if( this.cursorThread == null ){

			this.cursorThread = new CursorBlinkThread();
			this.cursorThread.start();
		}

		// cache the current 
		SequenceAlignmentPoint oldCursor = cursorBlinkPoint;

		//get the cursor position
		this.cursorBlinkPoint = this.getCursorModel().getPoint();

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



	class CursorBlinkThread extends Thread {

		public void run() {

			while( true ){
				try {
					sleep( 75 );
					unsetCursorBlink();
					waitImpl();

				} catch( InterruptedException t ){

					// Not doing anything here
				}
			}
		}

		public synchronized void waitImpl() throws InterruptedException {
			this.wait();
		}

		public synchronized void notifyImpl() {
			this.notify();
		}
	}

	 */

}


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
