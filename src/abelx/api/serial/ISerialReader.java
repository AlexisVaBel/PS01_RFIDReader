package abelx.api.serial;

import java.util.List;

public interface ISerialReader {
	void	setReadCnt(int iCnt);
	boolean readData(byte[] inData);
	void	setTimeOut(int iTimeOut);
	List<Integer> getReadedBuffer();
	void	clearBuffer();
	boolean allRead();
}
