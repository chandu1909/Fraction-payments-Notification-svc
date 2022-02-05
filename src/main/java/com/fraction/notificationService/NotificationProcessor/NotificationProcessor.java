package com.fraction.notificationService.NotificationProcessor;
import com.fraction.notificationService.NotificationListener.NotificationListener;
import com.fraction.notificationService.appConfig.NotificationConstants;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Random;

/**
 * @Author: Chandra sekhar Polavarapu
 * This class is a mock class which uses HTTP client to call external SMS / EMAIL delivery services and acts based on their response.
 * Here, we are only mocking the services.
 */

@Service
public class NotificationProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationProcessor.class);

    @Autowired
    NotificationListener notificationListener;

    Random random = new Random();
    JSONParser jsonParser = new JSONParser();


    //SMS http client mock
    public void sendSmsNotification(JSONObject notification) {

        //Assuming this class has access to the phone numbers of the users
        JSONObject transactionObject = new JSONObject((LinkedHashMap) notification.get("transaction"));
        JSONObject accountObject = new JSONObject((LinkedHashMap) transactionObject.get("account"));
        JSONObject userObject = new JSONObject((LinkedHashMap) accountObject.get("user"));
        String sms = "";
        JSONObject response;
        try {
            sms = "Hi ! " + userObject.get(NotificationConstants.FIRST_NAME) + " your " +
                    notification.get(NotificationConstants.MESSAGE);
            //Typically, the response would be a HTTPClient response, for mocking purpose I am considering JSON.
            // generating random boolean flag to generate failures sometimes.

            response = mockSMSDeliveryMethod("2704217200", sms, random.nextBoolean());

            if (response.get(NotificationConstants.HTTP_RESPONSE_CODE).toString().equals(NotificationConstants.SUCCESS_CODE)) {
                LOGGER.info("SMS has been delivered to customer, transaction ID: {} and the message :{}",
                        transactionObject.get(NotificationConstants.TRANSACTION_ID), response.get(NotificationConstants.SMS_TEXT));
            } else {
                LOGGER.error("Unable to deliver the message to customer on phone. SRE needs to initiate alternative approach, transaction ID: {} and the message : {}",
                        transactionObject.get(NotificationConstants.TRANSACTION_ID), response.get(NotificationConstants.SMS_TEXT));
            }
        } catch (Exception e) {
            LOGGER.error(" Exception occurred while processing the notification delivery {}", e.getMessage());
        }

    }


    private JSONObject mockSMSDeliveryMethod(String phoneNumber, String message, boolean randomFlag) {

        JSONObject mockResponse = new JSONObject();
        if (!randomFlag) {
            mockResponse.put(NotificationConstants.STATUS, NotificationConstants.DELIVERY_FAILED);
            mockResponse.put(NotificationConstants.HTTP_RESPONSE_CODE, NotificationConstants.FAILURE_CODE);
            mockResponse.put("sms_text", message);
        } else {
            mockResponse.put(NotificationConstants.STATUS, NotificationConstants.DELIVERY_FAILED);
            mockResponse.put(NotificationConstants.HTTP_RESPONSE_CODE, NotificationConstants.SUCCESS_CODE);
            mockResponse.put("sms_text", message);
        }
        return mockResponse;

    }


}
