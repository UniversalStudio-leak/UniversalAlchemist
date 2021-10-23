package ru.universalstudio.universalalchemist;

import org.bukkit.*;
import org.bukkit.plugin.*;

/**
 * @Author source code: NaulbiMIX
 * @Author plugin code: UniversalStudio
 */

public class UGuard {

    private String licenseKey;
    private Plugin plugin;
    private String validationServer;
    private ru.universalstudio.universalalchemist.UGuard.LogType logType;
    private String securityKey;
    private boolean debug;

    public UGuard(String lisenceKey, String validationServer, Plugin plugin) {
        this.logType = ru.universalstudio.universalalchemist.UGuard.LogType.NORMAL;
        this.securityKey = "YecoF0I6M05thxLeokoHuW8iUhTdIUInjkfF";
        this.debug = false;
        this.licenseKey = lisenceKey;
        this.plugin = plugin;
        this.validationServer = validationServer;
    }

    public UGuard setSecurityKey(String securityKey) {
        this.securityKey = securityKey;
        return this;
    }

    public UGuard setConsoleLog(ru.universalstudio.universalalchemist.UGuard.LogType consoleLog) {
        this.logType = consoleLog;
        return this;
    }

    public UGuard debug() {
        this.debug = true;
        return this;
    }

    public boolean register() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[UniversalAlchemist] Plugin is enabled! Product by vk.com/universalstudio " + ChatColor.GOLD + "and cracked by NaulbiMIX");
        return true;
    }

    public boolean isValidSimple() {
        return this.isValid() == ru.universalstudio.universalalchemist.UGuard.ValidationType.VALID;
    }

    public ru.universalstudio.universalalchemist.UGuard.ValidationType isValid() {
        return ru.universalstudio.universalalchemist.UGuard.ValidationType.VALID;
    }

    private void log(int priority, String message) {
        if (this.logType != ru.universalstudio.universalalchemist.UGuard.LogType.NONE && (this.logType != ru.universalstudio.universalalchemist.UGuard.LogType.LOW || priority != 0)) {
            System.out.println(message);
        }
    }

    enum ValidationType {
        WRONG_RESPONSE,
        PAGE_ERROR,
        URL_ERROR,
        KEY_OUTDATED,
        KEY_NOT_FOUND,
        NOT_VALID_IP,
        INVALID_PLUGIN,
        VALID;
    }

    enum LogType {
        NORMAL,
        LOW,
        NONE;
    }

}