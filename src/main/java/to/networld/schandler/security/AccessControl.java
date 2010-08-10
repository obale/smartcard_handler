/**
 * SmartCard Access Library
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

package to.networld.schandler.security;

import java.sql.SQLException;
import java.util.Random;
import java.util.Vector;

import javax.smartcardio.ResponseAPDU;

import to.networld.schandler.card.BasicMifare;
import to.networld.schandler.interfaces.ICard.HASH_TYPE;
import to.networld.schandler.common.HashValueHandler;
import to.networld.schandler.common.HexHandler;

/**
 * Access Control class for the handling different checks if the card
 * is permitted to access the system. Additional fine granulated access
 * rights are returned.
 * 
 * @author Alex Oberhauser
 */
public class AccessControl {
	/**
	 * Access Control List
	 * TODO: Think about real use scenarios and update the enumeration.
	 */
	public enum ACL {
		READ,
		WRITE,
		UPDATE,
		READ_WRITE,
		ALL
	}

	private final AccessHandler accessHandler;
	private final KeyHandler keyHandler;
	
	/**
	 * 
	 * @param _filePath Where to store the key and access database. For example: /tmp
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public AccessControl(String _filePath) throws ClassNotFoundException, SQLException {
		String filePath;
		if ( _filePath.endsWith("/") )
			filePath = _filePath;
		else
			filePath = _filePath + "/";
		this.keyHandler = new KeyHandler(filePath + "acl.db");
		this.accessHandler = new AccessHandler(filePath + "accessRights.db");
	}
	
	/**
	 * This method checks if the stored hash value on the card matches the key in the database.
	 * Additional a second database is queried to read out the access rights. If one of the two
	 * checks fails the method returns NULL.
	 * 
	 * @param _card The card that is connected to the reader.
	 * @return The access rights as special enumeration block or if not permitted NULL.
	 * @throws Exception
	 */
	public ACL getAccessRight(BasicMifare _card) throws Exception {
		if ( !checkKey(_card) )
			return null;
		ACL[] rights = ACL.values();
		int ordinal = this.accessHandler.getAccessRights(_card.getUIDHash(HASH_TYPE.SHA512));
		if ( ordinal == -1 ) return null;
		return rights[ordinal];
	}
	
	/**
	 * This method should return a random String with random length.
	 * 
	 * @return A random generated String with numbers as content and random length.
	 */
	private String returnUniqueToken() {   
	    Random rand = new Random(System.currentTimeMillis());
	    Random randLength = new Random();
	    
	    StringBuffer strBuffer = new StringBuffer();
	    int loopLength = 128 + randLength.nextInt(256);
	    for (int i = 0; i < loopLength; i++) {
	    	int pos = rand.nextInt(Integer.MAX_VALUE);
	        strBuffer.append(pos);
	    }
	    return strBuffer.toString();
	}
	
	/**
	 * Generates a hash value for the generated key. The returned value should
	 * be written to the card.
	 * 
	 * @param _cardUID The unique identifier of the RFID card.
	 * @return The hash value of the key that should be written on the card.
	 * @throws Exception 
	 */
	private String generateKey(String _cardUID) throws Exception {
		String UIDHash = HashValueHandler.computeSHA512(_cardUID.replace(" ", ""));
		_cardUID = null;
		String uniqueToken = this.returnUniqueToken();
		this.keyHandler.storeKey(UIDHash, uniqueToken);
		return HashValueHandler.computeSHA512(uniqueToken);
	}
	
	public boolean checkKey(BasicMifare _card) throws Exception {
		String value = this.keyHandler.getKey(_card.getUIDHash(HASH_TYPE.SHA512));
		if ( value == null ) return false;
		String key = this.readKey(_card);
		if ( key.equals(HashValueHandler.computeSHA512(value)) ) return true;
		return false;
	}
	
	/**
	 * Read out the hash value of the key that was stored on the card.
	 * 
	 * @param _card The card that is connected to the reader.
	 * @return The hash value of the key that was stored on the card. 
	 * @throws Exception
	 */
	private String readKey(BasicMifare _card) throws Exception {
		String key = new String();
		for ( int count = 38; count < _card.USER_DATA_FIELDS.length; count++ ) {
			ResponseAPDU response = _card.readBlockData(BasicMifare.KEY_A, BasicMifare.STD_KEY, (byte)0x00, _card.USER_DATA_FIELDS[count], (byte)0x01);
			byte[] byteArray = response.getData();
			if ( byteArray[0] == (byte)0x00 ) break;
			key += HexHandler.getHexToAscii(byteArray);
		}
		return key;
	}
	
	/**
	 * Generates a random key and writes it to the internal structure and the
	 * SHA-512 value to the card.
	 * 
	 * @param _card An element that represents the RFID card of the type Mifare 1K/4K.
	 * @return True if the changing was successful otherwise False
	 * @throws Exception
	 */
	public boolean changeKey(BasicMifare _card) throws Exception {
		String cardUID = _card.getUID();
		if ( !this.checkKey(_card) ) return false;
		String cardKey = this.generateKey(cardUID);
		Vector<byte[]> blocks = HexHandler.splitArray(cardKey.getBytes());
		for ( int count = 38; count < _card.USER_DATA_FIELDS.length; count++ ) {
			if ( blocks.isEmpty() ) break;
			_card.writeBlockData(BasicMifare.KEY_A, BasicMifare.STD_KEY, (byte)0x00, _card.USER_DATA_FIELDS[count], (byte)0x01, blocks.firstElement());
			blocks.remove(0);
		}
		return true;
	}
	
	/**
	 * ATTENTION: This function is dangerous because you could initiate a card without
	 *            checking if the calling program has rights to do so.
	 *            Please assure on your own that only permitted entities are able to 
	 *            initiate a new card.
	 * 
	 * @param _card The card that should be initiated.
	 * @return True if the procedure was successful, otherwise False.
	 * @throws Exception
	 */
	public boolean initNewCard(BasicMifare _card, ACL _right) throws Exception {
		String oldKey = this.keyHandler.getKey(_card.getUIDHash(HASH_TYPE.SHA512));
		String cardKey;
		if ( oldKey == null ) {
			cardKey = this.generateKey(_card.getUID());
		} else {
			cardKey = HashValueHandler.computeSHA512(oldKey);
		}
		Vector<byte[]> blocks = HexHandler.splitArray(cardKey.getBytes());
		for ( int count = 38; count < _card.USER_DATA_FIELDS.length; count++ ) {
			if ( blocks.isEmpty() ) break;
			_card.writeBlockData(BasicMifare.KEY_A, BasicMifare.STD_KEY, (byte)0x00, _card.USER_DATA_FIELDS[count], (byte)0x01, blocks.firstElement());
			blocks.remove(0);
		}
		this.accessHandler.storeAccessRights(_card.getUIDHash(HASH_TYPE.SHA512), _right.ordinal());
		return true;
	}
}
