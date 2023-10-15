package com.edureka.dataLoader;

import com.edureka.model.Hotel;
import com.edureka.repository.HotelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DataLoader implements ApplicationRunner {


    @Autowired
    HotelRepository repository;

    @Autowired
    Environment environment;

    //Note dont change these name, because these variables are used in another application
    public static String available = "AVAILABLE";
    public static String reserved = "ALREADY_RESERVED";

    @Override
    public void run(ApplicationArguments arg0) throws Exception {

        List<Hotel> list = new ArrayList<>();


        list.add(new Hotel(20L,"Westin","Luxury", available));
        list.add(new Hotel(20L,"Taj Faluknama", "Luxury", available));
        list.add(new Hotel(20L,"vivanta", "Luxury", reserved));
        list.add(new Hotel(20L,"ITC kohenur", "Luxury", available));
        list.add(new Hotel(20L,"Oakwood Residence", "Luxury", available));
        list.add(new Hotel(20L,"Hotel  Radisson Hyd", "Luxury", available));
        list.add(new Hotel(20L,"Hotel LemonTree", "Luxury", available));
        list.add(new Hotel(20L,"Hotel Holiday Inn", "Luxury", available));
        long currentCount = repository.count();
        System.out.println("current total count of hotel is " + currentCount);
        if (currentCount == 0) {
            list.stream().forEach(hotel -> repository.save(hotel));
            System.err.printf("Total %d hotels data has been auto inserted in database with names --> \n", repository.count());
            System.err.println(list.stream().map(hotel-> hotel.getHotelName()).collect(Collectors.toList()));

        }
        System.out.println("swagger url for this application is " + "localhost:" + environment.getProperty("server.port") + "/swagger-ui.html");
    }
}
