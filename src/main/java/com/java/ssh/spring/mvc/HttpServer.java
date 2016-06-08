package com.java.ssh.spring.mvc;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.webapp.WebAppContext;

public class HttpServer {
	
	
	public static class DefaultHttpServlet extends HttpServlet{
		private static final long serialVersionUID = 1L;

		@Override
		protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			//super.doGet(req, resp);
			String query = req.getQueryString();
			String cIp = CommonUtils.getClientIP(req);
			System.out.println(String.format("ip[%s]query [%s]", cIp, query));
		}

		@Override
		protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
			super.doPost(req, resp);
		}
		
	}
	/**
	 * 手动配置servlet路径
	 */
	public static void run(){
		Server server = new Server();  
        try {  
            Connector conn = new SocketConnector();  
            conn.setPort(9999);  
            server.setConnectors(new Connector[]{conn});  
              
            ContextHandlerCollection handler = new ContextHandlerCollection();  
            ServletContextHandler servlethandler = new ServletContextHandler();  
            servlethandler.addServlet(DefaultHttpServlet.class, "*.action");  
            handler.addHandler(servlethandler);  
              
            WebAppContext webapp = new WebAppContext();  
            webapp.setContextPath("/");  
            //设置资源的路径(静态资源和jsp)
            webapp.setResourceBase("WebRoot");
            URL defDescriptor = ClassLoader.getSystemClassLoader().getResource("com/java/ssh/spring/mvc/webdefault.xml");
            webapp.setDefaultsDescriptor(defDescriptor.toString());  
            URL descriptor = ClassLoader.getSystemClassLoader().getResource("com/java/ssh/spring/mvc/web.xml");
			webapp.setDescriptor(descriptor.toString());
            handler.addHandler(webapp);  
              
            server.setHandler(handler);  

              
            server.start();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		run();

	}

}
