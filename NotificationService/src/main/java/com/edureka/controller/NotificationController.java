package com.edureka.controller;

import com.edureka.model.Notification;
import com.edureka.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/Notifications")
public class NotificationController {

    @Autowired
    NotificationRepository repository;

    //working
    @PostMapping("/register")
    public List<Notification> registerANotification(@RequestBody Notification notification) {
        System.out.println("received Notification details  as " + notification);
        repository.save(notification);
        return repository.findAll();
    }

    @GetMapping("/getAllNotifications")
    public List<Notification> getAllNotifications() {
        return repository.findAll();
    }
    @GetMapping("/")
    public String welcome() {
        return "welcome";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> getNotificationById(@PathVariable Long id) {

        Optional<Notification> optional = repository.findById(id);
        if (optional.isPresent()) {
            Notification notification = optional.get();
            return ResponseEntity.ok(notification);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        // logic
    }
}
