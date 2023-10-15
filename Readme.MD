Question:- https://github.com/akash-coded/spring-framework/discussions/149
i have implemented project discussion number 149 as from the above link


Note:
======================
1)Read me file is available For each and every application ,  pls read it u will get all urls 	 and some information about that application

2)All the sample payloads are available in readme file named  in the same directory 

3) i have placed postman collection For every application, inside application folder
4) For each  application we have 1 seperate H2 data base
5) For each  application we have 1 seperate swagger, alternatively i have attached the
   postman collection for each and every application in that folder itself,
6) INcase if swagger is not working please upload postman collection and use it 
6)  some of data is auto inserted on application startup


Procedure to start application one by one
====================================================
1) first start notification service 
*because it is called by other many other micro services like Customer service,Payment service
ex:-  Because once we call http://localhost:8080/api/v1/customers/register it will internally call Notification service using 
 http://localhost:8081/api/v1/Notifications/register
 Hence first start Notification service first

2) then start PaymentService - This will be called by Reservation service for payments
3) Then start Hotel Management service -This will also be called by Reservation service ,C.S
4) then start customer service - this will call hotel Management service
5) then at last start Reservation service - this will call hotel Management service, payment,Notification services,	Hence start this application atlast




Inter communication flows
===========================
NS- means Notification service  -runs  on  - 8080
Ps- payment service				-runs  on  - 8081
HMS- Hotel Management service	-runs  on  - 8082
CS- means customer service      -runs  on  - 8083
RS- means Reservation service   -runs  on  - 8084

1) when customer registration is success, CS will interact with NS  -done
1a) A customer can see entire hotels list- so CS will call HMS to get hotels list

2) when hotel registration is success, RS will interact with NS
3) When customer cancel reservation, HMS will call RS to cancel,  RS will call PS to raise refund
once refund is success, RS will call NS to send notification
4) to book a room, HMS will call RS to make reservation, 
RS will call PS once payment is success, RS will call NS,
5) once payment is success payment service will call notification service
6) When refund is success then also payment service will call Notification service



Procedure to Test the applications
====================================
1) start all the 5 micro services in above mentioned order

2) create a customer using Customer service , But ensure notification service is up, else it will throw errors (service-to-service communication)
http://localhost:8083/api/v1/customers/register
{
    "name": "sai santhoshi",
    "email": "saisylu1@gmail.com",
    "password": "MANItcs@123"
}
// NOw a customer will be created, indicates customer service is working fine it will call NS
// if notification service is down, u will get errors

2a) using Customer service u can get hotels list- which will internally calls Hotel Management service -- (service-to-service communication)

a customer can see all hotels 
GET- this is a get request
http://localhost:8083/api/v1/customers/getAllHotels

sample response- all Hotels and rooms statuses
===============================================
 {
        "id": 1.0,
        "hotelName": "Westin",
        "roomType": "Luxury",
        "reservationStatus": "AVAILABLE"
    },
    {
        "id": 2.0,
        "hotelName": "Taj Faluknama",
        "roomType": "Luxury",
        "reservationStatus": "AVAILABLE"
    },
    {
        "id": 3.0,
        "hotelName": "vivanta",
        "roomType": "Luxury",
        "reservationStatus": "ALREADY_RESERVED"
    },
    {
        "id": 4.0,
        "hotelName": "ITC kohenur",
        "roomType": "Luxury",
        "reservationStatus": "AVAILABLE"
    },
    {
        "id": 5.0,
        "hotelName": "Oakwood Residence",
        "roomType": "Luxury",
        "reservationStatus": "AVAILABLE"
    },
    {
        "id": 6.0,
        "hotelName": "Hotel  Radisson Hyd",
        "roomType": "Luxury",
        "reservationStatus": "AVAILABLE"
    },
    {
        "id": 7.0,
        "hotelName": "Hotel LemonTree",
        "roomType": "Luxury",
        "reservationStatus": "AVAILABLE"
    },
    {
        "id": 8.0,
        "hotelName": "Hotel Holiday Inn",
        "roomType": "Luxury",
        "reservationStatus": "AVAILABLE"
    }


Booking a room -positive flow
===========================

When u try to book a room all these below intercommunications b/n services takes place
a) first Reservation service (RS) will call Hotel Management service (HMS) to check availability
b) once if it is available , RS wil call payment service(PS) , once payment is success 
c) Then Reservation service (RS) will again called Hotel Management service (HMS) to update the hotel status to reserved



3a) With correct hotel name- and room type
 based on above response try to book a room with correct hotel name which is present and roomtype which is not booked 
Note: - if u dont know hotel name and room type then hit this url to get the default hotel names and u can see whether its avaialbe or not (http://localhost:8082/api/v1/hotels/getAllHotels)

URL to book room
http://localhost:8084/api/v1/reservation/reserveRoom
here westin hotel is not already booked, so try with it 
payload -1
{
    "customerId": 22,
    "hotelName": "Westin",
    "roomType": "Luxury",
    "paymentAmount": 250,
    "accountNumber": 123456
}
payload -2
{
    "customerId": 22,
    "hotelName": "Oakwood Residence",
    "roomType": "Luxury",
    "paymentAmount": 250,
    "accountNumber": 123456
}

3b) Try with a hotel name which is not saved or not present in data base
---------------------------------------------------
Note:- if u hit twice with same payload, 2nd time u cant book because room is already reserved

u can give wrong hotel name or wrong room type which  is not already saved and then  test 
u will get the below error

ex:- here we dont have a hotel called edureka, so test with below payload url RS and see the error
http://localhost:8084/api/v1/reservation/reserveRoom
{
    "customerId": 22,
    "hotelName": "edureka",
    "roomType": "Luxury",
    "paymentAmount": 250,
    "accountNumber": 123456
}
// Now booking fails and u will get error like below which gives the allowed hotel name and room types which we should provide 

error:-
Sorry!!!, please Ensure Hotel management is up or not. 
 Unable to check the hotel availability as exception occurred by invoking Hotel Management service to with url 
 http://localhost:8082/api/v1/hotels/getHotelAvailabilityStatus
 reason --> null400 : "Sorry ! entered hotel and room type combination doesnt exists please choose one among <EOL> hotelName ---- roomType <EOL>[<EOL>Westin-------Luxury, <EOL>Taj Faluknama-------Luxury, <EOL>vivanta-------Luxury, <EOL>ITC kohenur-------Luxury, <EOL>Oakwood Residence -------Luxury, <EOL>Hotel  Radisson Hyd-------Luxury, <EOL>Hotel LemonTree-------Luxury, <EOL>Hotel Holiday Inn-------Luxury]"

solution:- either u have to use only hotels names came in the response , or 
create a new hotel and use it

3c) Give a hotel name which is already reserved
-------------------------------------
 u can give a hotel name and room type which is already booked and see the error

http://localhost:8084/api/v1/reservation/reserveRoom
// GEnerally by default vivanta hotel is already booked as per DataLoader class 
so when u try also it will give error
{
    "customerId": 22,
    "hotelName": "vivanta",
    "roomType": "Luxury",
    "paymentAmount": 250,
    "accountNumber": 123456
}
Below is the error response  u will get :-
----------------------------
The room is already RESERVED for hotel --> vivanta  and roomType -->Luxury please try with new hotel 


4) Shutdown the payment service 
 Now shutdown the payment service and try to book a room with below payload
http://localhost:8084/api/v1/reservation/reserveRoom
Note: Provide a hotel name which is avaialbe else control will not go to PaymentService 
{
    "customerId": 22,
    "hotelName": "Taj Faluknama",
    "roomType": "Luxury",
    "paymentAmount": 250,
    "accountNumber": 123456
}
Now u will get response as notification service is not up as below
response:- 
Sorry!!!, please Ensure Payment serviceis up or not. 
 Unable to make a payment as exception occurred by invoking Payment service to with url 
 http://localhost:8081/api/v1/payments/createPayment
 reason --> java.net.ConnectException: Connection refused: connectI/O error on POST request for "http://localhost:8081/api/v1/payments/createPayment": Connection refused: connect; nested exception is java.net.ConnectException: Connection refused: connect

5)Shut down hotel Management service
-----------------------------
 you can shutdown Hotel Management servie and test Reservation service and u will get errors saying up the HMS

http://localhost:8084/api/v1/reservation/reserveRoom
{
    "customerId": 22,
    "hotelName": "Taj Faluknama",
    "roomType": "Luxury",
    "paymentAmount": 250,
    "accountNumber": 123456
}


sample error response when hotel Management service is down
---------------------------------
Sorry!!!, please Ensure Hotel management is up or not. 
 Unable to check the hotel availability as exception occurred by invoking Hotel Management service to with url 
 http://localhost:8082/api/v1/hotels/getHotelAvailabilityStatus
 reason --> java.net.ConnectException: Connection refused: connectI/O error on POST request for "http://localhost:8082/api/v1/hotels/getHotelAvailabilityStatus": Connection refused: connect; nested exception is java.net.ConnectException: Connection refused: connect

6) Customer service-to Notification service inter communication

// when customer is created it will call notification service 
Now shutdown the Notification service and call customer service it will throw error
http://localhost:8083/api/v1/customers/register
{
    "name": "sai santhoshi",
    "email": "ahsaisylu1sda@gmail.com",
    "password": "MANItcs@123"
}
Now u will get error because, Notification service is not up
sample response:-
---------------
Customer details are saved, but Exception occurred while invoking Notification microservice using REST API 
 with url  http://localhost:8080/api/v1/Notifications/register
 due to org.springframework.web.client.ResourceAccessException: I/O error on POST request for "http://localhost:8080/api/v1/Notifications/register": Connection refused: connect; nested exception is java.net.ConnectException: Connection refused: connect

