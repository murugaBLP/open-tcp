package com.iot.service.mail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.iot.service.bean.MailBean;
import com.iot.service.utils.Constants;
import com.iot.service.utils.ParserConfigService;
import com.iot.service.utils.Utils;

/**
 * @author Muruganandam
 *
 */

public class MailService implements Runnable, Constants{

	private static final Logger LOGGER = Logger.getLogger(MailService.class.getName());
	private Properties properties = new Utils().readConfigFile(configFileName);
	public static Map<String, List<MailBean>> statusMap = new HashMap<>();
	
	ParserConfigService configService = null;
	
	public MailService(ParserConfigService configService) {
		this.configService = configService;
	}
	
	public void run() {
		try {
			while(true) {
				try {
					checkStatus();
					Thread.sleep(6000);
				} catch (Exception e) {
					LOGGER.severe("Exception in while loop.."+e);
				}
			}
		} catch (Exception e) {
			LOGGER.severe("Exception in MailService thread.."+e);
		}
	}
	
	private void checkStatus() {
		try {
			for(Map.Entry<String, List<MailBean>> map : statusMap.entrySet()) {
				List<MailBean> mailBeans = map.getValue();
				StringBuilder listOfValues = new StringBuilder();
				listOfValues.setLength(0);
				mailBeans.forEach(it->{
					if(it.getStatus() == 1) {
						Long occTime = it.getOccurredTime();
						Long currentTime = System.currentTimeMillis();
						Long timeDiff = (((currentTime-occTime)/1000)/60);
						if(timeDiff>=it.getMailInterval()) {
							Long sentTime = it.getMailSentTime();
							if(sentTime == null) {
								//send mail
								it.setMailSentTime(currentTime);
								listOfValues.append(it.getTagName()).append(", status : ").append(it.getStatus()).append("\n");
							}else {
								Long sentTimeDiff =  (((currentTime-sentTime)/1000)/60);
								if(sentTimeDiff>=it.getMailInterval()) {
									//send mail
									it.setMailSentTime(currentTime);
									listOfValues.append(it.getTagName()).append(", status : ").append(it.getStatus()).append("\n");
								}
							}
						}
					}
				});
				
				//send one mail for all the tags
				if(listOfValues.length() != 0) {
					String imeiNo = mailBeans.get(0).getImeiNo();
					Map<String, String> deviceInfo = configService.getDeviceInfo().get(imeiNo);
					String emailId = deviceInfo.get("email");
					String emailText = deviceInfo.get("emailText");
					if(emailText == null || emailText.isEmpty())
						emailText = properties.getProperty(EMAIL_TXT);
					
					String subject = properties.getProperty(EMAIL_SUB)+" - "+imeiNo;
					emailText = emailText +"\n"+listOfValues;
					
					sendMail(emailText, emailId, subject);
				}
			}
		} catch (Exception e) {
			LOGGER.severe("Exception while checking status.."+e);
		}
	}
	
	private Session createSession() {
		try {
			final String username = properties.getProperty(EMAIL_ID);
	        final String password = properties.getProperty(EMAIL_PASS);
			 Properties prop = new Properties();
				prop.put(EMAIL_HOST, EMAIL_HOST_SMTP);
		        prop.put(EMAIL_SMTP_PORT, EMAIL_SMTP_HOST_PORT);
		        prop.put(EMAIL_AUTH, EMAIL_AUTH_TRUE);
		        prop.put(EMAIL_SMTP_ENABLED, EMAIL_AUTH_TRUE); //TLS
		        
	        Session session = Session.getInstance(prop,
	                new javax.mail.Authenticator() {
	                    protected PasswordAuthentication getPasswordAuthentication() {
	                        return new PasswordAuthentication(username, password);
	                    }
	                });
	        return session;
		} catch (Exception e) {
			LOGGER.severe("Exception while create mail session.."+e);
		}
		return null;
	}
	
	public boolean sendMail(String txtMsg, String emailId,String subject) {
		try {
			Session session = createSession();
			if(session == null)
				return false;
			//"ishwarkrishna2@gmail.com"
			final String sendFromEmail = properties.getProperty(EMAIL_ID);
			LOGGER.info("Email session : "+session);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sendFromEmail));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(emailId)
            );
            message.setSubject(subject);
            message.setText(txtMsg);
            LOGGER.info("going to send an email.");
            Transport.send(message);
            session.getTransport().close();
            LOGGER.info("Email done");
		} catch (Exception e) {
			
		}
		return true;
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		/*MailService service = new MailService();
		service.sendMail();*/		
    }
}
