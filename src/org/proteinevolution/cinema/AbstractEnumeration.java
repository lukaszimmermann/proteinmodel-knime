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

import java.util.Hashtable;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ConcurrentModificationException;


// TODO This class should be replaced by the native Java Enumeration


/**
 * AbstractEnumeration.java
 * 
 * Provides support for Enumerated Types in Java. This class provides
 * several methods useful for all Enumerated Types including a
 * sensible printable toString method, the total
 * number of instances of a given type, an Iterator through all the
 * types, and an ordinal number running from 0 upwards for each type. <p>
 *
 * This class is used by extending it with a new class which 
 * <ul>
 *    <li>is declared final, which prevents subclasses from
 *     introducing new instances</li>
 *    <li>has a private constructor</li>
 *    <li>declares a public static final data member for each instance
 *     that is required</li>
 * </ul>
 *
 * So for example
 *
 * <code>
 *   <pre>
 * public final class TrafficLight extends AbstractEnumeration
 * {
 *   private TrafficLight( String toString ){
 *   {
 *     super( toString );
 *   }
 *
 *   public static final TrafficLight RED 
 *      = new TrafficLight( "TrafficLight Enumerated Type:- RED" );
 *   public static final TrafficLight ORANGE 
 *      = new Traffic( "TrafficLight Enumerated Type:- ORANGE" );
 *   public static final TrafficLight GREEN 
 *      = new Traffic( TrafficLight Enumerated Type:- GREEN" );
 * }
 *
 *    </pre>
 * </code>
 *
 * Currently this class can not be serialised. Having one of the
 * subclasses implement Serializable would be a mistake as it would
 * provide an alternative route for the instances of the class to be
 * produced. This could be circumvented using the
 * replaceObject/writeObject methods introduced in the 1.2
 * serialisation spec, but I haven't got around to implementing this
 * yet!
 *
 * It should be noted that there are problems in compiling this class
 * with some versions of javac. This is bug in javac (Bug ID:4157676),
 * not my code which is perfectly legal java. Jikes works
 * fine. Alternatively you can comment out the references to the ord
 * variable and do without this functionality, or make it non final,
 * in which case attempts to alter it will no longer produce compiler
 * errors as they should. 
 *
 * Created: Mon Feb 21 14:11:41 2000
 *
 * @author Phillip Lord
 * @version $Id: AbstractEnumeration.java,v 1.9 2001/04/11 17:04:43 lord Exp $
 */

public abstract class AbstractEnumeration 
{
  // A linked list of all these types.
  private String toString;
  private AbstractEnumeration next;
  private AbstractEnumeration prev;
  
  // private static hashtables, which support the enum
  // implementation. We are using hashtables because they are
  // synchronized and they need to be for this!
  private static Hashtable upperBoundHash = new Hashtable();
  private static Hashtable firstHash = new Hashtable();
  private static Hashtable lastHash = new Hashtable();
  private static Hashtable currentTopOrd = new Hashtable();
  

  // ordinal number. Very useful. 
  public final int ord; //if this does not compile its a bug in javac. Removing "final" should make it work!!!!
  
  protected AbstractEnumeration( String toString )
  {
    super();
    
    this.toString = toString;
    
    // sort the ordinal number for this type. 
    // first retrieve it (if it exists) from the hash
    Object upBound = upperBoundHash.get( getClass() );
    int upperBound;
    
    if( upBound == null ){
      upperBound = 0;
    }
    else{
      upperBound = ((Integer)upBound).intValue();
    }
    
    // make the upper bound recoverable. Sadly this causes javac to
    // croak, due to a bug in javac (see for instance Bug ID:
    // 4157676). As this utility is vital to the class I have decided
    // to incorporate it anyway, and simply compile with Jikes. The
    // bug should be fixed as of the 1.3 release. 
    ord = upperBound;
    // then return the advanced upper bound to the hash
    upperBoundHash.put( getClass(), new Integer( ++upperBound ) );
    
    
    // next step is to make add to (or create) a linked list
    // start and end elements in the appropriate elements
    Object firstObj = firstHash.get( getClass() );
    Object lastObj = lastHash.get( getClass() );
    
    AbstractEnumeration first = (firstObj == null)? null:((AbstractEnumeration)firstObj);
    AbstractEnumeration last = (lastObj == null)? null:((AbstractEnumeration)lastObj);
    
    // sort out the linked list that we use to store all of the
    // Elements
    if( first == null ){
      first = this;
      firstHash.put( getClass(), first );
    }
    
    if( last != null ){
      this.prev = last;
      last.next = this;
    }
    
    last = this;
    lastHash.put( getClass(), last );    
  }

  // these methods are enum support
  public static Iterator iterator( Class cla )
  {
    return new ElementIterator( cla );
  }
  
  public static class ElementIterator implements Iterator
  {
    // we need to store the size so that we can detect any concurrent
    // modifications. This should only happen if things go badly
    // wrong. 
    int size;
    
    //store the current position
    private AbstractEnumeration curr;
    
    public ElementIterator( Class cla )
    {
      //store the size of this enum
      size = ((Integer)upperBoundHash.get( cla )).intValue();
    
      //store the current element
      curr = (AbstractEnumeration)firstHash.get( cla );
    }
    
    public boolean hasNext()
    {
      if( curr != null ) return true;
      
      return false;
    }
    
    public Object next()
    {
      if( getSize( curr.getClass() ) != size ) throw new ConcurrentModificationException
	( "The total number of elements has changed, which means bad things" );
      
      if( curr == null ) throw new NoSuchElementException
	( "Attempt to iterate past last Element" );
      AbstractEnumeration retn = curr;
      curr = curr.next;
      return retn;
    }
    
    public void remove()
    {
      throw new UnsupportedOperationException( "Removing elements not allowed" );
    }
  }
  
  public static AbstractEnumeration[] getAllElements( Class cla )
  {
    // should I cache these. Maybe....
    AbstractEnumeration[] allElements = new AbstractEnumeration[ getSize( cla ) ];
    
    Iterator iter = iterator( cla );
    int count = 0;
    while( iter.hasNext() ){
      allElements[ count++ ] = (AbstractEnumeration)iter.next();
    }
    return allElements;
  }
  
  public static int getSize( Class cla )
  {
    return ((Integer)upperBoundHash.get( cla )).intValue();
  }
  
  public String toString()
  {
    return toString;
  }
} // AbstractEnumeration



/*
 * ChangeLog
 * $Log: AbstractEnumeration.java,v $
 * Revision 1.9  2001/04/11 17:04:43  lord
 * Added License agreements to all code
 *
 * Revision 1.8  2001/02/19 17:48:20  lord
 * Added to documentation
 *
 * Revision 1.7  2000/12/13 16:36:01  lord
 * Cosmetic
 *
 * Revision 1.6  2000/10/31 15:51:11  lord
 * Improved documentation
 *
 * Revision 1.5  2000/07/18 12:39:45  lord
 * Documentation enhancement
 *
 * Revision 1.4  2000/05/15 16:17:58  lord
 * Have made final ordinal number available for all instances.
 * I wrote the code for this previously, but it wouldnt compile
 * through what has turned out to be a javac bug. Now that I know that
 * I really want this functionality I have decided to put it back in
 * despite the inconvienience.
 *
 * Revision 1.3  2000/05/10 15:00:09  lord
 * Added ordinal number support to this class.
 * Actually I already had ordinal support but for some strange
 * reason I had not let the world see it. What was going on in my
 * mind at that stage eh?
 *
 * Revision 1.2  2000/03/08 17:26:36  lord
 * To many changes to document
 *
 * Revision 1.1  2000/02/28 15:59:37  lord
 * Initial Checkin
 *
 * Revision 1.1  2000/02/21 16:48:22  lord
 * Initial checkin
 *
 */
