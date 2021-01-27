package API_Testing.Functional.Automation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.Test;
import org.zeroturnaround.zip.ZipUtil;

public class PostBuild {
	
	
	
	public static void main(String[] args) throws Exception {
	//@Test
	//public void postExecution() throws Exception {
		System.out.println(" == THIS IS A MAIN CLASS FOR REPORT GENERATION==");
		//System.out.println("copy pasted");
		//1.  Move the Logs.log file to Test_Results folder
		FileUtils.copyFileToDirectory(new File(System.getProperty("user.dir")+"\\Logs.log"), new File(System.getProperty("user.dir")+"\\Test_Result"));
		FileUtils.copyFileToDirectory(new File(System.getProperty("user.dir")+"\\TestData\\Input Json.xls"), new File(System.getProperty("user.dir")+"\\Test_Result"));
		FileUtils.copyFileToDirectory(new File(System.getProperty("user.dir")+"\\CustomReport.html"), new File(System.getProperty("user.dir")+"\\Test_Result"));
		//FileUtils.copyFileToDirectory(new File(System.getProperty("user.dir")+"\\ConsoleOutput.txt"), new File(System.getProperty("user.dir")+"\\Test_Result"));
		//System.out.println("copy pasted");
		//2. Delete the existing Logs.log file
		FileUtils.forceDelete(new File(System.getProperty("user.dir")+"\\Logs.log"));
		FileUtils.forceDelete(new File(System.getProperty("user.dir")+"\\CustomReport.html"));
		//FileUtils.forceDelete(new File(System.getProperty("user.dir")+"\\ConsoleOutput.txt"));
		
		//3. Get LastExecutionTime from config file
		String lastExecTime = readPropertyFromConfig("LastExecutionTime");
		
		//4.  Zip the test-output folder and then move to Test_Results only
		SystemDirectoryUtil.zipDir(System.getProperty("user.dir")+"\\target\\surefire-reports", System.getProperty("user.dir")+"\\Test_Result\\surefire_Zipped");
		
		//5. Create a new folder Reports Timestap, inside Test_Results folder.
		String reportsFileName = "Reports "+lastExecTime;
		SystemDirectoryUtil.createDir( System.getProperty("user.dir")+"\\Test_Result\\"+reportsFileName);
		
		// 6. Zip JsonResponses folder folder and keep it in TestResults folder only.
		SystemDirectoryUtil.zipDir(System.getProperty("user.dir")+"\\Test_Result\\JsonResponses",
				System.getProperty("user.dir")+"\\Test_Result\\JsonResponses_Zipped");
		
		// 7. Copy paste all files from Test_Results to Reports Timestamp folder
		String[] files = {   System.getProperty("user.dir")+"\\Test_Result\\JsonResponses_Zipped", 
											System.getProperty("user.dir")+"\\Test_Result\\surefire_Zipped",
											System.getProperty("user.dir")+"\\Test_Result\\TestData.xls",
											System.getProperty("user.dir")+"\\Test_Result\\Input Json.xls",
											System.getProperty("user.dir")+"\\Test_Result\\CustomReport.html",
											System.getProperty("user.dir")+"\\Test_Result\\ResultData "+lastExecTime+".xls",
											System.getProperty("user.dir")+"\\Test_Result\\ResultSummary "+lastExecTime+".xls",
											System.getProperty("user.dir")+"\\Test_Result\\Logs.log"
											//System.getProperty("user.dir")+"\\Test_Result\\ConsoleOutput.txt"
											};
		SystemDirectoryUtil.copyFilesToDirectory(files, System.getProperty("user.dir")+"\\Test_Result\\"+reportsFileName);
		System.out.println("Copied all files to results directory");
		// 8. Zip the Reports Timestap folder and store inside the TestResult_Dumps folder only.
		SystemDirectoryUtil.createDir(System.getProperty("user.dir")+"\\TestResult_Dumps");
		ZipUtil.pack(new File(System.getProperty("user.dir")+"\\Test_Result\\"+reportsFileName),
				new File(System.getProperty("user.dir")+"\\TestResult_Dumps\\"+reportsFileName));
		System.out.println("Zipped files accordingly");
		Thread.sleep(2000);
		
		// 9. Send mail method call from PostBuild class only
		if(readPropertyFromConfig("SendMail").contains("y") || readPropertyFromConfig("SendMail").contains("Y"))
		{
			String[] toMails = {};String[] ccMails = {};
			toMails = readPropertyFromConfig("MailIds").split(",");
			if(readPropertyFromConfig("SendToCC").contains("y") || readPropertyFromConfig("SendToCC").contains("Y"))
				ccMails = readPropertyFromConfig("CCMails").split(",");
			System.out.println("Preparing mail");
			String[] filesNameArray=new String[] {
					System.getProperty("user.dir")+"\\Test_Result\\ResultData "+lastExecTime+".xls",
					System.getProperty("user.dir")+"\\Test_Result\\ResultSummary "+lastExecTime+".xls",
					System.getProperty("user.dir")+"\\Test_Result\\TestData.xls",
					System.getProperty("user.dir")+"\\Test_Result\\Input Json.xls",
					System.getProperty("user.dir")+"\\Test_Result\\CustomReport.html",
					System.getProperty("user.dir")+"\\Test_Result\\JsonResponses_Zipped",
					//System.getProperty("user.dir")+"\\Test_Result\\surefire_Zipped",
					System.getProperty("user.dir")+"\\Test_Result\\Logs.log"
					//System.getProperty("user.dir")+"\\Test_Result\\ConsoleOutput.txt"
			};
		//	String[] filesNameArray1=new String[] {System.getProperty("user.dir")+"\\TestResult_Dumps\\"+reportsFileName};
			sendBsoftMail(readPropertyFromConfig("FromMailId"), readPropertyFromConfig("FromMailPwd"), toMails, ccMails, filesNameArray,
					"API functional automation test report : " + lastExecTime , "Kindly find the attached reports");
			System.out.println("Mail sent successfully");
			
		}
		
		
	}
	
	
	
	private static String readPropertyFromConfig(String Property) 
	{
		Properties p = new Properties();
		try {
			FileInputStream FI = new FileInputStream(new File(System.getProperty("user.dir")+"\\TestData\\CONFIG.properties"));
			p.load(FI);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return (p.getProperty(Property)); 
	}
	
	private static void sendBsoftMail(final String fromAddress, final String password, String[] toMailIds, String[] ccMailIds, String[] filesNameArray, String subject, String messageBody)
	{
		Properties props = new Properties();
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.port", "465");
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

	    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
	        protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(fromAddress, password);
	        }
	    });
	    Transport transport = null;
	    
	    try {
	        transport = session.getTransport();
	        Message message = new MimeMessage(session);
	        //String messageBody = "<div style=\"color:red;\">" + "mailBody asbca jashsdas hdjjah d" + "</div>";
	        message.setSentDate(new Date());

	        message.setSubject(subject);
	        // message.setContent(mensagem, "text/plain");
	        
	        Multipart mp = new MimeMultipart();
	        BodyPart messageBodyPart = new MimeBodyPart();
	        
	        for(String fileToAttached:filesNameArray){
	            MimeBodyPart mbp2 = new MimeBodyPart();
	            FileDataSource fds = new FileDataSource(new File(fileToAttached));
	            mbp2.setDataHandler(new DataHandler(fds));
	            mbp2.setFileName(fds.getName());
	            mp.addBodyPart(mbp2);
	        }
	        
	        messageBodyPart.setText(messageBody);
	        mp.addBodyPart(messageBodyPart);
	        message.setContent(mp);
	        
	        
	        for(int i = 0; i<toMailIds.length; i++)
            	message.addRecipient(Message.RecipientType.TO, new InternetAddress(toMailIds[i]));
            for(int i = 0; i<ccMailIds.length; i++)
            	message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccMailIds[i]));
	        
	        transport.connect();
	        message.setReplyTo(InternetAddress.parse(fromAddress));
	       // Transport.send(message, InternetAddress.parse("manju.crz@gmail.com"));
	        Transport.send(message);
	        transport.close();      
	    }catch(MessagingException mex) {
            mex.printStackTrace();
	    }
	}
	
}
