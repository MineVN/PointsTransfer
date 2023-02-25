package net.minevn.pointstransfer;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;

public class Server {
    private final PointsTransfer main = PointsTransfer.getInstance();
    private final String name, username, password, database;
    private boolean isSQLConnected;

    private Connection connection;
    private PreparedStatement preparedStatement;

    public Server(String name, String username, String password, String database) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.database = database;
    }

    public void loadSQL() {
        Utils.runAsync(() -> {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connection = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database, username, password);
                setSQLConnected(true);
            } catch (SQLException | ClassNotFoundException e) {
                Utils.log("Error while loading MySQL: " + e, Level.SEVERE);
                setSQLConnected(false);
            }
        });
    }

    public void transferPoints(@NotNull Player player) {
        int currentAmount = main.getPpAPI().look(player.getUniqueId());
        Utils.runAsync(() -> {
            try {
                player.sendMessage(Utils.color("&6&lMINEVN &b&l&o> &eĐang xử lý, xin vui lòng chờ..."));
                if (isSQLConnected()) {
                    preparedStatement = connection.prepareStatement("UPDATE " + getDatabase() + ".playerpoints_points SET points = points + ? WHERE uuid = ?");
                    preparedStatement.setInt(1, currentAmount);
                    preparedStatement.setString(2, player.getUniqueId().toString());
                    preparedStatement.executeUpdate();
                    main.getPpAPI().set(player.getUniqueId(), 0);
                    player.playSound(player.getLocation(), "minecraft:entity.player.levelup", 1, 1);
                    player.sendMessage(Utils.color("&6&lMINEVN &b&l&o> &aĐã chuyển thành công &b&l" + currentAmount + " Points &asang " + getName()));
                    player.sendMessage(Utils.color("&6&lMINEVN &b&l&o> &fThời gian: &e" + Utils.getDate()));
                } else {
                    player.playSound(player.getLocation(), "minecraft:entity.wolf.whine", 1, 1);
                    player.sendMessage(Utils.color("&6&lMINEVN &b&l&o> &cĐã có lỗi xảy ra, vui lòng báo cho Admin!"));
                    player.sendMessage(Utils.color("&6&lMINEVN &b&l&o> &fThời gian: &e" + Utils.getDate()));
                    Utils.log("Error while transferring points: SQL is not connected", Level.SEVERE);
                }
            } catch (SQLException e) {
                player.playSound(player.getLocation(), "minecraft:entity.wolf.whine", 1, 1);
                player.sendMessage(Utils.color("&6&lMINEVN &b&l&o> &cĐã có lỗi xảy ra, vui lòng báo cho Admin!"));
                player.sendMessage(Utils.color("&6&lMINEVN &b&l&o> &fThời gian: &e" + Utils.getDate()));
                Utils.log("Error while transferring points: " + e, Level.SEVERE);
            }
        });
    }

    public Connection getConnection() {
        return connection;
    }

    public String getName() {
        return Utils.color(name);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabase() {
        return database;
    }

    public boolean isSQLConnected() {
        return isSQLConnected;
    }

    public void setSQLConnected(boolean sqlConnected) {
        isSQLConnected = sqlConnected;
    }
}