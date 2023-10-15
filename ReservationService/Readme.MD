Note- about this application
================
1) Before starting this application start Notification Service which is another project
2) Because once we hotel booking is success  http://localhost:8084/api/v1/reservation/reserveRoom 
it will internally call Notification service using http://localhost:8081/api/v1/Notifications/register
3) 

H2 Url's
-----------------
http://localhost:8084/h2-console/login.jsp

jdbc url:jdbc:h2:mem:testdb
user name: sa
password: 1234
 
Queries
==============
select * from reservations

swagger ui urls
===============
you can get swagger ui url from console also when app ran
http://localhost:8084/swagger-ui.html
 

Reservation service urls and payloads
=================================
1) To reserve a room
POST request 
   http://localhost:8084/api/v1/reservation/reserveRoom
   Note:- while giving hotel name enter only hotel name and room types from this url http://localhost:8082/api/v1/hotels/getAllHotels 

{
   "customerId": 22,
   "hotelName": "Westin",
   "roomType": "Luxury",
   "paymentAmount": 250,
   "accountNumber": 123456
   }

2) POST - to create an reservation

http://localhost:8080/api/v1/customers/register
{
    "name": "sai santhoshi",
    "email": "saisylu1@gmail.com",
    "password": "MANItcs@123"
}
