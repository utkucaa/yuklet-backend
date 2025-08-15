package com.project.yuklet.reporsitory;

import com.project.yuklet.entities.User;
import com.project.yuklet.entities.UserType;
import com.project.yuklet.entities.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByPhone(String phone);
    
    List<User> findByUserType(UserType userType);
    
    List<User> findByStatus(UserStatus status);
    
    boolean existsByEmail(String email);
    
    boolean existsByPhone(String phone);
}
