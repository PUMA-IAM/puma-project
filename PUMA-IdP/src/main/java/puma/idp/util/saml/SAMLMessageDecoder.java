package puma.idp.util.saml;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Base64;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.binding.BasicSAMLMessageContext;
import org.opensaml.saml2.binding.decoding.HTTPRedirectDeflateDecoder;
import org.opensaml.ws.message.decoder.MessageDecodingException;
import org.opensaml.ws.transport.http.HttpServletRequestAdapter;
import org.opensaml.xml.security.SecurityException;

import puma.util.saml.SAMLHelper;

public class SAMLMessageDecoder {
	private static final Logger logger = Logger.getLogger(SAMLMessageDecoder.class.getName());
	public static BasicSAMLMessageContext<SAMLObject, SAMLObject, SAMLObject> decode(HttpServletRequest request) {
		try {
			SAMLHelper.initialize();
			BasicSAMLMessageContext<SAMLObject, SAMLObject, SAMLObject> messageContext = new BasicSAMLMessageContext<SAMLObject, SAMLObject, SAMLObject>();
	        logger.log(Level.INFO, "Parsing message");
	        messageContext.setInboundMessageTransport(new HttpServletRequestAdapter(request));
	        HTTPRedirectDeflateDecoder decoder = new HTTPRedirectDeflateDecoder();
	        // DEBUG
	        String report = "";
	        Map<String, String[]> params = request.getParameterMap();
	        Iterator<String> i = params.keySet().iterator();	        
	        while ( i.hasNext() )
	          {
	            String key = (String) i.next();
	            String value = ((String[]) params.get( key ))[ 0 ];
	            report = report + key + "=" + value + ", ";
	          }	        		
	        logger.log(Level.INFO, "Parameters: " + report.substring(0, report.length() - 2));
	        String rsp = (String) request.getParameter("SAMLResponse");
	        String rq = (String) request.getParameter("SAMLRequest");
	        logger.log(Level.INFO, "Response is " + (rsp==null?"null":rsp) + ", request is " + (rq==null?"null":rq));
	        if (rsp != null) {
		        Base64 base64Decoder = new Base64();
		        byte[] xmlBytes;
				try {
					xmlBytes = rsp.getBytes("UTF-8");
			        byte[] base64DecodedByteArray = base64Decoder.decode(xmlBytes);
			        // Inflate (uncompress) the AuthnRequest data
			        // First attempt to unzip the byte array according to DEFLATE (rfc 1951)
		
			        Inflater inflater = new Inflater(true);
			        inflater.setInput(base64DecodedByteArray);
			        // since we are decompressing, it's impossible to know how much space we
			        // might need; hopefully this number is suitably big
			        byte[] xmlMessageBytes = new byte[5000];
			        int resultLength = 0;
					try {
						resultLength = inflater.inflate(xmlMessageBytes);
					} catch (DataFormatException e) {
						e.printStackTrace();
					}
		
			        if (!inflater.finished()) {
			            throw new RuntimeException("didn't allocate enough space to hold "
			                    + "decompressed data");
			        }
		
			        inflater.end();
		
			        try {
						String decodedResponse = new String(xmlMessageBytes, 0, resultLength,
						        "UTF-8");
						byte[] encoded = (byte[]) base64Decoder.encode(decodedResponse.getBytes("UTF-8"));
						String encodedMsg = new String(encoded);
						logger.log(Level.INFO, "Decoded message: " + decodedResponse + " -- encoded message: " + encodedMsg);
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
	        }
	         
	        // /DE BUG
	        logger.log(Level.INFO, "Decoding message");
	        decoder.decode(messageContext);
	        logger.log(Level.INFO, "Decoded message");
	        logger.log(Level.INFO, "Type of inbound message is " + messageContext.getInboundSAMLMessage().getClass().getCanonicalName() + ".");
	        return messageContext;
		} catch (MessageDecodingException ex) {
			logger.log(Level.SEVERE, null, ex);
		} catch (SecurityException ex) {
			logger.log(Level.SEVERE, null, ex);
		}
		return null;
	}
}
