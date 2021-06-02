package net.liuxuan.db.repository;

import net.liuxuan.db.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserGroupRepository extends JpaRepository<UserGroup, Integer>, JpaSpecificationExecutor<UserGroup> {

}