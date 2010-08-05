/**
 * SmartCard Handler Library
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

package to.networld.schandler.card;

import javax.smartcardio.CardTerminal;
import javax.smartcardio.ResponseAPDU;

import to.networld.schandler.common.HexHandler;

/**
 * @author Alex Oberhauser
 *
 */
public class ISO15693 extends AbstractCard {

	public static final byte[] GET_UID = new byte[] { (byte)0xFF, (byte)0xCA, (byte)0x00, (byte)0x00, (byte)0x00 };
	
	/**
	 * @param terminal
	 * @param protocol
	 */
	public ISO15693(CardTerminal terminal, String protocol) {
		super(terminal, protocol);
	}
	
	/**
	 * Needed because the data will be send in reverse order.
	 * 
	 * @param _inputArray A byte array to reverse.
	 * @return The input array in reverse order.
	 */
	private byte[] reverseArray(byte[] _inputArray) {
		int size = _inputArray.length;
		byte[] returnArray = new byte[size];
		for ( int i=size-1, j=0; i >= 0; i--, j++ ) {
			returnArray[j] = _inputArray[i];
		}
		return returnArray;
	}
	
	/**
	 * @return The UID of the RFID card.
	 * @throws Exception
	 */
	public synchronized String getUID() throws Exception {
		ResponseAPDU res = this.sendAPDUCommandToCard(GET_UID);
		String rawUID = HexHandler.getHexString(this.reverseArray(res.getData()));
		return rawUID;
	}
}
