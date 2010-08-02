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

package to.networld.schandler.card;

import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

/**
 * Abstract class that encapsulates the functions and values that are
 * the same for all cards.
 * 
 * @author Alex Oberhauser
 *
 */
public abstract class AbstractCard {
	protected Card card;
	private CardTerminal terminal;
	private String protocol;
	
	public static final String PROTOCOL_T0 = "T=0";
	public static final String PROTOCOL_T1 = "T=1";
	
	/**
	 * A abstract card object that provides the functions and values that are the same for
	 * all cards.
	 * 
	 * @param _terminal A reader that is related to this card.
	 * @param _protocol For example "T=0" or "T=1"
	 */
	public AbstractCard(CardTerminal _terminal, String _protocol) {
		assert((_terminal != null) && (_protocol != null));
		this.terminal = _terminal;
		this.protocol = _protocol;
	}
	
	public synchronized boolean connectToCard() throws CardException {
		if ( this.terminal.isCardPresent() ) {
			this.card = this.terminal.connect(this.protocol);
			return true;
		}
		return false;
	}
	
	/**
	 * Possible to change the related reader for this card.
	 * null value is NOT permitted.
	 * 
	 * @param _terminal A object that encapsulates the reader.
	 */
	public synchronized void setTerminal(CardTerminal _terminal) {
		assert(_terminal != null);
		this.terminal = _terminal;
	}
	
	/**
	 * Possible to change the related protocol for this card.
	 * null value is NOT permitted.
	 * 
	 * @param _protocol The protocol as String value.
	 */
	public synchronized void setProtocol(String _protocol) {
		assert(_protocol != null);
		this.protocol = _protocol;
	}
	
	/**
	 * @return The current related reader for this card.
	 */
	public synchronized CardTerminal getTerminal() { return this.terminal; }
	
	/**
	 * @return The current related protocol for this card.
	 */
	public synchronized String getProtocol() { return this.protocol; }
	
	/**
	 * @return The card that is related to this object.
	 */
	public synchronized Card getCard() { return this.card; }
	
	/**
	 * Send a command to the card. For example to read out values or to write
	 * data to the card.
	 * 
	 * @param _command The command encoded in bytes.
	 * @return A {@link ResponseAPDU} that encapsulates the response.
	 * @throws CardException
	 */
	public synchronized ResponseAPDU sendAPDUCommandToCard(byte[] _command) throws CardException {
		assert (this.card != null);
		CommandAPDU com = new CommandAPDU(_command);
		
		this.card.beginExclusive();
		CardChannel channel = this.card.getBasicChannel();
		ResponseAPDU response = channel.transmit(com);
		this.card.endExclusive();
		
		return response;
	}
	
	public synchronized ResponseAPDU sendAPDUCommandToCard(CommandAPDU _command) throws CardException {
		assert (this.card != null);
		
		this.card.beginExclusive();
		CardChannel channel = this.card.getBasicChannel();
		ResponseAPDU response = channel.transmit(_command);
		this.card.endExclusive();
		
		return response;
	}
}
