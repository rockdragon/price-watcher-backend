package me.moye.hew.tasks;

import me.moye.hew.database.ProductRepository;
import me.moye.hew.service.ProductService;
import me.moye.hew.service.SpiderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultLifecycleProcessor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import me.moye.hew.model.Product;

import java.text.ParseException;

@Service
@Component
public class PriceScheduler {
    Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SpiderService spiderService;

    @Scheduled(cron = "0 0 * * * *")
    public void refreshPriceTrend() throws ParseException, InterruptedException {
        logger.info("[refreshPriceTrend] BEGIN...");

        // fetch all product where it was monitored
        var allProductInMonitored = productRepository.findByIsMonitoredTrue();
        logger.info("[refreshPriceTrend] モニタリングのプロダクトの数量：{}", allProductInMonitored.size());

        for (var product : allProductInMonitored) {
            logger.info("[refreshPriceTrend] ID:{}, ASIN: {} about to update",
                    product.getId(), product.getAsin());
            // crawl
            spiderService.crawlProduct(product.getAsin());
            // a little bit of sleep
            Thread.sleep(1000);
        }

        logger.info("[refreshPriceTrend] ...END");
    }
}
