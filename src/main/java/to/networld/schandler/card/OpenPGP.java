package to.networld.schandler.card;

import java.util.Vector;

import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;

import to.networld.schandler.common.HexHandler;

/**
 * Before you can read out the values you have to call the "selectFile" method.
 * 
 * @author Alex Oberhauser
 *
 */
public class OpenPGP extends AbstractCard {

	/*
	 * BEGIN Response Message Codes
	 */
	// 61 XX - Command correct XX data in response.
	public static final byte[] SUCCESS = new byte[] { (byte)0x90, (byte)0x00 };
	public static final byte[] CLA_NOT_SUPPORTED = new byte[] { (byte)0x6E, (byte)0x00 };
	public static final byte[] INS_NOT_SUPPORTED = new byte[] { (byte)0x6D, (byte)0x00 };
	public static final byte[] WRONG_PARAMETER = new byte[] { (byte)0x6B, (byte)0x00 };
	public static final byte[] DATA_NOT_FOUND = new byte[] { (byte)0x6A, (byte)0x88 };
	public static final byte[] INCORRECT_DATA_PARAMETER = new byte[] { (byte)0x6A, (byte)0x80 };
	public static final byte[] INCORRECT_SM_DATA = new byte[] { (byte)0x69, (byte)0x88 };
	public static final byte[] MISSING_SM_DATA = new byte[] { (byte)0x69, (byte)0x87 };
	public static final byte[] USE_CONDITION_NOT_SATISFIED = new byte[] { (byte)0x69, (byte)0x85 };
	public static final byte[] AUTH_OR_PW_BLOCKED = new byte[] { (byte)0x69, (byte)0x83 };
	public static final byte[] SECURITY_STATUS_NOT_SATISFIED = new byte[] { (byte)0x69, (byte)0x82 };
	public static final byte[] UNSUPPORTED_COMMAND_CHAINING = new byte[] { (byte)0x68, (byte)0x84 };
	public static final byte[] EXPECTED_LAST_COMMAND = new byte[] { (byte)0x68, (byte)0x83 };
	public static final byte[] UNSUPPORTED_SECURE_MESSAGING = new byte[] { (byte)0x68, (byte)0x82 };
	public static final byte[] WRONG_LENGTH = new byte[] { (byte)0x67, (byte)0x00 };
	public static final byte[] MEMORY_FAILURE = new byte[] { (byte)0x65, (byte)0x81 };
	public static final byte[] TERMINATION_STATE = new byte[] { (byte)0x62, (byte)0x85 };
	/*
	 * END Response Message Codes
	 */
	
	public static final byte[] SELECT_FILE = new byte[] { (byte)0x00, (byte)0xA4, (byte)0x04, (byte)0x00, (byte)0x06, 
		(byte)0xD2, (byte)0x76, (byte)0x00, (byte)0x01, (byte)0x24, (byte)0x01, (byte)0x00 };
	
	/**
	 * OpenPGP card (also used for the FSFE card). 
	 * 
	 * Tested with v2.0 of the FSFE card. 
	 * 
	 * @param terminal
	 * @param protocol
	 */
	public OpenPGP(CardTerminal terminal, String protocol) {
		super(terminal, protocol);
	}
	
	/**
	 * Returns a human readable return message.
	 * 
	 * @param _responseArray The error code in the form of an byte array.
	 * @return The response message in human readable form.
	 * @throws Exception 
	 */
	public static String getResponseMessage(byte[] _responseArray) throws Exception {
		if ( _responseArray.length > 2 ) return HexHandler.getHexString(_responseArray);
		if ( _responseArray[0] ==  SUCCESS[0] && _responseArray[1] == SUCCESS[1] )
			return "Success";
		else if ( _responseArray[0] ==  TERMINATION_STATE[0] && _responseArray[1] == TERMINATION_STATE[1] )
			return "Selected file in termination state";
		else if ( _responseArray[0] ==  MEMORY_FAILURE[0] && _responseArray[1] == MEMORY_FAILURE[1] )
			return "Memory failure";
		else if ( _responseArray[0] ==  WRONG_LENGTH[0] && _responseArray[1] == WRONG_LENGTH[1] )
			return "Wrong length (Lc and/or Le)";
		else if ( _responseArray[0] ==  UNSUPPORTED_SECURE_MESSAGING[0] && _responseArray[1] == UNSUPPORTED_SECURE_MESSAGING[1] )
			return "Secure messaging not supported";
		else if ( _responseArray[0] ==  EXPECTED_LAST_COMMAND[0] && _responseArray[1] == EXPECTED_LAST_COMMAND[1] )
			return "Last command of the chain expected";
		else if ( _responseArray[0] ==  UNSUPPORTED_COMMAND_CHAINING[0] && _responseArray[1] == UNSUPPORTED_COMMAND_CHAINING[1] )
			return "Command chaining not supported";
		else if ( _responseArray[0] ==  SECURITY_STATUS_NOT_SATISFIED[0] && _responseArray[1] == SECURITY_STATUS_NOT_SATISFIED[1] )
			return "Security status not satisfied / PW wrong / PW not checked (command not allowed) / Secure messaging incorrect (checksum and/or cryptogram)";	
		else if ( _responseArray[0] ==  AUTH_OR_PW_BLOCKED[0] && _responseArray[1] == AUTH_OR_PW_BLOCKED[1] )
			return "Authentication method blocked / PW blocked (error counter zero)";
		else if ( _responseArray[0] == USE_CONDITION_NOT_SATISFIED[0] && _responseArray[1] == USE_CONDITION_NOT_SATISFIED[1] )
			return "Condition of use not satisfied";
		else if ( _responseArray[0] == MISSING_SM_DATA[0] && _responseArray[1] == MISSING_SM_DATA[1] )
			return "Expected SM data objects missing (e.g. SM-key, SSC)";
		else if ( _responseArray[0] == INCORRECT_SM_DATA[0] && _responseArray[1] == INCORRECT_SM_DATA[1] )
			return "SM data objects incorrect (e.g. wrong TLV-structure in command data)";
		else if ( _responseArray[0] == INCORRECT_DATA_PARAMETER[0] && _responseArray[1] == INCORRECT_DATA_PARAMETER[1] )
			return "Incorrect parameters in the data field";
		else if ( _responseArray[0] == DATA_NOT_FOUND[0] && _responseArray[1] == DATA_NOT_FOUND[1] )
			return "Referenced data not found";
		else if ( _responseArray[0] == WRONG_PARAMETER[0] && _responseArray[1] == WRONG_PARAMETER[1] )
			return "Wrong parameters P1-P2";
		else if ( _responseArray[0] == INS_NOT_SUPPORTED[0] && _responseArray[1] == INS_NOT_SUPPORTED[1] )
			return "Instruction (INS) not supported";
		else if ( _responseArray[0] == CLA_NOT_SUPPORTED[0] && _responseArray[1] == CLA_NOT_SUPPORTED[1] )
			return "Class (CLA) not supported";
		
		return "Unknown Response Message";	
	}
	
	public synchronized ResponseAPDU selectFile() throws CardException {
		return this.sendAPDUCommandToCard(OpenPGP.SELECT_FILE);
	}
	
	public synchronized byte[] getData(byte _p1, byte _p2) throws Exception {
		byte[] commandByteArray = new byte[] { (byte)0x00, (byte)0xCA, _p1, _p2, (byte)0xFF };
		Vector<Integer> commandVector = HexHandler.getHexToInt(commandByteArray);
		CommandAPDU command = new CommandAPDU(commandVector.get(0),
				commandVector.get(1),
				commandVector.get(2),
				commandVector.get(3),
				commandVector.get(4));
		ResponseAPDU dataAPDU = this.sendAPDUCommandToCard(command);
		System.out.println("[!!] DEBUG: " + getResponseMessage(dataAPDU.getBytes()));
		return dataAPDU.getData();
	}
	
	/**
	 * @return The Application Identifier.
	 * @throws Exception 
	 */
	public synchronized String getAID() throws Exception {
		return HexHandler.getHexString(this.getData((byte)0x00, (byte)0x4F));
	}
	
	/**
	 * @return The login name of the card owner.
	 * @throws Exception 
	 */
	public synchronized String getLoginData() throws Exception {
		return HexHandler.getHexToAscii(this.getData((byte)0x00, (byte)0x5E));
	}
	
	/**
	 * @return The name of the card owner.
	 * @throws Exception 
	 */
	public synchronized String getName() throws Exception {
		return HexHandler.getHexToAscii(this.getData((byte)0x00, (byte)0x5B));
	}

	/**
	 * @return The URL of the public key
	 * @throws Exception 
	 */
	public synchronized String getURL() throws Exception {
		return HexHandler.getHexToAscii(this.getData((byte)0x5F, (byte)0x50));
	}
	
	/**
	 * TODO
	 * 
	 * @return The gender of the card owner.
	 * @throws Exception 
	 */
	public synchronized String getGender() throws Exception {
		return HexHandler.getHexString(this.getData((byte)0x5F, (byte)0x35));
	}
	
	/**
	 * TODO
	 * 
	 * @return The language of the card owner.
	 * @throws Exception 
	 */
	public synchronized String getLanguage() throws Exception {
		return HexHandler.getHexToAscii(this.getData((byte)0x5F, (byte)0x2D));
	}
	
	/**
	 * TODO
	 * 
	 * @return The user data concatenated to one string.
	 * @throws Exception 
	 */
	public synchronized String getUserData() throws Exception {
		return HexHandler.getHexToAscii(this.getData((byte)0x00, (byte)0x65));
	}
}
