package net.liuxuan.db.repository;

import net.liuxuan.db.entity.UserinfoRoleinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserinfoRoleinfoRepository extends JpaRepository<UserinfoRoleinfo, Integer>, JpaSpecificationExecutor<UserinfoRoleinfo> {

}