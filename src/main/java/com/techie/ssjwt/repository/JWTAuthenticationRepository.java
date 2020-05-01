package com.techie.ssjwt.repository;

import com.techie.ssjwt.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JWTAuthenticationRepository  extends JpaRepository<UserInfo,Integer> {

    UserInfo findByUsername(String username);
}
