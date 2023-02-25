package net.minevn.pointstransfer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class GUI implements Listener {
    private final Inventory inv;
    private final PointsTransfer main;

    public GUI(@NotNull PointsTransfer main) {
        this.main = main;
        inv = main.getServer().createInventory(null, 45, "§8Chuyển Points");
        setItems();
    }

    public void setItems() {
        var serverConfig = Objects.requireNonNull(main.getConfig().getConfigurationSection("servers")).getKeys(false);
        serverConfig.forEach(id -> {
            var server = main.getConfig().getConfigurationSection("servers." + id);
            String name = server.getString("name");
            Material icon = Material.valueOf(server.getString("icon"));
            int slot = server.getInt("slot");
            inv.setItem(slot, Utils.createItemStack(icon, name));
        });
    }

    public void open(@NotNull Player player) {
        player.openInventory(inv);
    }

    @EventHandler
    public void onClick(@NotNull InventoryClickEvent e) {
        if (e.getClickedInventory().getType().equals(inv.getType())) e.setCancelled(true);
        if (e.getCurrentItem() == null) return;
        if (e.getCurrentItem().getType() == Material.AIR) return;

        Player clicker = (Player) e.getWhoClicked();

        var serverConfig = Objects.requireNonNull(main.getConfig().getConfigurationSection("servers")).getKeys(false);
        serverConfig.forEach(id -> {
            var server = main.getConfig().getConfigurationSection("servers." + id);
            String name = server.getString("name");
            Material icon = Material.valueOf(server.getString("icon"));
            int slot = server.getInt("slot");
            if (e.getCurrentItem().equals(Utils.createItemStack(icon, Utils.color(name)))) {
                if (main.getPpAPI().look(clicker.getUniqueId()) != 0) {
                    main.getManager().fromID(id).transferPoints((Player) e.getWhoClicked());
                } else {
                    clicker.playSound(clicker.getLocation(), "minecraft:entity.wolf.whine", 1, 1);
                    clicker.sendMessage(Utils.color("&6&lMINEVN &b&l&o> &cBạn không có Points để chuyển!"));
                }
            }
        });
    }

    @EventHandler
    public void onInventoryClick(@NotNull InventoryDragEvent e) {
        if (e.getInventory().getType().equals(inv.getType())) e.setCancelled(true);
    }
}