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
