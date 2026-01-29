package me.moye.hew.service;

import me.moye.hew.database.PriceTrendRepository;
import me.moye.hew.database.ProductRepository;
import me.moye.hew.model.PriceTrend;
import me.moye.hew.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PriceTrendRepository priceTrendRepository;

    public Page<Product> search(String keyword, Pageable pageable) {
        return productRepository.findByNameContaining(keyword, pageable);
    }

    public void updateMonitored(long id, boolean monitored) {
        var productOptional = productRepository.findById(id);
        productOptional.ifPresent(product -> {
            product.setMonitored(monitored);
            productRepository.save(product);
        });
    }

    public Product getProductById(long id) {
        return productRepository.getProductById(id);
    }

    public List<PriceTrend> getProductPriceTrends(long id) {
        return priceTrendRepository.findByProductIdOrderByPriceDateAsc(id);
    }
}
