package net.liuxuan.db.repository;

import net.liuxuan.db.entity.Function;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FunctionRepository extends JpaRepository<Function, Integer>, JpaSpecificationExecutor<Function> {

}