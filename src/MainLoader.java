
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import com.abelx.http.ServiceWebCreator;
import com.abelx.http.ServiceWebServlet;
import com.abelx.http.ServiceWebSocket;

import abelx.api.serial.*;
import abelx.core.serial.*;

public class MainLoader {
	
	public static class ServiceSocketServlet extends WebSocketServlet{
		private static final long serialVersionUID = 3285936204366619277L;		
		
		@Override
		public void configure(WebSocketServletFactory factory){
			factory.getPolicy().setIdleTimeout(10000);
			factory.register(ServiceWebSocket.class);
			
		}
	}	
	
	
	public static void main(String[] args){
		//work with port
		String strPort="/dev/ttyUSB0";
		
		ISerialPort 	port=new SPortSSC(strPort);		
		Thread			thr=new PS01Serial();
		ISerialReader 	rdr	=(ISerialReader) thr;		
		rdr.setReadCnt(10);		
		port.setDirection(true, false);
		port.addListener(rdr);
		//work with port		

		//WebSocket added
		Server server=new Server(8090);
		ServiceWebSocket webSocket=new ServiceWebSocket();		
		webSocket.setReader(rdr);
		ServiceWebCreator webCreator=new ServiceWebCreator(webSocket);
		ServiceWebServlet webServlet=new ServiceWebServlet(webCreator);
		
		ServletContextHandler ctx=new ServletContextHandler();
		ctx.setContextPath("/");
//		ctx.addServlet(new ServletHolder(webServlet), "/ps01");
		ctx.addServlet(ServiceSocketServlet.class, "/ps01");
		server.setHandler(ctx);		
		//WebSocket added		
		
		try {
			port.open();
			thr.start();
			server.start();
			server.join();
			thr.join();
		} catch (Exception e1) {
			e1.printStackTrace();
		};				
		
		
	}
}
