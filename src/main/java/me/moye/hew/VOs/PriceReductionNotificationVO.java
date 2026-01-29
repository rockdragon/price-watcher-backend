package me.moye.hew.VOs;

import me.moye.hew.model.PriceReductionNotification;

public record PriceReductionNotificationVO(
        long productId,
        boolean notificationEnabled,
        float decreaseRate,
        String recipientEmail
) {
    public static PriceReductionNotificationVO from(
            PriceReductionNotification entity
    ) {
        return new PriceReductionNotificationVO(
                entity.getProductId(),
                entity.isNotificationEnabled(),
                entity.getDecreaseRate(),
                entity.getRecipientEmail()
        );
    }

    public static PriceReductionNotificationVO defaultValue() {
        return new PriceReductionNotificationVO(0, false, 5.0F, "");
    }
}
