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

package to.networld.schandler.reader;

import java.util.List;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;

/**
 * Factory class that provides the reader objects.
 * 
 * @author Alex Oberhauser
 *
 */
public abstract class ReaderFactory {
	
	/** Omnikey 5x21 smart card reader slot. */
	public static int OMNIKEY_5x21_SMARTCARD = 0x00;
	/** Omnikey 5x21 RFID reader slot. */
	public static int OMNIKEY_5x21_RFID = 0x01;
	
	/**
	 * Returns the n-th element of the reader list. You could use the static constant
	 * in this class to reference a special device. 
	 * 
	 * @param _readerNr The n-th element in the reader list.
	 * @return A {@link CardTerminal} element that encapsulates the reader otherwise null.
	 * @throws CardException {@link CardException}
	 */
	public synchronized static CardTerminal getReaderObject(int _readerNr) throws CardException {
		TerminalFactory factory = TerminalFactory.getDefault();
		List<CardTerminal> readerList = factory.terminals().list();
		if  ( readerList.size() > _readerNr )
			return readerList.get(_readerNr);
		else
			return null;
	}
	
	/**
	 * Returns all found reader objects in a list.
	 * 
	 * @return A list of all found readers.
	 * @throws CardException {@link CardException}
	 */
	public static List<CardTerminal> getAllReaderObjects() throws CardException {
		TerminalFactory factory = TerminalFactory.getDefault();
		return factory.terminals().list();
	}
}
