package com.boli.userservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Address {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;
  @Column(name = "line1", nullable = false)
  private String line1;
  @Column(name = "line2", nullable = true)
  private String line2;
  @Column(name = "city", nullable = false)
  private String city;
  @Column(name = "state", nullable = false)
  private String state;
  @Column(name = "country", nullable = false)
  private String country;
  @Column(name = "postal_code", nullable = false)
  private String postalCode;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;
}
