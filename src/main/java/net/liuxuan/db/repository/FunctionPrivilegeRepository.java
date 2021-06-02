package net.liuxuan.db.repository;

import net.liuxuan.db.entity.FunctionPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FunctionPrivilegeRepository extends JpaRepository<FunctionPrivilege, Integer>, JpaSpecificationExecutor<FunctionPrivilege> {

}