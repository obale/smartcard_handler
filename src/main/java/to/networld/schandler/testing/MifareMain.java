package to.networld.schandler.testing;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.ResponseAPDU;

import to.networld.schandler.card.BasicMifare;
import to.networld.schandler.common.HexHandler;
import to.networld.schandler.factories.ReaderFactory;

/**
 * @author Alex Oberhauser
 */
public class MifareMain {
	private static boolean DEBUG = true;
	private static BasicMifare card = null;
	
	public static void writeMyFOAFFile() throws Exception {
		card.formatCard(BasicMifare.KEY_A, BasicMifare.STD_KEY, (byte)0x01);
		card.writeData(BasicMifare.KEY_A, BasicMifare.STD_KEY, (byte)0x01, "http://devnull.networld.to/foaf.rdf#me".getBytes());
	}
	
	public static void readMifare1KCardBytes() throws Exception {
		for (int count=0; count < card.MAX_BLOCKS; count++) {
			ResponseAPDU readData = card.readBlockData(BasicMifare.KEY_A,
					BasicMifare.STD_KEY,
					(byte)0x00,
					HexHandler.getByte(count),
					(byte)0x01);
			System.out.println("\t[" + HexHandler.getByteToString(HexHandler.getByte(count)) + "] " + HexHandler.getHexString(readData.getData()));
		}
	}
	
	public static String readMifare1KCardString() throws Exception {
		String data = card.readData(BasicMifare.KEY_A, BasicMifare.STD_KEY, (byte)0x01);
		return data;
	}
	
	public static void readRFIDCard(CardTerminal _terminal) throws Exception {
		System.out.println("[*] Waiting for card   ...");
		
		
		while ( !_terminal.isCardPresent() ) {
			System.out.println("<<<< Please input a card and press then ENTER ... ");
			while ( System.in.read() != '\n'); 
		}
		card = new BasicMifare(_terminal, BasicMifare.PROTOCOL_T1);
		
		String data = readMifare1KCardString();
		String currentUID = card.getUID();
		System.out.println("[*] UID                " + currentUID);
		System.out.println("[*] Card Type          " + card.getCardType());
		System.out.println("[*] Data on the card   " + data);
		if ( DEBUG ) {
			System.out.println("---- DEBUG Raw Data ----");
			MifareMain.readMifare1KCardBytes();
			System.out.println("------------------------");
			System.out.println();
		}

		card.disconnect(true);
	}
	
	public static void main(String[] args) throws Exception {
		if ( args.length > 0)
			try { DEBUG = Boolean.parseBoolean(args[0]); } catch (Exception e) {}
				
		System.out.print("[*] Waiting for reader ...");
		CardTerminal terminal = null;
		boolean error = false;
		do { 
			try {
				terminal = ReaderFactory.getReaderObject(ReaderFactory.OMNIKEY_5x21_RFID);
				if ( terminal != null )
					error = false;
			} catch (CardException e) {
				error = true;
				System.out.print(".");
				Thread.sleep(1000);
			}
		} while ( error );
		System.out.println();
		
		while ( true ) {
			MifareMain.readRFIDCard(terminal);
			System.out.print("<<<< Please press ENTER to scan for another card... ");
			while ( System.in.read() != '\n'); 
		}

	}
}
