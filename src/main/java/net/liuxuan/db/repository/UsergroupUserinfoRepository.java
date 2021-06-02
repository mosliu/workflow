package net.liuxuan.db.repository;

import net.liuxuan.db.entity.UsergroupUserinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UsergroupUserinfoRepository extends JpaRepository<UsergroupUserinfo, Integer>, JpaSpecificationExecutor<UsergroupUserinfo> {

}