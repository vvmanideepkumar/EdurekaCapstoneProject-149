package com.edureka.controller;

import com.edureka.model.Hotel;
import com.edureka.model.HotelBookingRequest;
import com.edureka.model.Reservation;
import com.edureka.repository.ReservationRepository;
import com.edureka.service.HotelManagementService;
import com.edureka.service.NotificationService;
import com.edureka.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/reservation")
public class ReservationController {

    @Autowired
    ReservationRepository repository;

    //Note dont change these name, because these variables are used in another application
    public static String available = "AVAILABLE";
    public static String reserved = "ALREADY_RESERVED";
    @Autowired
    NotificationService svc;

    @Autowired
    HotelManagementService hotelService;

    @Autowired
    PaymentService paymentService;

    /**
     * once customer registration is success, this application will call notification service using REST template
     Because once we call http://localhost:8080/api/v1/customers/register it will internally call Notification service using
     http://localhost:8081/api/v1/Notifications/register
     * @param
     * @return
     */
    @PostMapping("/reserveRoom")
    public ResponseEntity registerACustomer(@RequestBody HotelBookingRequest request){
        System.out.println("request received to book a room in  hotel with details "+request);
        Reservation reservation=Reservation.builder().build();

        //Check if hotel is available or not
        ResponseEntity hotelResponseEntity = hotelService.callHotelMgmntSvcAndGetHotelAvailabilityStatus(request);

        if(hotelResponseEntity.getStatusCode()==HttpStatus.OK && isRoomAvailable(hotelResponseEntity)){
        //When room is available then call payment service
            Hotel h =(Hotel) hotelResponseEntity.getBody();
            ResponseEntity paymentResponseEntity = CallPaymentService(request);
            if (paymentResponseEntity.getStatusCode()!=HttpStatus.OK){
                return paymentResponseEntity;
            }else{
                //Now the payment is success, just we have to call notification service to and save reservation and update the hotel status to booked
                reservation.setCustomerId(request.getCustomerId());
                reservation.setHotelId(h.getId());
                reservation.setStartDate(LocalDateTime.now().toString());
                reservation.setEndDate(LocalDateTime.now().plusDays(1).toString());
                repository.save(reservation);
                hotelService.updateHotelStatusTokBookedUsingHotelMgmntService(request,reserved);
                List<Reservation> all = repository.findAll();
                StringBuffer hotelsList=new StringBuffer(" ");
                        all.stream().forEach(res->{
                            hotelsList.append("\n "+res).append(" \n");
                        });
                return new ResponseEntity("Great, Reservation is suucess \n Total reservations so far are \n "+ hotelsList,HttpStatus.OK);
            }

        }else if(hotelResponseEntity.getStatusCode()==HttpStatus.OK &&  !isRoomAvailable(hotelResponseEntity)){
            String reserved = "The room is already RESERVED for hotel --> "+request.getHotelName()+"  and roomType -->"+request.getRoomType()+" please try with new hotel ";
            return new ResponseEntity<>(reserved,HttpStatus.BAD_REQUEST);
        }
        return hotelResponseEntity;
    }

    private ResponseEntity CallPaymentService(HotelBookingRequest request) {
        return paymentService.callPaymentSvcAndMakePayment(request);
//        if(paymentResponseEntity.getStatusCode()==HttpStatus.OK){
//            return paymentResponseEntity
//        }else{
//            return paymentResponseEntity;
//        }
//        return null;
    }

    private boolean isRoomAvailable(ResponseEntity responseEntity) {
        Hotel h = (Hotel) responseEntity.getBody();
        return available.equalsIgnoreCase(h.getReservationStatus());
    }


    @GetMapping("/getAllReservations")
    public List<Reservation> getAllCustomers(){
        return repository.findAll();
    }

    @GetMapping("/reservationById/{id}")
    public ResponseEntity<Reservation> getCustomerById(@PathVariable Long id) {

        Optional<Reservation> optional = repository.findById(id);
        if(optional.isPresent()){
            Reservation reservation = optional.get();
            return ResponseEntity.ok(reservation);
        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        // logic
    }


    //working
    }
