package test.unit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import data.protocol.Protocol;
import data.enums.ActionCodes;

import org.junit.Before;
import org.junit.Test;

public class TestProtocol {
	
	Protocol protocol;
	
	@Test
	public void createProtocol() {
		ActionCodes Connect = ActionCodes.CONNECTION;
		protocol = new Protocol(Connect);
		System.out.println(protocol.toString());
		assertEquals(ActionCodes.CONNECTION.getCode(), protocol.toString());
	}
}
