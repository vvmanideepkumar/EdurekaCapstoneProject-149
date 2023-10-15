Note- about this application
================
This application is invoked by reservation Micro service

H2 Url's
-----------------
http://localhost:8082/h2-console/login.jsp

jdbc url:jdbc:h2:mem:testdb
user name: sa
password: 1234
 
Queries
==============
select * from hotel

swagger ui urls
===============
you can get swagger ui url from console also when app ran
http://localhost:8082/swagger-ui.html
 

Customer service urls and payloads
=================================
1) GET request to fetch all hotels
   http://localhost:8082/api/v1/hotels/getAllHotels

2) POST - to create an hotel

http://localhost:8082/api/v1/hotels/createHotel

Here u can enter any any hotel name and any room type, By default reservation status is Available
Even if u create with anyother status, it will be overridden as Available, 
as first time we are creating  a hotel the default status will be available

{
"hotelName": "Taj",
"roomType": "Luxury",
}

3) To check the Availability of hotel whether it is reserved or available user follwoing endpoint  http://localhost:8082/api/v1/hotels/getHotelAvailabilityStatus

Note: 
--------------
while entering the hotel name and room type please enter only below combinations unless u create a new hotel
Because we have saved only below hotel names and types in H2 while application startup using DataLoader class
Hotel Name -- Room type
==============================
Westin-------Luxury, 
Taj Faluknama-------Luxury, 
vivnata-------Luxury, 
ITC kohenur-------Luxury, 
Oakwood Residence ----- Luxury, 
Hotel  Radisson Hyd-------Luxury, 
Hotel LemonTree-------Luxury, 
Hotel Holiday Inn-------Luxury

GET 
http://localhost:8082/api/v1/hotels/getHotelAvailabilityStatus
{
    "hotelName": "vivnata",
    "roomType": "Luxury"
}




4) To update Room Availability status
------------
While updating Room availability status also ensure you provide hotel names and room types 
which are already present, u can either hit http://localhost:8082/api/v1/hotels/getAllHotels endpoint or query the h2 db to get all hotel names and room types

Note:- 
u can give only 2 status either AVAILABLE or RESERVED, if u give any other status it will throw exception as 400

http://localhost:8082/api/v1/hotels/updateRoomReservationStatus
{
    "hotelName": "Taj Faluknama",
    "roomType": "Luxury",
    "reservationStatus":"RESERVED"
    
}





