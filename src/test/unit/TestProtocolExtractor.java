package test.unit;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import data.Protocol;
import data.enums.ActionCodes;
import exceptions.InvalidProtocolException;
import process.protocol.ProtocolExtractor;

public class TestProtocolExtractor {
	
	@Test
	public void extractSimpleProtocol() throws InvalidProtocolException {
		String protocolString = "<0001><Billy><Bernard>";
		ProtocolExtractor protocolExtractor = new ProtocolExtractor(protocolString);
		Protocol protocol = protocolExtractor.getProtocol();
		
		assertEquals(ActionCodes.CONNECTION_NORMAL, protocol.getActionCode());
		List<String> optionsList = protocol.getOptionsList();
		assertEquals("Billy", optionsList.get(0));
		assertEquals("Bernard", optionsList.get(1));
	}
	
	@Test(expected = InvalidProtocolException.class)
	public void extractWrongSimpleProtocol() throws InvalidProtocolException{
		String protocolString = "<0001><Billy<>Bernard>";
		
		ProtocolExtractor protocolExtractor = new ProtocolExtractor(protocolString);
	}
	
	@Test(expected = InvalidProtocolException.class)
	public void extractWrongCodeSimpleProtocol() throws InvalidProtocolException{
		String protocolString = "<8989><Billy>Bernard>";
		
		ProtocolExtractor protocolExtractor = new ProtocolExtractor(protocolString);
	}
}
