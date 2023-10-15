package com.edureka.repository;

import com.edureka.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    public Hotel findByHotelNameAndRoomType(String roomType,String hotelName);

}
