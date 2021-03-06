package abelx.core.serial;

import java.util.ArrayList;
import java.util.List;

import abelx.api.serial.ISerialReader;


public class PS01Serial extends Thread implements ISerialReader{
	
	private String m_strOut="";
	private static int MAX_LNG=9;
	private static int FST_CMD=0x23;
	List<Integer> m_lst=null;
	List<Integer> m_lstPrev=null;
	byte[] m_data;
	
	private int m_iRcd;
	private int m_iTimeOut;	
	private boolean m_bRcvd;
	private boolean m_bPcsd;
	boolean	m_bWork;
	
	public PS01Serial(){
		m_iRcd	 =MAX_LNG;			
		m_lst	 =new ArrayList<>();
		m_lstPrev=new ArrayList<>();
		m_data=new byte[]{};
		m_bWork=true;
		m_bRcvd=false;
		m_bPcsd=false;
	}
	
	@Override
	public void setReadCnt(int iCnt) {
		// here is a stub	
	}

	@Override
	public boolean readData(byte[] inData) {
		Byte data=inData[0];		
		if((m_lst.size()<=m_iRcd)){
			m_bPcsd=false;
			m_lst.add(new Integer(data.intValue()&0xFF));			
		}else{
			getReadedBuffer();
			clearBuffer();
			procsPacket();
			m_bPcsd=true;
			m_lst.add(new Integer(data.intValue()&0xFF));
		};		
		if(m_lst.get(0).intValue()!=FST_CMD){
			clearBuffer();
		}else
			m_bRcvd=true;		
		return m_bRcvd;
	}

	@Override
	public void setTimeOut(int iTimeOut) {
		m_iTimeOut=iTimeOut;		
	}

	@Override
	public List<Integer> getReadedBuffer() {		
		return m_lst;
	}

	
	public void clearBuffer() {
		m_lstPrev=new ArrayList<>(m_lst);
		m_lst.clear();
		m_bRcvd=false;		
	}

	@Override
	public boolean allRead() {
		return (m_lst.size()>=m_iRcd);
	}

	
	@Override
	public void run(){
		System.out.println("SerialProcessor run");
		while(m_bWork){			
			try {
				sleep(m_iTimeOut);
				if((m_bRcvd)&&(!m_bPcsd)){					
					getReadedBuffer();
					clearBuffer();
					procsPacket();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}		
			
		}
		System.out.println("SerialProcessor out");
	}
	
	private void procsPacket(){
		if(m_lstPrev.size()<MAX_LNG){
			System.out.println("NONE DATA");
			m_strOut="NONE DATA";
			return;
		}
		if(m_lstPrev.get(1).intValue()==0x50){
			m_strOut="Turned on power";
			System.out.println("Turned on power");
		}
		if(m_lstPrev.get(1).intValue()==0x53){
			m_strOut="Autotest made";
			System.out.println("Autotest made");
		}
		if(m_lstPrev.get(1).intValue()==0x43){
			String outStr="";
//			outStr=Integer.toHexString(m_lstPrev.get(3))+"_";			
			outStr+=String.format("%02x", (m_lstPrev.get(4)));
			outStr+=String.format("%02x", (m_lstPrev.get(5)));
			outStr+=String.format("%02x", (m_lstPrev.get(6)));			
			outStr+=String.format("%02x", (m_lstPrev.get(7)));			
			m_strOut=outStr;
			System.out.println("Card code "+Integer.parseInt(outStr, 16));			
		}
	}

	@Override
	public String getReaded() {
		return m_strOut;
	}
	
}
