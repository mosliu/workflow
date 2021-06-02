package net.liuxuan.db.repository;

import net.liuxuan.db.entity.RoleinfoPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleinfoPrivilegeRepository extends JpaRepository<RoleinfoPrivilege, Integer>, JpaSpecificationExecutor<RoleinfoPrivilege> {

}