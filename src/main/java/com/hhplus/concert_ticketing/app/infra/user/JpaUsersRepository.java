package com.hhplus.concert_ticketing.app.infra.user;

import com.hhplus.concert_ticketing.app.domain.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUsersRepository extends JpaRepository<Users,Long> {
}
