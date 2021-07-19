package net.liuxuan.datacompare.controller;

import lombok.extern.slf4j.Slf4j;
import net.liuxuan.common.BaseController;
import net.liuxuan.datacompare.service.DataCompareService;
import net.liuxuan.db.entity.Datacompare;
import net.liuxuan.springconf.CommonResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Liuxuan
 * @version v1.0.0
 * @description 数据比较
 * @date 2021-07-13
 **/
@Slf4j
@RestController
@RequestMapping("/datacompare")
public class DataCompareController  extends BaseController<Datacompare, Integer, DataCompareService> {

    /**
     * 查询所有的数据 <br>
     * <p>
     */
    @GetMapping()
    public CommonResponseDto fetchDataCompare(@RequestParam(defaultValue = "false") boolean filter) {
        List<Datacompare> all = baseService.findAll();
        return CommonResponseDto.success(all);
    }
}
