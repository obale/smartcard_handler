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

/**
 * Implements functionality of RFID card implemented with the 
 * ISO 15693 specification.<p/>
 * 
 * The {@link BasicCard#getUID()} method from the super class 
 * is able to detect the card type and reverse the gained UID.<p/>
 * 
 * <b>TODO:</b> Add more specialized methods for the card handling. 
 *              At the moment is only possible to read out the UID 
 *              from the card.
 * 
 * @author Alex Oberhauser
 */
public class ISO15693 extends BasicCard {
	
	/**
	 * @param terminal
	 * @param protocol
	 */
	public ISO15693(CardTerminal terminal, String protocol) {
		super(terminal, protocol);
	}
	
}
