package net.minevn.pointstransfer;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

public class Utils {
    private static final PointsTransfer instance = PointsTransfer.getInstance();

    @Contract("_ -> new")
    public static @NotNull String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> color(@NotNull List<String> text) {
        return text.stream().map(Utils::color).toList();
    }

    public static @NotNull ItemStack createItemStack(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(color(name));
        meta.setLore(color(List.of("", "&7Chuyển hết số Points hiện có", "&7sang cụm này", "", "&c&lKHÔNG &cthể hoàn tác!", "", "&eNhấn để chuyển")));
        item.setItemMeta(meta);

        return item;
    }

    public static @NotNull String getDate() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
    }

    public static void runAsync(Runnable runnable) {
        instance.getServer().getScheduler().runTaskAsynchronously(instance, runnable);
    }

    public static void log(String msg, Level lvl) {
        instance.getLogger().log(lvl, msg);
    }
}