package com.houkstead.ticketsystem.repository;

import com.houkstead.ticketsystem.models.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {
    UserInfo findByUserId(int userid);
}
