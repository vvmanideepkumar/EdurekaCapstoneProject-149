Note- about this application -Inter communication with other applications
================
1) Before starting this application start Notification Service,Hotel management service which are another projects
2) Because once we call http://localhost:8083/api/v1/customers/register it will internally call Notification service using 
 http://localhost:8080/api/v1/Notifications/register
3) A customer can see all hotels   , so this application will call Hotel management service to see hotels list
 http://localhost:8082/api/v1/hotels/getAllHotels

H2 Url's
-----------------
http://localhost:8083/h2-console/login.jsp

jdbc url:jdbc:h2:mem:testdb
user name: sa
password: 1234
 
Queries
==============
select * from customer

swagger ui urls
===============
you can get swagger ui url from console also when app ran
http://localhost:8083/swagger-ui.html
 

Customer service urls and payloads
=================================
1) GET request to fetch all customer 
http://localhost:8083/api/v1/customers/getAllCustomers

2) POST - to create an customer
http://localhost:8083/api/v1/customers/register
{
    "name": "sai santhoshi",
    "email": "saisylu1@gmail.com",
    "password": "MANItcs@123"
}
3) To see all hotels list - this will hit the hotel management service and it will get all the hotels
   http://localhost:8083/api/v1/customers/getAllHotels
