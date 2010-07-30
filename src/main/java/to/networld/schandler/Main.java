package to.networld.schandler;

import javax.smartcardio.CardTerminal;
import javax.smartcardio.ResponseAPDU;

import to.networld.schandler.card.Mifare1K;
import to.networld.schandler.common.HexHandler;
import to.networld.schandler.reader.ReaderFactory;

/**
 * @author Alex Oberhauser
 *
 */
public class Main {

	/**
	 * The main class that is used to test the library during the development phase.
	 * 
	 * @param args Not used!
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		CardTerminal terminal = ReaderFactory.getReaderObject(ReaderFactory.OMNIKEY_5x21_RFID);
		Mifare1K card = new Mifare1K(terminal, Mifare1K.PROTOCOL_T1);
		
		while ( !card.connectToCard() ) {
			Thread.sleep(500);
		}
		String currentKey = card.getUID();
		System.out.println("Card UID    : " + currentKey);
		
		ResponseAPDU readData = card.readData(Mifare1K.KEY_A,
				Mifare1K.STD_KEY,
				(byte)0x00,
				(byte)0x00,
				(byte)0x1B);
		System.out.println("Data Block:");
		System.out.println("\tStatus Message: " + Mifare1K.getResponseMessage(readData.getBytes()));
		System.out.println("\tStatus Code   : " + HexHandler.getHexString(readData.getBytes()));
		System.out.println("\tData          : " + HexHandler.getHexString(readData.getData()));
	}

}
