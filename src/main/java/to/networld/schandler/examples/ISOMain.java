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

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;

import to.networld.schandler.card.ISO15693;
import to.networld.schandler.factories.ReaderFactory;

/**
 * @author Alex Oberhauser
 *
 */
public class ISOMain {
	private static boolean DEBUG = true;
	private static ISO15693 card = null;

	private static void readISOCard(CardTerminal _terminal) throws Exception {
		System.out.println("[*] Waiting for card   ...");
		card = new ISO15693(_terminal, ISO15693.PROTOCOL_T1);
		while ( !card.connectToCard() ) {
			System.out.println("<<<< Please input a card and press then ENTER ... ");
			while ( System.in.read() != '\n'); 
		}
		
		String currentUID = card.getUID();
		if ( DEBUG ) {
			System.out.println("[*] UID                " + currentUID);
			System.out.println("[*] Card Type          " + card.getCardType());
		}
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		if ( args.length > 0)
			try { DEBUG = Boolean.parseBoolean(args[0]); } catch (Exception e) {}
			
		System.out.print("[*] Waiting for reader ...");
		CardTerminal terminal = null;
		boolean error = false;
		do { 
			try {
				terminal = ReaderFactory.getReaderObject(ReaderFactory.OMNIKEY_5x21_RFID);
				if ( terminal != null)
					error = false;
			} catch (CardException e) {
				error = true;
				System.out.print(".");
				Thread.sleep(1000);
			}
		} while ( error );
		System.out.println();
		
		while ( true ) {
			ISOMain.readISOCard(terminal);
			System.out.print("<<<< Please press ENTER to scan for another card... ");
			while ( System.in.read() != '\n'); 
		}
	}

}
