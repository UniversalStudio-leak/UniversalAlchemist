package ru.universalstudio.universalalchemist;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.plugin.*;
import org.bukkit.command.*;
import org.bukkit.plugin.java.*;
import net.milkbowl.vault.economy.*;
//import ru.universalstudio.universalalchemist.gui.InventoryManager; usage???
import ru.universalstudio.universalalchemist.utils.*;

/**
 * @Author source code: NaulbiMIX
 * @Author plugin code: UniversalStudio
 * @Author default source code: WinLocker02
 */

public class AlchemistPlugin extends JavaPlugin implements CommandExecutor {

    private Economy economy;
    private static AlchemistPlugin instance;
 /*  Usage???
    private static InventoryManager invManager;
    public static InventoryManager manager() { return invManager; }
  */

    public static AlchemistPlugin getInstance() {
        return instance;
    }

    public Economy getEconomy() {
        return this.economy;
    }

    /**
     * Auto-generation
     */
    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "[UniversalCode] Plugin recompiled and cracked by NaulbiMIX | Sponsored by FlatiCommunity (https://t.me/flaticommunity) | Specially publication for https://teletype.in/@naulbimix/rumine"); // да и кстати на деле если чё, то сурсы писал я сам, идею брал у универсалов. а по закону идею пиздить не запрещёно, поэтому у меня авторское право на это говно :)
            /* вырезанный код из SmartInvs, который скорей всего в сурсах имеется, но при декомпиле его нету...
        invManager = new InventoryManager(this);
        invManager.init();
            */
            if ((new UGuard("лицензию спиздили", "и крякнули", this)).register()) {
            instance = this;
            Utils.getConfig();
            this.getCommand("ualchemist").setExecutor(this);
            if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
                RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class); // я бы всё равно здесь сделал проверку на нулл или на трай кэтч
                this.economy = rsp.getProvider();
            }
            ConsoleCommandSender var1337 = Bukkit.getConsoleSender(); // ну ладно...
            var1337.sendMessage("");
            var1337.sendMessage(Utils.color("&a| &dUniversalAlchemist &f- Версия: &c" + this.getDescription().getVersion())); // пастой запахло
            var1337.sendMessage("");
        }
    }

    public boolean withdraw(Player player, double money) {
        if (this.economy == null) {
            return true;
        }
        return (this.economy.getBalance(player) < money) ? false : this.economy.withdrawPlayer(player, money).transactionSuccess();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) { // явно тогда винлокер был бухой
        if (!(sender instanceof Player)) {
            Utils.sendMessage(sender, Utils.getMessage("only-players"));
            return true;
        } else if (!Utils.has(sender, "ualchemist.alchemist")) {
            // а сообщение что он бомж безправный?
            return true;
        } else {
            Player player = (Player)sender;
            if (Utils.getPotions(player).isEmpty()) {
                Utils.sendMessage(player, Utils.getMessage("potions-empty"));
                return true;
            } else {
                (new AlchemistGui()).open(player);
                return true;
            }
        }
    }

}
