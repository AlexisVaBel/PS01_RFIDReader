package abelx.core.serial;




import java.util.ArrayList;
import java.util.List;

import abelx.api.serial.ISerialReader;
import abelx.api.serial.ISerialWriter;

public class SerialSimplePrcs extends Thread implements ISerialReader, ISerialWriter{
	
	List<Integer> m_lst=null;
	List<Integer> m_lstPrev=null;
	byte[] m_data;
	private int m_iCurSnd;
	private int m_iCurRcd;
	private int m_iTimeOut;	
	private boolean m_bRcvd;
	boolean	m_bWork;
	
	
	public SerialSimplePrcs(){
		m_iCurRcd=0;
		m_iCurSnd=0;		
		m_lst	 =new ArrayList<>();
		m_lstPrev=new ArrayList<>();
		m_data=new byte[]{};
		m_bWork=true;
		m_bRcvd=false;
	}
	
	@Override
	public void setReadCnt(int iCnt) {
		m_iCurRcd=iCnt;		
	}
	
	@Override
	public boolean readData(byte[] inData) {		
		Byte data=inData[0];		
		if((m_lst.size()<m_iCurRcd)){
			m_lst.add(new Integer(data.intValue()&0xFF));
		}else{
			getReadedBuffer();
			clearBuffer();			
			m_lst.add(new Integer(data.intValue()&0xFF));
		};		
		m_bRcvd=true;		
		return true;
	}

	@Override
	public boolean allRead() {		
		return (m_lst.size()>=m_iCurRcd);
	}
	
	@Override
	public List<Integer> getReadedBuffer() {
		System.out.println(m_lst.toString());
		return m_lst;
	}
	
	
	public void clearBuffer() {
		m_lstPrev=new ArrayList<>(m_lst);
		m_lst.clear();
		m_bRcvd=false;
	}
	
	
	@Override
	public void setDataToWrite(byte[] data) {
		m_data=data;
		m_iCurSnd=0;		
	}

	@Override
	public byte writeNext() {
		System.out.println("writing "+m_data[m_iCurSnd]);
		if(m_iCurSnd<m_data.length)return m_data[m_iCurSnd++];		
		return -1;
	}

	@Override
	public boolean allWritten() {		
		return (m_iCurSnd>=m_data.length);
	}	

	@Override
	public void run(){
		System.out.println("SerialProcessor run");
		while(m_bWork){			
			try {
				sleep(m_iTimeOut);
				if(m_bRcvd){					
					getReadedBuffer();
					clearBuffer();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}		
			
		}
		System.out.println("SerialProcessor out");
	}
	
	public void stopWork(){
		m_bWork=false;
	}

	@Override
	public void setTimeOut(int iTimeOut) {
		m_iTimeOut=iTimeOut;		
	}

	@Override
	public String getReaded() {
		// TODO Auto-generated method stub
		return null;
	}
	

	
}
