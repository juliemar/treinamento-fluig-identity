package com.totvslabs.SamlSPUsingToolkit;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totvslabs.idm.protocol.saml2toolkit.common.OpenSAMLInitializer;
import com.totvslabs.idm.protocol.saml2toolkit.common.PropertyObject;
import com.totvslabs.idm.protocol.saml2toolkit.common.PropertyReader;
import com.totvslabs.idm.protocol.saml2toolkit.common.User;
import com.totvslabs.idm.protocol.saml2toolkit.responseVerify.ResponseProcessor;

/**
 * This class will receive SAML response from the IDP
 *
 */
public class AssertionConsumerService extends HttpServlet {

	private final static Logger logger = LoggerFactory.getLogger(AssertionConsumerService.class);
	
	private static final long serialVersionUID = 1L;

	@Override
	public void init()
	{
		try 
		{
			//initialize the library
			OpenSAMLInitializer initOpenSaml = new OpenSAMLInitializer();
			initOpenSaml.initializeLibrary();						
		} 
		catch (Exception e) 
		{
			logger.error("Exception occurred while initializing the OpenSaml library: " + e.getMessage());
		}  
		
		try
		{			
			ServletContext sc = getServletConfig().getServletContext();
			
			if(sc.getAttribute("propertyObject") == null)
			{
				logger.info("Load the configurations for saml");
				
				//**********set relURI to saml.properties, if you are using properties file********/
				//**********OR set relURI to samlConfig.xml, if you are using xml file******/
				String relURI = "saml.properties";
				//String relURI = "samlConfig.xml";
				
				PropertyReader propReader = new PropertyReader(); 
				PropertyObject propObj = propReader.loadPropertyFile(relURI);				
				
				sc.setAttribute("propertyObject", propObj);
			}
			
			logger.info("PropertyObject is set...");
		}
		catch(Exception e)
		{
			logger.error("Exception occurred while loading the configuration file: " + e.getMessage());
		}
	}
	
	@Override
	public void doGet(final HttpServletRequest httpRequest,final HttpServletResponse httpResponse) throws ServletException,IOException 
	{
		logger.info("received a GET");
		receiveResponse(httpRequest, httpResponse);
	}

	@Override
	public void doPost(final HttpServletRequest request,final HttpServletResponse response) throws ServletException, IOException	      
	{	
		logger.info("received a POST");
		receiveResponse(request, response);
	}	
	
	private void receiveResponse(HttpServletRequest request, HttpServletResponse response)
	{
		logger.info("receieved a SAML Response");
		
		try
		{
			ServletContext sc = request.getSession().getServletContext();
			PropertyObject propObj = (PropertyObject) sc.getAttribute("propertyObject");
			if(propObj == null)
			  throw new Exception("Configuration not initialized");
			
			ResponseProcessor responseProcessor = new ResponseProcessor();
		
			User user = responseProcessor.processResponse(request, response, propObj);
			//get the username
			System.out.println("Subject: " + user.getUsername());
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().write("Subject: " + user.getUsername());
			
			//TODO: Você poderia injetar o usuário logado em um outro sistema mqualquer que não suporte SAML nativamente.
			
			
			//get the attribute names and values
			Map<String, List<String>> attributes =  user.getAttributes();		
			
			
			for (Entry<String, List<String>> entry : attributes.entrySet()) 
			{
				System.out.println("Attribute name: " + entry.getKey());
				List<String> attrValueList = entry.getValue();
				for (String val : attrValueList){
					System.out.println("Attribute value : " + val);
					response.getWriter().write("Attribute value : " + val);
				}
			}
		}
		catch(Exception e)
		{				
			logger.error("Exception while processing the response: " + e.getMessage());
		}
	}
}
