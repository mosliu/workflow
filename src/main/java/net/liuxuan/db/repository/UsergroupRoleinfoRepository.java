package net.liuxuan.db.repository;

import net.liuxuan.db.entity.UsergroupRoleinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UsergroupRoleinfoRepository extends JpaRepository<UsergroupRoleinfo, Integer>, JpaSpecificationExecutor<UsergroupRoleinfo> {

}