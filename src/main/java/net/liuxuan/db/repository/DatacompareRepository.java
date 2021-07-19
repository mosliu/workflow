package net.liuxuan.db.repository;

import net.liuxuan.db.entity.Datacompare;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DatacompareRepository extends JpaRepository<Datacompare, Integer>, JpaSpecificationExecutor<Datacompare> {

}