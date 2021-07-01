package net.liuxuan.db.repository;

import net.liuxuan.db.entity.UserDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserDetailsRepository extends JpaRepository<UserDetailsEntity, Integer>, JpaSpecificationExecutor<UserDetailsEntity> {

}