package test.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import process.protocol.ProtocolFactory;
import data.Protocol;
import data.enums.ActionCodes;

import org.junit.Before;
import org.junit.Test;

public class TestProtocolCreation {
	
	Protocol protocol;
	
	@Test
	public void createConnectionProtocol() {
		Protocol protocol = ProtocolFactory.createConnectionProtocol("Billy", "billytheboss43", false);
		assertEquals("<0001><Billy><billytheboss43>", protocol.toString());
	}
}
