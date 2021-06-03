package net.liuxuan.db.repository;

import net.liuxuan.db.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Integer>, JpaSpecificationExecutor<UserDetails> {

}