package test.abelx.core.serial;

import abelx.core.serial.SPortSSC;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SPortSSCTest {
	private SPortSSC m_port=null;
	
	@Test
	public void openPortSSC(){		
		try {
			m_port=new SPortSSC("/dev/ttyUSB0");
			m_port.open();
			assertEquals(m_port.isOpen(),true);
			m_port.close();
			assertEquals(m_port.isOpen(),false);
		} catch (Exception e) {
			System.out.println("not found port");
			assertEquals(false,true);			
			e.printStackTrace();
		}
	}

}
