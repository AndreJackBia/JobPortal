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

	
	@RequestMapping(value = "/api/send-notification", method = RequestMethod.POST)
	public ResponseEntity sendNotification(@RequestBody NotificationEntity notification) {
		if(checkFieldMail(notification)) {
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
	
	
	public boolean checkFieldMail(NotificationEntity destinationMail) {
		if (destinationMail.getDestination().equals(null)) {
			return false;
		}
		if (destinationMail.getSubject().equals(null)) {
			return false;
		}
		if (destinationMail.getBody().equals(null)) {
			return false;
		}
		return true;
	}
}