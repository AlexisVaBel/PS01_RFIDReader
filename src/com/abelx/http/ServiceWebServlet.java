package com.abelx.http;


import javax.servlet.annotation.WebServlet;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@WebServlet(name="web servlet",urlPatterns={"/ps01"})
public class ServiceWebServlet extends WebSocketServlet{
	private ServiceWebCreator m_creator=null;

	
	public ServiceWebServlet(ServiceWebCreator creator){
		m_creator	=creator;
	}
	@Override
	public void configure(WebSocketServletFactory arg0) {
		System.out.println("setting creator");
		arg0.getPolicy().setIdleTimeout(10000);
		arg0.setCreator(m_creator);
	}

}
