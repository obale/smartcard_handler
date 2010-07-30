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
