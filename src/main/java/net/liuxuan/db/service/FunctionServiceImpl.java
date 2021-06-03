package net.liuxuan.db.service;

import lombok.extern.slf4j.Slf4j;
import net.liuxuan.db.entity.Function;
import net.liuxuan.db.repository.FunctionRepository;
import org.springframework.stereotype.Service;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-06-03
 **/
@Service
@Slf4j
public class FunctionServiceImpl extends BaseServiceImpl<Function, Integer, FunctionRepository> implements FunctionService {

//    public FunctionServiceImpl(FunctionRepository repository) {
//        super.repository = repository;
//    }
}
