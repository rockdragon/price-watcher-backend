package me.moye.hew.service;

import me.moye.hew.database.PriceReductionNotificationRepository;
import me.moye.hew.database.PriceTrendRepository;
import me.moye.hew.database.ProductRepository;
import me.moye.hew.model.PriceReductionNotification;
import me.moye.hew.model.PriceTrend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {
    Logger logger = LoggerFactory.getLogger(NotificationService.class);

    @Autowired
    private PriceReductionNotificationRepository priceReductionNotificationRepository;
    @Autowired
    private PriceTrendRepository priceTrendRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private EmailService emailService;

    /**
     * Checks for product price changes and notifies the recipient by email if necessary.
     *
     * @param productId
     * @param newPrice
     */
    public void checkPriceAndNotifyIfNecessary(Long productId, long newPrice) {
        var notificationOptional = priceReductionNotificationRepository.getPriceReductionNotifiersByProductId(productId);
        if (notificationOptional.isEmpty()) {
            return;
        }
        var notification = notificationOptional.get();

        // is disabled
        if (!notification.isNotificationEnabled()) {
            return;
        }

        // data volume < 3
        List<PriceTrend> priceTrendList = priceTrendRepository.findByProductIdOrderByPriceDateAsc(productId);
        if (priceTrendList.isEmpty() || priceTrendList.size() < 3) {
            return;
        }

        long averagePrice = Math.round(
                priceTrendList.stream().mapToLong(PriceTrend::getPrice).average().orElse(0.0)
        );
        // is not reduction
        if (newPrice >= averagePrice) {
            return;
        }
        if (newPrice >= priceTrendList.get(priceTrendList.size() - 1).getPrice()) {
            return;
        }

        long threshold = Math.round(averagePrice * notification.getDecreaseRate() / 100);
        if (averagePrice - newPrice >= threshold) {
            var product = productRepository.getProductById(productId);
            var emailSubject = String.format("商品 %s は %.0f％ 下げされ、現在は %d 円になっています。",
                    product.getName(),
                    notification.getDecreaseRate(),
                    newPrice
            );
//            var emailBody = String.format(
//                    emailSubject + "\n\nhttp://localhost:3000/admin/priceTrend/%d",
//                    productId
//            );
            var emailBody = String.format(
                    emailSubject + "\n\n%s",
                    product.getPageUrl()
            );
            emailService.sendEmail(
                    notification.getRecipientEmail(),
                    emailSubject,
                    emailBody
            );
        }
    }


    /**
     * update PriceReductionNotification for specific product
     * @param productId
     * @param notificationEnabled
     * @param decreaseRate
     * @param recipientEmail
     */
    public void updateNotificationSetting(
            Long productId,
            boolean notificationEnabled,
            float decreaseRate,
            String recipientEmail)
    {
        PriceReductionNotification priceReductionNotification;
        var setting = priceReductionNotificationRepository.getPriceReductionNotifiersByProductId(productId);
        priceReductionNotification = setting.orElseGet(PriceReductionNotification::new);
        priceReductionNotification.setProductId(productId);
        priceReductionNotification.setNotificationEnabled(notificationEnabled);
        priceReductionNotification.setDecreaseRate(decreaseRate);
        priceReductionNotification.setRecipientEmail(recipientEmail);
        priceReductionNotificationRepository.save(priceReductionNotification);
    }
}
