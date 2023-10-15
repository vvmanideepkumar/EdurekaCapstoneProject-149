package com.edureka.controller;

import com.edureka.model.Payment;
import com.edureka.model.PaymentResponseDTO;
import com.edureka.repository.PaymentRepository;
import com.edureka.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @Autowired
    PaymentRepository repository;


    @Autowired
    NotificationService svc;

    /**
     * This method is used to accept payment and this api will be called by reservation service
     * to make the payment
     * Once payment is success this payment service application will call Notification service
     *
     * @param request
     * @return
     */
    @PostMapping("/createPayment")
    public ResponseEntity makePayment(@RequestBody Payment request) {
        System.out.println("received a request to make a payment for " + request);
        ResponseEntity validationStatus = validate(request);
        if (validationStatus != null) {
            return validationStatus;
        }
        Payment result = repository.save(request);
        PaymentResponseDTO response = PaymentResponseDTO.builder().paymentId(result.getId()).build();

        if (!Objects.isNull(result)) {
            System.err.println("payment is successful, Now calling Notification service to send notification");
            svc.callNotificationServiceToCreateNotification(result.getId(), "Text",
                    "payment registration is successful for customer id " + request.getCustomerId());
            response.setPaymentStatus("Payment has been received and saved and successfully invoked Notification REST service ");
            return new ResponseEntity(response, HttpStatus.OK);
        }
        response.setPaymentStatus("Payment has been received and saved but unable to call   Notification REST service");
        return new ResponseEntity(response, HttpStatus.OK);
    }

    private ResponseEntity validate(Payment request) {
        if (request.getAmount() < 0 || request.getCustomerId() == null) {
            System.err.println("Error invalid input received, " + request);
            return new ResponseEntity("Error invalid input received either amount is negative or customer id is null", HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    private ResponseEntity validateRefundRequest(Payment request) {
        if (request.getId() == null) {
            System.err.println("Error invalid input received, request doesnt contain payment id " + request);
            return new ResponseEntity("Error invalid input received, request doesnt contain payment id, For raising a refund request " +
                    "payment id is mandatory", HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    /**
     * This method is called by Reservation service when person cancels the reservation , to initiate refund this api will be called
     * note: while raising a refund request payment id is mandatory which u will be send when payment is success
     *
     * @param request
     * @return
     */
    @PostMapping("/issueRefund")
    public ResponseEntity issueRefund(@RequestBody Payment request) {
        System.out.println("received a request to issue a refund for " + request);
        ResponseEntity validationStatus = validateRefundRequest(request);
        if (validationStatus != null) {
            return validationStatus;
        }
        boolean isPaymentDeleted = deletePaymentRecordAnGetStatus(request);
        if (isPaymentDeleted) {
            System.err.println("refund is successful, Now calling Notification service to send notification");
            svc.callNotificationServiceToCreateNotification(request.getId(), "Email",
                    "refund is successful for customer id " + request.getCustomerId());
            return new ResponseEntity("Refund has been processed and saved and successfully invoked Notification REST service ", HttpStatus.OK);
        }else{
            List<Payment> paymentList = repository.findAll();
            String paymentIdsAllowedForRefund="";
            if(paymentList!=null && paymentList.size()>0){
                paymentIdsAllowedForRefund=  paymentList.stream().map(p->p.getId()).collect(Collectors.toList()).toString();
            }
            String refundFailedReason = "Unable to process the refund for paymentId -->" + request.getId() +
                    " Either paymentId is wrong or refund already issued , \n payment ids allowed for refund are --->"+paymentIdsAllowedForRefund;
            System.err.println(refundFailedReason);
            return new ResponseEntity(refundFailedReason, HttpStatus.BAD_REQUEST);
        }
    }

    private boolean deletePaymentRecordAnGetStatus(Payment request) {
        Optional<Payment> doesAPaymentExists = repository.findById(request.getId());
        if(doesAPaymentExists.isPresent()){
        repository.deleteById(request.getId());
            System.out.println("deleted a payment with paymentId "+request.getId());
        return true;
        }
    return false;
    }


    //working
}
