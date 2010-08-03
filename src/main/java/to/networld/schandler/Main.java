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

package to.networld.schandler;

import javax.smartcardio.CardTerminal;
import javax.smartcardio.ResponseAPDU;

import to.networld.schandler.card.AbstractCard;
import to.networld.schandler.card.Mifare1K;
import to.networld.schandler.card.OpenPGP;
import to.networld.schandler.common.HexHandler;
import to.networld.schandler.reader.ReaderFactory;

/**
 * @author Alex Oberhauser
 */
public class Main {
	static Mifare1K card = null;
	
	public static void writeMifare1KCard(String _data) throws Exception {
		card.formatCard(Mifare1K.KEY_A, Mifare1K.STD_KEY, (byte)0x01);
		card.writeData(Mifare1K.KEY_A,
				Mifare1K.STD_KEY,
				(byte)0x01,
				_data.getBytes());
	}
	
	public static void readMifare1KCardString() throws Exception {
		String data = card.readData(Mifare1K.KEY_A, Mifare1K.STD_KEY, (byte)0x01);
		System.out.println("\t[DATA] " + data);
	}
	
	public static void readMifare1KCardBytes() throws Exception {
		for (int count=0; count < 64; count++) {
			ResponseAPDU readData = card.readBlockData(Mifare1K.KEY_A,
					Mifare1K.STD_KEY,
					(byte)0x00,
					HexHandler.getByte(count),
					(byte)0x01);
			System.out.println("\t[" + HexHandler.getByteToString(HexHandler.getByte(count)) + "] " + HexHandler.getHexString(readData.getData()));
		}
	}

	public static void initMifare1KCard() throws Exception {
		CardTerminal terminal = ReaderFactory.getReaderObject(ReaderFactory.OMNIKEY_5x21_RFID);
		card = new Mifare1K(terminal, AbstractCard.PROTOCOL_T1);
		
		while ( !card.connectToCard() ) {
			Thread.sleep(500);
		}

		String currentKey = card.getUID();
		System.out.println("\t[UID]  " + currentKey);
	}
	
	public static void testOpenPGPCard() throws Exception {
		CardTerminal terminal = ReaderFactory.getReaderObject(ReaderFactory.OMNIKEY_5x21_SMARTCARD);
		OpenPGP card = new OpenPGP(terminal, AbstractCard.PROTOCOL_T1);
		
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
		System.out.println("[*] Waiting for a RFID card    ...");
		Main.initMifare1KCard();
		System.out.println("[*] Reading out stored data    ...");
		Main.readMifare1KCardString();
		System.out.println("[*] Raw data                   ...");
		Main.readMifare1KCardBytes();
		Main.card.disconnect(false);
		
//		Main.testOpenPGPCard();
	} 

}
