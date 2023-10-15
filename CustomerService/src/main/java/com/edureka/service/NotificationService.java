package com.edureka.service;

import com.google.gson.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * This class is used to call Notification REST service
 */
@Service
public class NotificationService {

    @Value("${com.edureka.notification_service_root_url")
    String notificationServiceRootUrl;
    @Value("${com.edureka.notification_service_create_Notification_url}")
    String notificationServiceRegisterUrl;

    @Autowired
    RestTemplate restTemplate;

    public ResponseEntity callNotificationServiceToCreateNotification(Long customerId, String notificationType, String message){

        try{
            JsonObject obj= new JsonObject();
            obj.addProperty("message",message);
            obj.addProperty("customerId",customerId);
            obj.addProperty("type",notificationType);
            HttpEntity<JsonObject> request= new HttpEntity<>(obj);


            ResponseEntity<List> exchange = restTemplate.exchange(notificationServiceRegisterUrl,
                    HttpMethod.POST, request, List.class);
            System.err.println("Successfully Invoked Notification service using REST template with url "+notificationServiceRegisterUrl);
            return exchange;
        }catch (Exception e){
            String msg = "Customer details are saved, but Exception occurred while invoking Notification microservice using REST API \n with url  " + notificationServiceRegisterUrl + "\n due to " + e;
            System.err.println(msg);
            return new ResponseEntity(msg, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
