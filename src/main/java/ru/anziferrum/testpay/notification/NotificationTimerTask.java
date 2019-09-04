package ru.anziferrum.testpay.notification;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.anziferrum.testpay.util.Util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author anziferrum
 */
@Component
public class NotificationTimerTask {
    private NotificationManager notificationManager = NotificationManager.getInstance();

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificationTimerTask.class);

    @Scheduled(fixedRate = Util.NOTIFICATION_PERIOD)
    public void run() {
        LOGGER.info("NotificationTimerTask start.");

        List<NotificationBean> success = new ArrayList<>();

        notificationManager.getQueue().forEach(n -> {
            String notification = new JSONObject().
                    put("currency", n.getCurrency()).
                    put("amount", "" + n.getAmount()).
                    put("id", n.getId()).
                    put("extrenal_id", n.getExternalId()).
                    put("status", n.getTransactionState().name()).
                    put("sha2sig", n.getSignature()).
                    toString();

            try {
                LOGGER.info("Trying to send notification, attempt #{}", (n.getExecuted() + 1));

                CloseableHttpClient client = HttpClients.createDefault();
                HttpPost httpPost = new HttpPost(n.getNotificationURL());

                StringEntity entity = new StringEntity(notification);
                httpPost.setEntity(entity);
                httpPost.setHeader("Accept", "application/json");
                httpPost.setHeader("Content-type", "application/json");

                CloseableHttpResponse response = client.execute(httpPost);

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == HttpStatus.OK.value()) {
                    success.add(n);
                    LOGGER.info("Success!");
                } else {
                    n.inc();
                    LOGGER.info("Got status code = {}, leaving this notification for future.", statusCode);
                }

                client.close();
            } catch (IOException e) {
                n.inc();
                LOGGER.error("Cannot send notification due to exception!", e);
            }
        });

        success.forEach(notificationManager::dequeue);

        List<NotificationBean> expired = new ArrayList<>();

        notificationManager.getQueue().stream().filter(n -> n.getExecuted() == Util.ATTEMPTS).forEach(n -> {
            String notification = new JSONObject().
                    put("currency", n.getCurrency()).
                    put("amount", "" + n.getAmount()).
                    put("id", n.getId()).
                    put("extrenal_id", n.getExternalId()).
                    put("status", n.getTransactionState().name()).
                    put("sha2sig", n.getSignature()).
                    toString();

            LOGGER.info("Notification {} has expired, removing it from notification queue!", notification);

            expired.add(n);
        });

        expired.forEach(notificationManager::dequeue);

        LOGGER.info("NotificationTimerTask end.");
    }
}
