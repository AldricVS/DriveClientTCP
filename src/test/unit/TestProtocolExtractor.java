package test.unit;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import data.enums.ActionCodes;
import data.protocol.Protocol;
import exceptions.InvalidProtocolException;
import process.protocol.ProtocolExtractor;

public class TestProtocolExtractor {
	
	@Test
	public void extractSimpleProtocol() throws InvalidProtocolException {
		String protocolString = "<0001><Billy><Bernard>";
		ProtocolExtractor protocolExtractor = new ProtocolExtractor(protocolString);
		Protocol protocol = protocolExtractor.extract();
		
		assertEquals(ActionCodes.CONNECTION_NORMAL, protocol.getActionCode());
		List<String> optionsList = protocol.getOptionsList();
		assertEquals("Billy", optionsList.get(0));
		assertEquals("Bernard", optionsList.get(1));
	}
	
	@Test(expected = InvalidProtocolException.class)
	public void extractWrongSimpleProtocol() throws InvalidProtocolException{
		String protocolString = "<0001><Billy<>Bernard>";
		
		ProtocolExtractor protocolExtractor = new ProtocolExtractor(protocolString);
		//error when parsing ('<' before closing '>' 
		Protocol protocol = protocolExtractor.extract();
	}
	
	@Test(expected = InvalidProtocolException.class)
	public void extractWrongCodeSimpleProtocol() throws InvalidProtocolException{
		String protocolString = "<8989><Billy>Bernard>";
		
		ProtocolExtractor protocolExtractor = new ProtocolExtractor(protocolString);
		//The code 8989 is not a valid code 
		Protocol protocol = protocolExtractor.extract();
	}
}
