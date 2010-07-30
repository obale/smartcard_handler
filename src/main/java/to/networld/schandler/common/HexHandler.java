/**
 * Semantic Crawler Library
 *
 * Copyright (C) 2010 by Networld Project
 * Written by Alex Oberhauser <oberhauseralex@networld.to>
 * All Rights Reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation, version 3 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this software.  If not, see <http://www.gnu.org/licenses/>
 */

package to.networld.schandler.common;

import java.math.BigInteger;

/**
 * @author Alex Oberhauser
 *
 */
public abstract class HexHandler {
	
	/**
	 * Converts a byte array in a String.
	 * 
	 * @param _byteArray A array of bytes.
	 * @return The String representation of the byte Array.
	 * @throws Exception
	 */
	public static String getHexString(byte[] _byteArray) throws Exception {
		String result = new String();
		for (int i=0; i < _byteArray.length; i++) {
			result += Integer.toString( ( _byteArray[i] & 0xff ) + 0x100, 16).substring(1);
		}
		return result;
	}
	
	public static byte getByte(int _value) throws Exception {
		if ( _value > 255 ) throw new NumberFormatException("The number is to big!");
		String hexValue = Integer.toHexString(_value);
		byte[] retValues = new BigInteger(hexValue, 16).toByteArray();
		if ( retValues.length == 2 )
			return retValues[1];
		else 
			return retValues[0];
	}
}
