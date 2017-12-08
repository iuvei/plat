package com.gameportal.portal.util;

import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

import org.w3c.dom.NodeList;

import com.enterprisedt.util.debug.Logger;
import com.gameportal.manage.member.service.impl.MgGameAPI;


public class MgSoapPacket   {
    
    Logger logger = Logger.getLogger(MgSoapPacket.class);
    
	public NodeList sendPacket(MgGameAPI mgGameAPI) {
		try {			
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = soapConnectionFactory.createConnection();

			MessageFactory messageFactory = MessageFactory.newInstance();

			SOAPMessage soapMessage = messageFactory.createMessage();
			soapMessage = makeSoupPacket(soapMessage,mgGameAPI);
			//soapMessage.getSOAPBody().getChildNodes().getLength();
			SOAPMessage soapResponse = soapConnection.call(soapMessage,mgGameAPI.getUrl());

			soapConnection.close();

			if(soapResponse==null){
			    logger.debug("Response is Null");
				throw new Exception();
			}else{
				SOAPBody responseBody = soapResponse.getSOAPBody();
								
				if (responseBody.getFault() != null) {
					System.out.println("**************"+responseBody.getFault().getFaultString()+"***********");
					logger.debug(responseBody.getFault().getFaultString());
					throw new Exception();	
				} 
				else {
					return responseBody.getElementsByTagName(mgGameAPI.getApiName()+"Result");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	private SOAPMessage makeSoupPacket(SOAPMessage soapMessage, MgGameAPI mgGameAPI){
		SOAPPart soapPart = soapMessage.getSOAPPart();		
		String serverURI = mgGameAPI.getBaseUri();
		try{
			SOAPEnvelope envelope = soapPart.getEnvelope();
	
			envelope.addNamespaceDeclaration("xsi","http://www.w3.org/2001/XMLSchema-instance");
			envelope.addNamespaceDeclaration("xsd","http://www.w3.org/2001/XMLSchema");
			
			SOAPHeader header = envelope.getHeader();
			SOAPBody soapBody = envelope.getBody();			
			
			if(!mgGameAPI.getApiName().equals("IsAuthenticate")){
				QName headerName = new QName(serverURI, mgGameAPI.getHeaderElement(), "");
				SOAPElement soapHeaderElem = header.addHeaderElement(headerName);
				SOAPElement soapHeaderChild;
							
				for(Entry<String, Object> entry:mgGameAPI.getHeader().entrySet()){
					QName name = new QName(entry.getKey());
					soapHeaderChild = soapHeaderElem.addChildElement(name);
					soapHeaderChild.addTextNode(entry.getValue().toString());					
				}
			}
			else{
				
				header.detachNode();
			}
	
			// <Soap: Body>
			logger.debug(mgGameAPI.getApiName());
			QName bodyName = new QName(serverURI, mgGameAPI.getApiName(), "");
			SOAPElement soapBodyElem = soapBody.addBodyElement(bodyName);
			
			if(mgGameAPI.getBody() !=null){
				makeSoapBody(soapBodyElem, mgGameAPI.getBody());
				logger.debug(mgGameAPI.getBody().toString());
			}
	
			MimeHeaders mheader = soapMessage.getMimeHeaders();
			mheader.addHeader("Accept-Encoding", "gzip,deflate");
			
			soapMessage.saveChanges();
			
			return soapMessage;
			

		}catch(Exception e){
			logger.debug("Making Soap Packet is failed!" + e.toString());
			return null;
		}
	}
	private void makeSoapBody(SOAPElement soapElement, Map<String, Object> entrySet){

		for(Entry<String, Object> entry: entrySet.entrySet()){	
			QName paramName = new QName(entry.getKey());
			if(entry.getKey().matches("item(.*)")){
				paramName = new QName("item");
			}
			SOAPElement soapChildElement;
			try {
				soapChildElement = soapElement.addChildElement(paramName);
				if(entry.getValue() instanceof Map<?,?>){
					makeSoapBody(soapChildElement, (Map<String, Object>) entry.getValue());
				}
				else{
					soapChildElement.addTextNode(entry.getValue().toString());									
				}
			} catch (SOAPException e) {
				e.printStackTrace();
			}
		}
	}
}
