package com.abelx.http;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

public class ServiceWebCreator implements WebSocketCreator{
	private ServiceWebSocket m_webSocket;	
	
	public ServiceWebCreator(ServiceWebSocket socket){
		this.m_webSocket =socket;				
	}
	
	@Override
	public Object createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
		System.out.println(req.getSubProtocols());
		for(String strSub: req.getSubProtocols()){
			if("binary".equals(strSub)){
				resp.setAcceptedSubProtocol(strSub);				
				return m_webSocket;
			}
			if("text".equals(strSub)){
				resp.setAcceptedSubProtocol(strSub);				
				return m_webSocket;
			}
		}
		return null;
	}

}
