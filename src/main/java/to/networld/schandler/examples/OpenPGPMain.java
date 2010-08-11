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

package to.networld.schandler.examples;

import javax.smartcardio.CardTerminal;
import javax.smartcardio.ResponseAPDU;

import to.networld.schandler.card.OpenPGP;
import to.networld.schandler.common.HexHandler;
import to.networld.schandler.factories.ReaderFactory;
import to.networld.schandler.interfaces.ICard;

/**
 * Example implementation of the methods to show how they could be used.
 * 
 * @author Alex Oberhauser
 */
public class OpenPGPMain {
	
	public static void testOpenPGPCard() throws Exception {
		CardTerminal terminal = ReaderFactory.getReaderObject(ReaderFactory.OMNIKEY_5x21_SMARTCARD);
		OpenPGP card = new OpenPGP(terminal, ICard.PROTOCOL_T1);
		
		while ( !card.connectToCard() ) {
			Thread.sleep(500);
		}
		ResponseAPDU dataAPDU = card.selectFile();
		System.out.println(OpenPGP.getResponseMessage(dataAPDU.getBytes()));
		
		System.out.println("AID      : " + card.getAID());
		System.out.println("URL      : " + card.getURL());
		System.out.println("LoginName: " + card.getLoginData());
		System.out.println("Gender   : " + card.getGender());
		System.out.println("Language : " + card.getLanguage());
		System.out.println("User Data: " + card.getUserData());
		System.out.println("Variable Data: " + HexHandler.getHexToAscii(card.getData((byte)0x00, (byte)0xC4)));
		card.disconnect(true);
	}
	
	/**
	 * The main class that is used to test the library during the development phase.
	 * 
	 * @param args Not used!
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		OpenPGPMain.testOpenPGPCard();
	}
}
