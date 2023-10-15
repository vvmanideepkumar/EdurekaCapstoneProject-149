package com.edureka.controller;

import com.edureka.model.Hotel;
import com.edureka.repository.HotelRepository;
import com.edureka.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.edureka.dataLoader.DataLoader.available;
import static com.edureka.dataLoader.DataLoader.reserved;

@RestController
@RequestMapping("/api/v1/hotels")
public class HotelController {

    @Autowired
    HotelRepository repository;


    @Autowired
    NotificationService svc;

    /**
     * @param hotel
     * @return
     */
    @PostMapping("/createHotel")
    public List<Hotel> registerAHotel(@RequestBody Hotel hotel){
        //By default when hotel is created that hotel status is available
        hotel.setReservationStatus(available);
        System.out.println("received hotel details  as "+ hotel);
        Hotel result = repository.save(hotel);
        if(!Objects.isNull(result)){
            svc.callNotificationServiceToCreateNotification(result.getId(),"Email",
                    "A now hotel has been created with hotel name as"+ hotel.getHotelName());
        }
        System.out.println("A New Hotel  is added to database ");
        return repository.findAll();
    }
    @GetMapping("/getAllHotels")
    public List<Hotel> getAllCustomers(){
        System.out.println("received a get request to fetch all hotels");
        return repository.findAll();
    }


    /**
     * This will be called by Reservation service to check the hotel availability
     *
     * @param hotel
     * @return
     */
    @PostMapping("/getHotelAvailabilityStatus")
    public ResponseEntity getHotelAvailabilityStatus(@RequestBody Hotel hotel){
        System.out.println("received a request to check hotel availability"+hotel);

        Hotel result = repository.findByHotelNameAndRoomType(hotel.getHotelName(), hotel.getRoomType());
        if(result==null){
            List<Hotel> hotelList = repository.findAll();
            List<String> hotelNamesAndRoomTypes = hotelList.stream().map(h -> "\n"+h.getHotelName() + "-------" + h.getRoomType() ).collect(Collectors.toList());
            String errorMessage = "Sorry ! entered hotel and room type combination doesnt exists please choose one among \n hotelName ---- roomType \n" + hotelNamesAndRoomTypes;
            System.out.println(errorMessage);
            return new ResponseEntity(errorMessage,HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(result,HttpStatus.OK);
    }

    /**
     * once the customer made payment Then This api  will be called by Reservation service to update the room status
     * While updating Room availability status also ensure you provide hotel names and room types
     * which are already present, u can either hit http://localhost:8082/api/v1/hotels/getAllHotels endpoint or query the h2 db to get all hotel names and room types
     *
     */
    @PostMapping("/updateRoomReservationStatus")
    public ResponseEntity updateHotelStatus(@RequestBody Hotel request){
        ResponseEntity<String> isRequestVaild = ValidateRoomStatus(request);
        if(isRequestVaild!=null){
            return  isRequestVaild;
        }
        Hotel result = repository.findByHotelNameAndRoomType(request.getHotelName(), request.getRoomType());
        if(result==null){
            List<Hotel> hotelList = repository.findAll();
            List<String> hotelNamesAndRoomTypes = hotelList.stream().map(h -> "\n"+h.getHotelName() + "-------" + h.getRoomType() ).collect(Collectors.toList());
            return new  ResponseEntity(
            "Sorry ! entered hotel and room type combination doesnt exists please choose one among \n hotelName ---- roomType \n"+hotelNamesAndRoomTypes,
                    HttpStatus.BAD_REQUEST);
        }else{
            result.setReservationStatus(request.getReservationStatus());
            Hotel savedStatus = repository.save(result);
            System.out.println("room status has been successfully saved as "+savedStatus);
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    /**
     * u can give only 2 status either AVAILABLE or RESERVED, if u give any other status it will throw exception as 400 BAD request
     *
     * @param request
     * @return
     */
    private ResponseEntity<String> ValidateRoomStatus(Hotel request) {
        List<String> roomAllowedStatuses=new ArrayList<>();
        roomAllowedStatuses.add(available);roomAllowedStatuses.add(reserved);
        String requestedRoomStatus = request.getReservationStatus();
        if(!roomAllowedStatuses.contains(requestedRoomStatus)){
            return new ResponseEntity<>("Sorry ! entered Room status  --> "+requestedRoomStatus+
                    "    is invalid , whereas status can only be "+roomAllowedStatuses,HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    }
