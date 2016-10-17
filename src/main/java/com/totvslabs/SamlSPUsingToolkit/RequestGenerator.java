package com.totvslabs.SamlSPUsingToolkit;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.totvslabs.idm.protocol.saml2toolkit.authnRequest.AuthnRequestGenerator;
import com.totvslabs.idm.protocol.saml2toolkit.common.OpenSAMLInitializer;
import com.totvslabs.idm.protocol.saml2toolkit.common.PropertyObject;
import com.totvslabs.idm.protocol.saml2toolkit.common.PropertyReader;

/**
 * This class will generate and send the authentication request to the IDP
 * (SP-initiated)
 *
 */
public class RequestGenerator extends HttpServlet {

	private final static Logger logger = LoggerFactory.getLogger(RequestGenerator.class);
	
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
		generateRequest(httpRequest, httpResponse);
	}

	@Override
	public void doPost(final HttpServletRequest httpRequest,final HttpServletResponse httpResponse) throws ServletException, IOException	      
	{	
		logger.info("received a POST");
		generateRequest(httpRequest, httpResponse);
	}	

	public void generateRequest(HttpServletRequest httpRequest, HttpServletResponse httpResponse)
	{
		logger.info("Request to generate authentication request");
		
		try
		{
			ServletContext sc = httpRequest.getSession().getServletContext();
			PropertyObject propObj = (PropertyObject) sc.getAttribute("propertyObject");
			if(propObj == null)
			  throw new Exception("Configuration not initialized");
		
			AuthnRequestGenerator authnRequestGenerator = new AuthnRequestGenerator();
			
			authnRequestGenerator.sendAuthnRequest(httpRequest, httpResponse, propObj);
		} catch (Exception e) {
			logger.error("Exception: " + e.getMessage());
		}	
	}	
}
