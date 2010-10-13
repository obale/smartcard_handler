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

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import to.networld.schandler.common.HashValueHandler;
import to.networld.schandler.common.HexHandler;
import to.networld.schandler.interfaces.ICard;

/**
 * Basic class that encapsulates the functions and values that are
 * the same for all cards. At least cards that implements the PC/SC
 * specification.
 * 
 * @author Alex Oberhauser
 *
 */
public class BasicCard implements ICard {
	protected Card card;
	private CardTerminal terminal;
	private String protocol;
	
	public static final byte[] GET_UID = new byte[] { (byte)0xFF, (byte)0xCA, (byte)0x00, (byte)0x00, (byte)0x00 };
	
	/**
	 * A abstract card object that provides the functions and values that are the same for
	 * all cards.
	 * 
	 * @param _terminal A reader that is related to this card.
	 * @param _protocol For example "T=0" or "T=1"
	 */
	public BasicCard(CardTerminal _terminal, String _protocol) {
		assert((_terminal != null) && (_protocol != null));
		this.terminal = _terminal;
		this.protocol = _protocol;
	}
	
	@Override
	public synchronized boolean connectToCard() throws CardException {
		if ( this.terminal.isCardPresent() ) {
			this.card = this.terminal.connect(this.protocol);
			return true;
		}
		return false;
	}
	
	@Override
	public synchronized void setTerminal(CardTerminal _terminal) {
		assert(_terminal != null);
		this.terminal = _terminal;
	}
	
	@Override
	public synchronized void setProtocol(String _protocol) {
		assert(_protocol != null);
		this.protocol = _protocol;
	}
	
	@Override
	public synchronized CardTerminal getTerminal() { return this.terminal; }
	
	@Override
	public synchronized String getProtocol() { return this.protocol; }
	
	@Override
	public synchronized Card getCard() { return this.card; }
	
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

	@Override
	public synchronized String getUID() throws Exception {
		ResponseAPDU res = this.sendAPDUCommandToCard(GET_UID);
		CardType type = this.getCardType();
		if ( type == CardType.ICODE1
				|| type == CardType.ICODEEPC 
				|| type == CardType.ICODESLI
				|| type == CardType.ICODEUID
				|| type == CardType.SRF55V02P
				|| type == CardType.SRF55V02S
				|| type == CardType.SRF55V10P
				|| type == CardType.SRF55V10S )
			return HexHandler.getHexString(this.reverseArray(res.getData()));
		else
			return HexHandler.getHexString(res.getData());
	}
	
	@Override
	public synchronized String getUIDHash(HASH_TYPE _hashType) throws Exception {
		switch (_hashType) {
			case SHA1:
				return HashValueHandler.computeSHA1(this.getUID().replace(" ", ""));
			case SHA256:
				return HashValueHandler.computeSHA256(this.getUID().replace(" ", ""));
			case SHA512:
				return HashValueHandler.computeSHA512(this.getUID().replace(" ", ""));
			default:
				return null;
		}
	}
	
	@Override
	public synchronized ResponseAPDU sendAPDUCommandToCard(byte[] _command) throws CardException {
		assert (this.card != null);
		CommandAPDU com = new CommandAPDU(_command);
		
		try {
			this.card.beginExclusive();
			CardChannel channel = this.card.getBasicChannel();
			ResponseAPDU response = channel.transmit(com);
			return response;
		} finally {
			this.card.endExclusive();
		}
	}

	@Override
	public synchronized ResponseAPDU sendAPDUCommandToCard(CommandAPDU _command) throws CardException {
		assert (this.card != null);
		
		try {
			this.card.beginExclusive();
			CardChannel channel = this.card.getBasicChannel();
			ResponseAPDU response = channel.transmit(_command);
			return response;
		} finally {
			this.card.endExclusive();
		}
	}
	
	@Override
	public synchronized void  disconnect(boolean _reset) throws CardException {
		this.card.disconnect(_reset);
	}
	
	@Override
	public synchronized CardType getCardType() {
		if ( this.card == null) return null;
		byte[] atr = this.card.getATR().getBytes();
		
		int typeNr = (int)atr[14];
		CardType[] values = CardType.values();
		if ( typeNr < values.length )
			return values[typeNr];
		else
			return CardType.UNKNOWN;
	}
}
