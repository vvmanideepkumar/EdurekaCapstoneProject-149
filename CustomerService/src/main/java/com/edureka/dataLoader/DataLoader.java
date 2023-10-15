package com.edureka.dataLoader;

import com.edureka.model.Customer;
import com.edureka.repository.CustomerRepository;
import org.apache.catalina.core.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class DataLoader  implements ApplicationRunner {


    @Autowired
    CustomerRepository repository;

    @Autowired
    Environment environment;
    @Override
    public void run(ApplicationArguments arg0) throws Exception {
        Customer customer =  new Customer(20L,"manideepKumar","vv.manideepKumar","Manideep123");
        long currentCount = repository.count();
        System.out.println("current total count of employees is "+ currentCount);
        if(currentCount==0){
            repository.save(customer);
            System.out.printf("single customer with this data has been auto inserted --> %s \n",customer);
            System.out.println("swagger url for this application is "+ "localhost:"+environment.getProperty("server.port")+"/swagger-ui.html");
        }
    }
}
