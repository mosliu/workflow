package net.liuxuan.db.repository;

import net.liuxuan.db.entity.MenuPrivilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MenuPrivilegeRepository extends JpaRepository<MenuPrivilege, Integer>, JpaSpecificationExecutor<MenuPrivilege> {

}