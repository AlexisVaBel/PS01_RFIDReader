package abelx.core.serial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import abelx.api.serial.ISerialPort;
import abelx.api.serial.ISerialReader;
import abelx.api.serial.ISerialWriter;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

public class SPortSSC implements ISerialPort{
	private static int SERIAL_RD_BY_BYTE=1;
	private static int SERIAL_TIMEOUT=50;
	
	private List<ISerialReader> m_lstRdrs;
	private ISerialWriter		m_wrt=null;
	
	private String m_strPort;
	private boolean		m_RD=true;
	private boolean		m_WR=false;
	
	private int m_iBaud		=SerialPort.BAUDRATE_9600;
	private int m_iDataB	=SerialPort.DATABITS_8;
	private int m_iStopB	=SerialPort.STOPBITS_1;
	private int m_iParity	=SerialPort.PARITY_NONE;
	private int m_iTimeOut	=SERIAL_TIMEOUT;
	
	
	private SerialPort 			m_port=null;
	private SPortSSCListener 	m_listener=null;
	
	private class SPortSSCListener implements SerialPortEventListener{
		@Override
		public void serialEvent(SerialPortEvent serialPortEvent) {
			if(!m_lstRdrs.isEmpty()){
				if((serialPortEvent.isRXCHAR())&&(serialPortEvent.getEventValue()>=SERIAL_RD_BY_BYTE)){
					try {					
						byte[] rdBuf=m_port.readBytes(1);
//						System.out.println(rdBuf[0]&0xFF);
						m_lstRdrs.stream().filter(rdr -> rdr!=null).forEach(rdr -> rdr.readData(rdBuf));
					} catch (SerialPortException e) {				
						e.printStackTrace();
					} 
				};
			}
			if(m_wrt!=null)
				if((serialPortEvent.isTXEMPTY()&&(!m_wrt.allWritten()))){
					System.out.println("sending next byte");
					try {
						m_port.writeByte(m_wrt.writeNext());
					} catch (SerialPortException e) {				
						e.printStackTrace();
					}
				}
		}
		
	}	
	
	public SPortSSC(String strPort){
		m_lstRdrs=new ArrayList<>();
		m_strPort=strPort;	
		m_port=new SerialPort(strPort);		
	}
	
	@Override
	public boolean open() throws Exception {
		System.out.println("port is opened "+m_strPort);
		m_port.openPort();
		if(m_port.isOpened()){
			m_port.setParams(m_iBaud, m_iDataB, m_iStopB, m_iParity);
			m_listener=new SPortSSCListener();
			if(m_RD)m_port.setEventsMask(SerialPort.MASK_RXCHAR);
			if(m_WR)m_port.setEventsMask(SerialPort.MASK_TXEMPTY);
			if(m_RD&&m_WR)m_port.setEventsMask(SerialPort.MASK_RXCHAR|SerialPort.MASK_TXEMPTY);
			m_port.addEventListener(m_listener);
			if(m_WR)m_port.writeByte(m_wrt.writeNext());
		}
		return false;		
	}

	@Override
	public void close() throws Exception {		
		m_port.closePort();		
	}

	@Override
	public List<String> getAvailable() {
		List<String> lst=Arrays.asList("hui ","v mandu"," ty "," podkljuchay");
		return lst;
	}

	@Override
	public void setName(String strName) {
		if(m_strPort.compareToIgnoreCase(strName)!=0){
			m_strPort=strName;			
			try {
				this.close();
				m_port=new SerialPort(m_strPort);
			} catch (Exception e) {			
				e.printStackTrace();
			}
		}		
	}

	@Override
	public void setTimeOut(int iTimeOut) {
		m_iTimeOut=iTimeOut;
		m_lstRdrs.stream().filter(rdr -> rdr!=null).forEach(rdr -> rdr.setTimeOut(m_iTimeOut));
	}

	@Override
	public int getTimeOut() {
		return m_iTimeOut;
	}
	
	@Override
	public void setParams(int iBaud, int iDataB, int iStop, int iParity) {
		m_iBaud =iBaud;
		m_iDataB=iDataB;
		m_iStopB=iStop;		
		m_iParity=iParity;
	}

	@Override
	public void setParams(String strName, int iBaud, int iDataB, int iStop, int iParity) {
		setName(strName); 
		setParams(iBaud, iDataB, iStop,iParity);		 		 
	}

	@Override
	public boolean addListener(ISerialReader rdr) {		
		if(m_lstRdrs.contains(rdr))return false;
		rdr.setTimeOut(m_iTimeOut);
		m_lstRdrs.add(rdr);
		return true;
	}

	@Override
	public boolean remListner(ISerialReader rdr) {
		return m_lstRdrs.remove(rdr);		
	}	

	@Override
	public boolean isOpen() {
		return m_port.isOpened();
	}

	@Override
	public boolean setWritter(ISerialWriter wrt) {
		m_wrt=wrt;
		return m_wrt!=null;
	}

	@Override
	public void setDirection(boolean rdonly, boolean wronly) {
		m_RD=rdonly;
		m_WR=wronly;		
	}	

}
