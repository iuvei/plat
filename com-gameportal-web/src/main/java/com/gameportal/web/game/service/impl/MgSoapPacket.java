package com.gameportal.web.game.service.impl;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
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
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.enterprisedt.util.debug.Logger;
import com.gameportal.web.util.DateUtil;

import net.sf.json.JSONObject;


public class MgSoapPacket   {
    
    static Logger logger = Logger.getLogger(MgSoapPacket.class);
    static{
    	FileOutputStream fos;
		try {
			fos = new FileOutputStream("/home/www/tomcat-web/logs/mg.log",true);
			//fos = new FileOutputStream("d:\\mg.log",true);
			PrintStream ps = new PrintStream(fos);
			System.setOut(ps);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
    }
    
	public NodeList sendPacket(MgGameAPI mgGameAPI) {
		SOAPConnection soapConnection = null;
		try {			
			SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
			soapConnection = soapConnectionFactory.createConnection();
			
			MessageFactory messageFactory = MessageFactory.newInstance();
			SOAPMessage soapMessage = messageFactory.createMessage();
			soapMessage = makeSoupPacket(soapMessage,mgGameAPI);
			
			SOAPMessage soapResponse = soapConnection.call(soapMessage,mgGameAPI.getUrl());
			// 输出返回报文
			System.out.print("\n"+DateUtil.getStrByDate(new Date(), DateUtil.TIME_FORMAT)+" "+mgGameAPI.getAccount() +"-->"+mgGameAPI.getApiName()+" Response SOAP Message\n");
			soapResponse.writeTo(System.out);
			
			if(soapResponse==null){
				System.out.println("Response is Null");
				throw new Exception();
			}else{
				SOAPBody responseBody = soapResponse.getSOAPBody();
				if (responseBody.getFault() != null) {
					System.out.println(mgGameAPI.getApiName()+"Result>>>>接口返回错误："+responseBody.getFault().getFaultString());
					throw new Exception();	
				} 
				else {
					return responseBody.getElementsByTagName(mgGameAPI.getApiName()+"Result");
				}
			}
		} catch (Exception e) {
			System.out.println(mgGameAPI.getApiName()+"Result返回错误："+e.getMessage());
			return null;
		}finally{
			try {
				soapConnection.close();
			} catch (SOAPException e) {
				e.printStackTrace();
			}
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
			}else{
				header.detachNode();
			}
	
			QName bodyName = new QName(serverURI, mgGameAPI.getApiName(), "");
			SOAPElement soapBodyElem = soapBody.addBodyElement(bodyName);
			
			if(mgGameAPI.getBody() !=null){
				makeSoapBody(soapBodyElem, mgGameAPI.getBody());
			}
	
			MimeHeaders mheader = soapMessage.getMimeHeaders();
			mheader.addHeader("Accept-Encoding", "gzip,deflate");
			
			soapMessage.saveChanges();
			
			// 输出请求报文
			System.out.print("\n"+DateUtil.getStrByDate(new Date(), DateUtil.TIME_FORMAT)+" "+mgGameAPI.getAccount() +"-->"+mgGameAPI.getApiName()+" Request SOAP Message\n");
			soap2String(soapMessage.getSOAPPart().getContent());
			
			return soapMessage;
		}catch(Exception e){
			System.out.println("Making Soap Packet is failed!" + e.getMessage());
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
				System.out.println("makeSoapBody 异常："+e.getMessage());
			}
		}
	}
	
	private static void PrintBody(Iterator<SOAPElement> iterator,JSONObject json) {
		while (iterator.hasNext()) {
			SOAPElement element = (SOAPElement) iterator.next();
			json.put(element.getNodeName(), element.getValue());
			if (null == element.getValue() && element.getChildElements().hasNext()) {
				PrintBody(element.getChildElements(),json);
			}
		}
	}
	 
	public void soap2String(Source source) throws Exception {
		if (source != null) {
			Node root = null;
			if (source instanceof DOMSource) {
				root = ((DOMSource) source).getNode();
			} else if (source instanceof SAXSource) {
				InputSource inSource = ((SAXSource) source).getInputSource();
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				dbf.setNamespaceAware(true);
				Document doc = dbf.newDocumentBuilder().parse(inSource);
				root = (Node) doc.getDocumentElement();
			}
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "no");
			transformer.transform(new DOMSource(root), new StreamResult(System.out));
		}
	}
}
