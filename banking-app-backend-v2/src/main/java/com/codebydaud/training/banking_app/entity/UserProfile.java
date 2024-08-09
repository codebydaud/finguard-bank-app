//package com.codebydaud.training.banking_app.entity;
//
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.time.LocalDate;
//
//
//@Getter
//@Setter
//@Entity (name = "user_profiles")
//public class UserProfile {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long profileId;
//
//    private String email;
//    private String phoneNumber;
//    private LocalDate dateOfBirth;
//
//    @OneToOne(mappedBy = "userProfile")
//    private User user;
//}