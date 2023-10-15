Note- about this application
================
1) Before starting this application start Notification Service which is another project
2) Because once we call http://localhost:8081/api/v1/payments/createPayment it will internally call Notification service using 
 http://localhost:8080/api/v1/Notifications/register

H2 Url's
-----------------
http://localhost:8081/h2-console/login.jsp

jdbc url:jdbc:h2:mem:testdb
user name: sa
password: 1234
 
Queries
==============
select *from  payments

swagger ui urls
===============
you can get swagger ui url from console also when app ran
if this doesnt work please use postman, i have placed each request and respective url below

http://localhost:8081/swagger-ui.html
 

payment service urls and payloads
=================================
1) POST - to create a payment
if u pass negative amounts it will throw 400 BAD request

http://localhost:8081/api/v1/payments/createPayment

{
    "customerId":2000639,
    "amount":25
}
REsponse for payment success
=========
{
    "paymentStatus": "Payment has been received and saved and successfully invoked Notification REST service ",
    "paymentId": 6
}

2) to issue a refund
 to initiate refund this api will be called
      * note: while raising a refund request payment id is mandatory which u will be get when payment is success, like when u hit api/v1/payments/createPayment api in that api response u will get the paymentid , that payment id u have to pass in the payload

* if u pass any other payment id which doesnt exists or for which payment is already issued,
then u will get 400 as bad request

http://localhost:8081/api/v1/payments/issueRefund
{
    "id":1
}
	  
Error scenario
-------
lets say u have fired refund for id:1 , when u do it again u will get below REsponse indicating the id's which are already present

REsponse
===========
**Unable to process the refund for paymentId -->1 Either paymentId is wrong or refund already issued , payment ids allowed for refund are --->[2, 3, 4, 5, 6]