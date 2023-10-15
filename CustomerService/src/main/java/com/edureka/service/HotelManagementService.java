package com.edureka.service;

import com.edureka.model.Hotel;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.List;


@Service
public class HotelManagementService {

    @Value("${com.edureka.hotel_management_service_getAllHotels_url}")
    String hotelMgmntServiceURL;

    @Autowired
    RestTemplate restTemplate;
    public List<Hotel> callhotelMgmntServiceToGetHotelsList(){
        System.err.println("Received a request to call hotel management service with url"+ hotelMgmntServiceURL);
        ResponseEntity<List> hotels = restTemplate.getForEntity(hotelMgmntServiceURL, List.class);
        List<Hotel> hotelsList=hotels.getBody();
        System.out.println(hotelsList);
        return hotelsList;
    }

//    @PostConstruct
//    public void CheckIfHotelManagementServiceisUp(ConfigurableApplicationContext cxt){
//        System.err.println("calling hotel management service"+ hotelMgmntServiceURL);
//
//        try {
//            restTemplate.getForEntity(hotelMgmntServiceURL, List.class);
//        }catch(RestClientException e){
//            String error = "hotel management service is not up and running, PLs start that application and then only start this application";
//            System.err.println(error +e);
//            cxt.stop();
//        }
//    }

}
