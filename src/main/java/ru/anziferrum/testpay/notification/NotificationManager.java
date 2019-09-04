package ru.anziferrum.testpay.notification;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anziferrum
 */
public class NotificationManager {
    private static NotificationManager instance;

    private NotificationManager() {}

    public static NotificationManager getInstance() {
        if (instance == null) {
            instance = new NotificationManager();
        }

        return instance;
    }

    private List<NotificationBean> queue = new ArrayList<>();

    public void enqueue(NotificationBean notification) {
        queue.add(notification);
    }

    public void dequeue(NotificationBean notification) {
        queue.remove(notification);
    }

    public synchronized List<NotificationBean> getQueue() {
        return queue;
    }
}
