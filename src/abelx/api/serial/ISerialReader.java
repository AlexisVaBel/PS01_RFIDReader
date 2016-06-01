package abelx.api.serial;

import java.util.List;

public interface ISerialReader {
	void	setTimeOut(int iTimeOut);
	void	setReadCnt(int iCnt);
	
	boolean readData(byte[] inData);	
	List<Integer> getReadedBuffer();
	String	getReaded();
	boolean allRead();
}
