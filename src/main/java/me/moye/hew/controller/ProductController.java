package me.moye.hew.controller;

import me.moye.hew.model.PriceTrend;
import me.moye.hew.model.Product;
import me.moye.hew.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/search")
    public Page<Product> search(
            @RequestParam String keyword,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return productService.search(keyword, pageable);
    }

    @PostMapping("/update_product_monitored")
    public HttpStatus UpdateProductMonitored(
            @RequestParam long id,
            @RequestParam boolean monitored
    ) {
        productService.updateMonitored(id, monitored);
        return HttpStatus.OK;
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/{id}/price_trends")
    public List<PriceTrend> getProductPriceTrends(@PathVariable long id) {
        return productService.getProductPriceTrends(id);
    }
}
