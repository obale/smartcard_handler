package to.networld.schandler.interfaces;

import javax.smartcardio.Card;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

/**
 * Interface that offers basic functionality for the handling of cards that
 * are able to communicate on the base of the PC/SC 2.0 specification.
 * 
 * @author Alex Oberhauser
 *
 */
public interface ICard {
	
	public static final String PROTOCOL_T0 = "T=0";
	public static final String PROTOCOL_T1 = "T=1";
	
	/**
	 * Supported hash types.
	 */
	public enum HASH_TYPE {
		SHA1,
		SHA256,
		SHA512
	}
	
	/**
	 * Enumeration that includes the different card types, sorted by ID.
	 */
	public enum CardType {
		NoName,
		Mifare1K,
		Mifare4K,
		MifareUltraLight,
		SLE55R_XXXX,
		XXX,
		SR176,
		SRIX4K,
		AT88RF020,
		AT88SC0204CRF,
		AT88SC0808CRF,
		AT88SC1616CRF,
		AT88SC3216CRF,
		AT88SC6416CRF,
		SRF55V10P,
		SRF55V02P,
		SRF55V10S,
		SRF55V02S,
		TAG_IT,
		LRI512,
		ICODESLI,
		TEMPSENS,
		ICODE1,
		PicoPass2K,
		PicoPass2KS,
		PicoPass16K,
		PicoPass16Ks,
		PicoPass16K_x2, 
		PicoPass16KS_x2,  
		PicoPass32KS_6_16, 
		PicoPass32KS_6_8x2,
		PicoPass32KS_x2_16,
		PicoPass32KS_x2_8x2,
		LRI64,
		ICODEUID,
		ICODEEPC,
		LRI12,
		LRI128,
		MifareMini,
		UNKNOWN
	}
	
	public boolean connectToCard() throws CardException;
	
	/**
	 * Possible to change the related reader for this card.
	 * null value is NOT permitted.
	 * 
	 * @param _terminal A object that encapsulates the reader.
	 */
	public void setTerminal(CardTerminal _terminal);
	
	/**
	 * Possible to change the related protocol for this card.
	 * null value is NOT permitted.
	 * 
	 * @param _protocol The protocol as String value.
	 */
	public void setProtocol(String _protocol);
	
	/**
	 * @return The current related reader for this card.
	 */
	public CardTerminal getTerminal();
	
	/**
	 * @return The current related protocol for this card.
	 */
	public String getProtocol();
	
	/**
	 * @return The card that is related to this object.
	 */
	public Card getCard();
	
	/**
	 * Send a command to the card. For example to read out values or to write
	 * data to the card.
	 * 
	 * @param _command The command encoded in bytes.
	 * @return A {@link ResponseAPDU} that encapsulates the response.
	 * @throws CardException
	 */
	public ResponseAPDU sendAPDUCommandToCard(byte[] _command) throws CardException;
	
	/**
	 * Sends a APDU command to the card. Used to interact with a connected card.
	 * 
	 * @param _command The command encapsulated in {@link CommandAPDU}
	 * @return A {@link ResponseAPDU} that encapsulates the response.
	 * @throws CardException
	 */
	public ResponseAPDU sendAPDUCommandToCard(CommandAPDU _command) throws CardException;
	
	/**
	 * Disconnect from the card.
	 * 
	 * @param _reset Reset the connection or not.
	 * @throws CardException
	 */
	public void  disconnect(boolean _reset) throws CardException;
	
	/**
	 * The card type from the ATR bytes (entry 13 and 14).
	 * The 14. byte is actually the entry in the type 
	 * enumeration.
	 * 
	 * @return The name of the card as enumeration entry.
	 */
	public CardType getCardType();
	
	/**
	 * @return The UID of the RFID card.
	 * @throws Exception
	 */
	public String getUID() throws Exception;

	/**
	 * Returns the hash value of the UID.
	 *  
	 * @param hashType What hash value type should be computed.
	 * @return The hash value of the UID.
	 * @throws Exception 
	 */
	public String getUIDHash(HASH_TYPE hashType) throws Exception;
}
