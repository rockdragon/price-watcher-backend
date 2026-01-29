package me.moye.hew.service;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import me.moye.hew.database.PriceTrendRepository;
import me.moye.hew.database.ProductRepository;
import me.moye.hew.model.PriceTrend;
import me.moye.hew.model.Product;
import me.moye.hew.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Locale;

@Service
public class SpiderService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PriceTrendRepository priceTrendRepository;
    @Autowired
    private NotificationService notificationService;

    public Product crawlProduct(String asin) throws ParseException {
        var product = productRepository.findByAsin(asin).orElse(null);

        String pageUrl = String.format("https://www.amazon.co.jp/dp/%s", asin);

        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            Page page = browser.newPage();
            page.navigate(pageUrl);

            if (product == null) {
                product = new Product();
                // ASIN
                product.setAsin(asin);
                // page url
                product.setPageUrl(pageUrl);
                // name
                String productName = page.locator("#title_feature_div #productTitle").innerText();
                product.setName(productName);
                // photo url
                String photoUrl = page.locator("#imgTagWrapperId img").first().getAttribute("src");
                product.setPhotoUrl(photoUrl);
                // save to DB
                productRepository.save(product);
            }

            // crawl out of stock
            var outOfStock = page.querySelector("#outOfStock") != null;
            product.setOutOfStock(outOfStock);
            // save to DB
            productRepository.save(product);

            // out of stock has no price
            if (!outOfStock) {
                // crawl the price of product
                String priceText = page.locator("#corePriceDisplay_desktop_feature_div .a-price-whole").first().innerText();
                NumberFormat format = NumberFormat.getInstance(Locale.JAPAN);
                Number number = format.parse(priceText);

                var priceTrend = priceTrendRepository.findTopByProductIdOrderByPriceDateDesc(product.getId()).orElse(null);
                if (priceTrend == null) {
                    priceTrend = new PriceTrend();
                    // price
                    priceTrend.setPrice(number.longValue());
                    // product id
                    priceTrend.setProductId(product.getId());
                    // price date
                    var nowDateNumber = Utils.date2yyyyMMddNumber(LocalDate.now());
                    priceTrend.setPriceDate(nowDateNumber);
                    // save to DB
                    priceTrendRepository.save(priceTrend);
                } else {
                    long newPrice = number.longValue();
                    if (priceTrend.getPrice() != newPrice) {
                        int nowDateNumber = Utils.date2yyyyMMddNumber(LocalDate.now());
                        if (priceTrend.getPriceDate() < nowDateNumber) {
                            // Checks for product price changes and notifies the recipient by email if necessary.
                            notificationService.checkPriceAndNotifyIfNecessary(priceTrend.getProductId(), newPrice);

                            // A new price will be inserted.
                            var priceTrendNew = new PriceTrend();
                            // price
                            priceTrendNew.setPrice(newPrice);
                            // product id
                            priceTrendNew.setProductId(product.getId());
                            // price date
                            priceTrendNew.setPriceDate(nowDateNumber);
                            // save to DB
                            priceTrendRepository.save(priceTrendNew);
                        }
                    }
                }
            }

            return product;
        }
    }
}
