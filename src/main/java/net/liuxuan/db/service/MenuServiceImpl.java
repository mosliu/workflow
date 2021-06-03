package net.liuxuan.db.service;

import lombok.extern.slf4j.Slf4j;
import net.liuxuan.db.entity.Menu;
import net.liuxuan.db.repository.MenuRepository;
import org.springframework.stereotype.Service;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description Tools for xx use
 * @date 2021-06-03
 **/
@Service
@Slf4j
public class MenuServiceImpl extends BaseServiceImpl<Menu, Integer, MenuRepository> implements MenuService {
//    public MenuServiceImpl(MenuRepository repository) {
//        super.repository = repository;
//    }
}
