package com.boli.userservice.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.boli.common.enums.RoleType;
import com.boli.common.enums.UserStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "users")
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id;
  @Column(name = "username", nullable = false, unique = true)
  private String username;
  @Column(name = "full_name", nullable = false)
  private String fullName;
  @Column(name = "email", nullable = false, unique = true)
  private String email;
  @Column(name = "password", nullable = false)
  private String password;
  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;
  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false)
  private RoleType role;
  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private UserStatus status;
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  @Builder.Default
  private List<Address> addresses = new ArrayList<>();

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority(role.asAuthority()));
  }

  @Override
  public boolean isAccountNonExpired() {
    return status != UserStatus.DELETED;
  }

  @Override
  public boolean isAccountNonLocked() {
    return status != UserStatus.BLOCKED;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return status != UserStatus.INACTIVE;
  }

  public void addAddress(Address address) {
    addresses.add(address);
    address.setUser(this);
  }

  public void removeAddress(Address address) {
    addresses.remove(address);
    address.setUser(null);
  }
}
