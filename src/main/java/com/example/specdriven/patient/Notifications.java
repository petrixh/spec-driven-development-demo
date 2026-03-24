package com.example.specdriven.patient;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

public final class Notifications {

    private Notifications() {
    }

    public static void showSuccess(String message) {
        Notification notification = Notification.show(message, 3000, Notification.Position.BOTTOM_STRETCH);
        notification.addThemeVariants(NotificationVariant.SUCCESS);
    }

    public static void showError(String message) {
        Notification notification = new Notification(message);
        notification.addThemeVariants(NotificationVariant.ERROR);
        notification.setPosition(Notification.Position.BOTTOM_STRETCH);
        notification.setDuration(0);
        notification.open();
    }

    public static void showWarning(String message) {
        Notification notification = Notification.show(message, 5000, Notification.Position.BOTTOM_STRETCH);
        notification.addThemeVariants(NotificationVariant.WARNING);
    }
}
