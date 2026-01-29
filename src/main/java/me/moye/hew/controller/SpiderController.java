package me.moye.hew.controller;

import me.moye.hew.service.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import me.moye.hew.model.Product;
import java.text.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/spider")
public class SpiderController {
    Logger logger = LoggerFactory.getLogger(SpiderController.class);

    @Autowired
    private SpiderService spiderService;

    @PostMapping("/crawl/{asin}")
    public Product trySpider(@PathVariable String asin) throws ParseException {
        Product product = spiderService.crawlProduct(asin);
        logger.info("[trySpider] got: {}", product);
        return product;
    }
}
