package com.abelx.http;

import abelx.api.serial.*;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;


@WebSocket
public class ServiceWebSocket {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2933973178120535900L;


	private Session session;
	
	
	private ISerialReader				m_iReader=null;
	private ScheduledFuture<?>  		m_curschedul=null;
	private ScheduledExecutorService 	executor=Executors.newScheduledThreadPool(1);

	public ServiceWebSocket(){
		System.out.println("created websocket");
	}
	
	public void setReader(ISerialReader rdr){
		m_iReader=rdr;	
	}
	
	@OnWebSocketConnect
	public void handleConnect(Session session){
		this.session=session;
		System.out.println("New session "+session.getProtocolVersion()+" "+session.getRemoteAddress());
	}
	
	@OnWebSocketClose
	public void handleClose(int statusCode, String reason){
		System.out.println("Connection closed due to "+reason+" with status "+statusCode);
	}
	
	@OnWebSocketMessage
	public void handleMessage(String message){
		switch(message){
		case "start":
			send("service started");
			if(m_iReader==null){send("no reader setted"); return;}
			m_curschedul=executor.scheduleAtFixedRate(() -> send(m_iReader.getReaded()), 0, 5, TimeUnit.SECONDS);
		break;
		case "stop":
			send("service stoped");
			if(m_curschedul!=null)m_curschedul.cancel(false);
		break;		
		
		}
	}
	
	 @OnWebSocketError
	 public void handleError(Throwable error){
		 error.printStackTrace();
	 }
	
	private void send(String message){
		try{
			if(session.isOpen()){
				session.getRemote().sendString(message);
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	
	}
	
	private void stop(){
		try {
			session.disconnect();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}
