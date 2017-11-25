package org.pauni.gnomeconnect.models;

/**
 *      Class for keeping relevant information about power&battery
 */

public class BatteryStatus {
    public static final String CHARGING_NOT_CHARGING       = "not charging";
    public static final String CHARGING_CHARGING_AC        = "charging ac";
    public static final String CHARGING_CHARGING_USB       = "charging usb";
    public static final String CHARGING_CHARGING_WIRELESS  = "charging wireless";

    private int level;
    private int temperature;
    private String charging;

    public BatteryStatus(int level, int temperature, String charging) {
        this.level       = level;
        this.temperature = temperature;
        this.charging    = charging;
    }

    public boolean equals(BatteryStatus status) {
        return status != null
                && Math.abs(this.temperature - status.temperature) < 3
                && this.level == status.level
                && this.charging.equals(status.charging);
    }

    public int getLevel() {
        return level;
    }

    public int getTemperature() {
        return temperature;
    }

    public String getCharging() {
        return charging;
    }
}
