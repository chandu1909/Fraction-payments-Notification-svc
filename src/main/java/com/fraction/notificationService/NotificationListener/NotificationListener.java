package com.fraction.notificationService.NotificationListener;
import com.fraction.notificationService.NotificationProcessor.NotificationProcessor;
import com.fraction.notificationService.appConfig.RabbitMqConfig;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationListener.class);

    NotificationProcessor notificationProcessor = new NotificationProcessor();

    @RabbitListener(queues = RabbitMqConfig.NOTIFICATIONS_QUEUE)
    public void consumeNotifications(JSONObject notification){

        LOGGER.info("Received the notification {} ", notification);
        notificationProcessor.sendSmsNotification(notification);
    }
}
