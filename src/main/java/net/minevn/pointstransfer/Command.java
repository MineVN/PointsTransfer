package net.minevn.pointstransfer;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Command implements CommandExecutor {
    private final PointsTransfer main;

    public Command(PointsTransfer main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, org.bukkit.command.@NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (player.hasPermission("pointstransfer.use")) {
                main.getGui().open(player);
            } else {
                player.sendMessage(Utils.color("&6&lMINEVN &b&l&o> &cBạn không có quyền để sử dụng lệnh này!"));
            }
        }

        return true;
    }
}