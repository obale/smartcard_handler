package to.networld.schandler.card;

import javax.smartcardio.CardTerminal;
import javax.smartcardio.ResponseAPDU;

import to.networld.schandler.common.HexHandler;

/**
 * @author Alex Oberhauser
 *
 */
public class IClass extends AbstractCard {

	public static final byte[] GET_UID = new byte[] { (byte)0xFF, (byte)0xCA, (byte)0x00, (byte)0x00, (byte)0x00 };
	
	/**
	 * @param terminal
	 * @param protocol
	 */
	public IClass(CardTerminal terminal, String protocol) {
		super(terminal, protocol);
	}
	
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
	
	/**
	 * @return The UID of the RFID card.
	 * @throws Exception
	 */
	public synchronized String getUID() throws Exception {
		ResponseAPDU res = this.sendAPDUCommandToCard(GET_UID);
		String rawUID = HexHandler.getHexString(this.reverseArray(res.getData()));
		return rawUID;
	}
}
