/*
  Processing library for the touchatag RFID reader
  
  (c) copyright
  
  This library is free software; you can redistribute it and/or
  modify it under the terms of the GNU Lesser General Public
  License as published by the Free Software Foundation; either
  version 2.1 of the License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General
  Public License along with this library; if not, write to the
  Free Software Foundation, Inc., 59 Temple Place, Suite 330,
  Boston, MA  02111-1307  USA
 */

package touchatag;

import processing.core.PApplet;

/**
 *  
 * @author augusto esteves
 * 
 */
public class Touchatag {

	PApplet myParent;
	int numReaders;
	
	public final String VERSION = "0.1.0";	
	
	/**
	 * loads both the necessary dlls
	 */
	static {
        System.loadLibrary("libnfc");
        System.loadLibrary("touchatag");
    }

	/**
	 * the Constructors, usually called in the setup() method in your sketch to
	 * initialize and start the library. 
	 * 
	 */
	public Touchatag(PApplet theParent, int nReaders) {
		myParent = theParent;
		
		if (nReaders <= 0) {
			numReaders = 1;
		} else {
			numReaders = nReaders;
		}
	}
	
	public Touchatag(PApplet theParent) {
		myParent = theParent;
		numReaders = 1;
	}
		
	/**
	 * return the version of the library.
	 * 
	 * @return String
	 */
	public String version() {
		return VERSION;
	}
	
	/**
	 * return the number of touchatag readers actually connected.
	 * 
	 * @return int
	 */
	public int On() {
		int numOfReaders = nativeConnectedRFID(numReaders);
		return numOfReaders;
	}

	/**
	 * return the tags found on a touchatag reader.
	 * 
	 * @return String
	 */
	public String[] tagsOnReader(int i) {
		if (i >= nativeConnectedRFID(numReaders)) {
			String[] error = { "Error: Reader doesn't exist" };
			return error;
		} else {
			String[] tags = nativeTagID(i, numReaders);
			return tags;
		}
	}
	
	public native String[] nativeTagID(int i, int nReaders);
    public native int nativeConnectedRFID(int nReaders);
}