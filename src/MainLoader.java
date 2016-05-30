
import abelx.api.serial.*;
import abelx.core.serial.*;

public class MainLoader {
	public static void main(String[] args){
		String strPort="/dev/ttyUSB1";
		
		ISerialPort 	port=new SPortSSC(strPort);
		
		
		Thread			thr=new PS01Serial();
		ISerialReader 	rdr	=(ISerialReader) thr;		
		rdr.setReadCnt(10);		
		port.setDirection(true, false);
		port.addListener(rdr);
		
		try {
			port.open();
			thr.start();
		} catch (Exception e1) {
			e1.printStackTrace();
		};		

	}
}
