package net.liuxuan.db.repository;

import net.liuxuan.db.entity.ChinaArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChinaAreaRepository extends JpaRepository<ChinaArea, Long>, JpaSpecificationExecutor<ChinaArea> {

}