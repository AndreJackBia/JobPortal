package com.notification.notification;

import java.io.InputStream;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

	/**
	 * This method is used to send email notification using Gmail APIs
	 * 
	 * @param notification
	 * @return 206 if data are missing, 500 if not successful, 200 otherwise
	 */
	@RequestMapping(value = "/api/send-notification", method = RequestMethod.POST)
	public ResponseEntity sendNotification(@RequestBody NotificationEntity notification) {
		if(checkField(notification)) {
			try {
				if (notification.getUsername() == null)
					GmailSender.sendMessage(notification.getDestination(), notification.getSubject(), notification.getBody());
				else {
					InputStream in = CouchDBHelper.getDocument(notification.getUsername());
					if (in != null)
						GmailSender.sendMessage(notification.getDestination(), notification.getSubject(), notification.getBody(), in);
					else
						return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).build();
				}
				return ResponseEntity.status(HttpStatus.OK).build();
			} catch (Exception e) {
				e.printStackTrace();
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
			}
		} else {
			return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).build();
		}
	}
	
	public boolean checkFieldMail(String destinationEmail) {
		String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
		  return destinationEmail.matches(regex);
	}
	
	public boolean checkField(NotificationEntity notificationEntity) {
		if ((notificationEntity.getDestination() == null) || 
			(notificationEntity.getDestination().equals("")) || 
			(!checkFieldMail(notificationEntity.getDestination()))) {
			return false;
		}
		if ((notificationEntity.getSubject() == null) || 
			(notificationEntity.getSubject().equals(""))) {
			return false;
		}
		if ((notificationEntity.getBody() == null) ||
			(notificationEntity.getBody().equals(""))){
			return false;
		}
		
		return true;
	}
}