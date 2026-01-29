package me.moye.hew.controller;

import me.moye.hew.VOs.PriceReductionNotificationVO;
import me.moye.hew.database.PriceReductionNotificationRepository;
import me.moye.hew.model.PriceReductionNotification;
import me.moye.hew.service.EmailService;
import me.moye.hew.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {
    Logger logger = LoggerFactory.getLogger(NotificationController.class);

    @Autowired
    private PriceReductionNotificationRepository priceReductionNotificationRepository;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private EmailService emailService;

    @GetMapping("/test_email")
    public String testEmail() {
        emailService.sendEmail("moyerock@gmail.com", "HELLO WORLD!", "this is our site:\nhttps://example.org/");
        return "OK";
    }

    @GetMapping("/product/{id}/setting")
    public PriceReductionNotificationVO getPriceReductionNotification(@PathVariable long id) {
        return priceReductionNotificationRepository.getPriceReductionNotifiersByProductId(id)
                .map(PriceReductionNotificationVO::from)
                .orElseGet(PriceReductionNotificationVO::defaultValue);
    }

    @PostMapping("/product/setting")
    public HttpStatusCode updatePriceReductionNotification(@RequestBody PriceReductionNotificationVO vo) {
        notificationService.updateNotificationSetting(vo.productId(), vo.notificationEnabled(), vo.decreaseRate(), vo.recipientEmail());
        return HttpStatus.OK;
    }
}
