package com.hhplus.concert_ticketing.infra.repository.user;

import com.hhplus.concert_ticketing.domain.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUsersRepository extends JpaRepository<Users,Long> {
}
