package to.networld.schandler.card;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;

/**
 * @author Alex Oberhauser
 *
 */
public class Mifare1K extends AbstractMifare {
	
	public final int MAX_BLOCKS = 64;
	
	/*
	 * Data blocks that are writable.
	 */
	public final byte[] USER_DATA_FIELDS = new byte[] { 
		            (byte)0x01, (byte)0x02, 
		(byte)0x04, (byte)0x05, (byte)0x06,
		(byte)0x08, (byte)0x09, (byte)0x0A,
		(byte)0x0C, (byte)0x0D, (byte)0x0E,
		(byte)0x10, (byte)0x11, (byte)0x12,
		(byte)0x14, (byte)0x15, (byte)0x16,
		(byte)0x18, (byte)0x19, (byte)0x2a,
		(byte)0x1c, (byte)0x1d, (byte)0x1e,
		(byte)0x20, (byte)0x21, (byte)0x22,
		(byte)0x24, (byte)0x25, (byte)0x26,
		(byte)0x28, (byte)0x29, (byte)0x2a,
		(byte)0x2c, (byte)0x2d, (byte)0x2e,
		(byte)0x30, (byte)0x31, (byte)0x32,
		(byte)0x34, (byte)0x35, (byte)0x36,
		(byte)0x38, (byte)0x39, (byte)0x3a,
		(byte)0x3c, (byte)0x3d, (byte)0x3e };
	
	/**
	 * @param terminal
	 * @param protocol
	 * @throws CardException
	 */
	public Mifare1K(CardTerminal terminal, String protocol) throws CardException {
		super(terminal, protocol);
	}

}
