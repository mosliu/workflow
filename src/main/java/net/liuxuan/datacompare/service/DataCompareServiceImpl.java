package net.liuxuan.datacompare.service;

import lombok.extern.slf4j.Slf4j;
import net.liuxuan.common.BaseServiceImpl;
import net.liuxuan.db.entity.Datacompare;
import net.liuxuan.db.repository.DatacompareRepository;
import org.springframework.stereotype.Service;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-07-13
 **/
@Service
@Slf4j
public class DataCompareServiceImpl extends BaseServiceImpl<Datacompare, Integer, DatacompareRepository> implements DataCompareService {
}
