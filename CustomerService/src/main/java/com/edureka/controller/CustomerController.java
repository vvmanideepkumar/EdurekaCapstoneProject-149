package com.edureka.controller;

import com.edureka.model.Customer;
import com.edureka.model.Hotel;
import com.edureka.repository.CustomerRepository;
import com.edureka.service.HotelManagementService;
import com.edureka.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @Autowired
    CustomerRepository repository;


    @Autowired
    NotificationService svc;

    @Autowired
    HotelManagementService hotelManagementService;

    /**
     * once customer registration is success, this application will call notification service using REST template
     * Because once we call http://localhost:8083/api/v1/customers/register it will internally call Notification service using
     * http://localhost:8081/api/v1/Notifications/register
     *
     * @param customer
     * @return
     */
    @PostMapping("/register")
    public ResponseEntity registerACustomer(@RequestBody Customer customer) {
        System.out.println("received Customer details  as " + customer);
        Customer result = repository.save(customer);
        ResponseEntity res = svc.callNotificationServiceToCreateNotification(result.getId(), "Email",
                "customer registration is successful with email id " + customer.getEmail());
        System.out.println("customer registration is successful and details are saved in database");
        if(res.getStatusCode()==HttpStatus.OK){
            StringBuffer customers = getCustomerNamesAsString();
            return new ResponseEntity("customer registration is successful and details are called Notification service "+ customers,HttpStatus.OK);
        }
        return res;
    }

    private StringBuffer getCustomerNamesAsString() {
        List<Customer> allCustomers = repository.findAll();
        StringBuffer customers=new StringBuffer(" ");
        allCustomers.stream().forEach(c->{
            customers.append("\n "+c).append(" \n");
        });
        return customers;
    }

    @GetMapping("/getAllCustomers")
    public List<Customer> getAllCustomers() {
        return repository.findAll();
    }

    @GetMapping("/getAllHotels")
    public List<Hotel> getAllHotels() {
        return hotelManagementService.callhotelMgmntServiceToGetHotelsList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {

        Optional<Customer> optional = repository.findById(id);
        if (optional.isPresent()) {
            Customer customer = optional.get();
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        // logic
    }
}
