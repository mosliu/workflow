package net.liuxuan.db.repository;

import net.liuxuan.db.entity.RoleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleInfoRepository extends JpaRepository<RoleInfo, Integer>, JpaSpecificationExecutor<RoleInfo> {

}