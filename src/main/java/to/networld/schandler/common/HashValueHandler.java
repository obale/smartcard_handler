package to.networld.schandler.common;

import java.security.MessageDigest;

/**
 * A class that provides methods for the computation of hash value.<p/>
 * 
 * Supported:<p/>
 * 
 * <ul>
 *     <li> SHA-1 </li>
 *     <li> SHA-256 </li>
 *     <li> SHA-512 </li>
 * </ul>
 * 
 * @author Alex Oberhauser
 */
public class HashValueHandler {
	
	/**
	 * Computes the SHA-1 value.
	 * 
	 * @param _data Input data as string.
	 * @return The SHA-1 value of the input data.
	 * @throws Exception
	 */
	public static String computeSHA1(String _data) throws Exception {
	    MessageDigest md = MessageDigest.getInstance("SHA-1");
	    byte[] sha1hash = new byte[128];
	    md.update(_data.getBytes("UTF-8"), 0, _data.length());
	    sha1hash = md.digest();
	    return HexHandler.getHexString(sha1hash).replace(" ", "");
	}
	
	/**
	 * Computes the SHA-256 value.
	 * 
	 * @param _data Input data as string.
	 * @return The sha-256 value of the input data.
	 * @throws Exception
	 */
	public static String computeSHA256(String _data) throws Exception {
	    MessageDigest md = MessageDigest.getInstance("SHA-256");
	    byte[] sha256hash = new byte[256];
	    md.update(_data.getBytes("UTF-8"), 0, _data.length());
	    sha256hash = md.digest();
	    return HexHandler.getHexString(sha256hash).replace(" ", "");
	}
	
	/**
	 * Computes the SHA-512 value.
	 * 
	 * @param _data Input data as string.
	 * @return The SHA-512 value of the input data.
	 * @throws Exception
	 */
	public static String computeSHA512(String _data) throws Exception {
	    MessageDigest md = MessageDigest.getInstance("SHA-512");
	    byte[] sha512hash = new byte[512];
	    md.update(_data.getBytes("UTF-8"), 0, _data.length());
	    sha512hash = md.digest();
	    return HexHandler.getHexString(sha512hash).replace(" ", "");
	}
}
