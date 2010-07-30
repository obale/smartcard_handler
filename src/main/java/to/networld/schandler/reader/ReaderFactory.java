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
	
	public static int OMNIKEY_5x21_SMARTCARD = 0x00;
	public static int OMNIKEY_5x21_RFID = 0x01;
	
	/**
	 * Returns the n-th element of the reader list. You could use the static constant
	 * in this class to reference a special device. 
	 * 
	 * @param _readerNr The n-th element in the reader list.
	 * @return A {@link CardTerminal} element that encapsulates the reader otherwise null.
	 * @throws CardException {@link CardException}
	 */
	public static CardTerminal getReaderObject(int _readerNr) throws CardException {
		TerminalFactory factory = TerminalFactory.getDefault();
		List<CardTerminal> readerList = factory.terminals().list();
		if  ( readerList.size() >= _readerNr )
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
