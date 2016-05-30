package abelx.api.serial;

import java.util.List;

public interface ISerialPort {
	
	public boolean	open() throws Exception;
	public void 	close()	throws Exception;	
	
	public List<String> getAvailable();
	public void setName(String strName);
	public void setDirection(boolean rdonly,boolean wronly);
	public void setParams(int iBaud, int iDataB, int iStop, int iParity);
	public void setParams(String strName,int iBaud, int iDataB, int iStop, int iParity);
	public void	setTimeOut(int iTimeOut);
	public int	getTimeOut();
	public boolean addListener(ISerialReader rdr);
	public boolean remListner(ISerialReader rdr);	
	public boolean setWritter(ISerialWriter wrt);
	
	public boolean isOpen();
}
