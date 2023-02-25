package net.minevn.pointstransfer;

import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class PointsTransfer extends JavaPlugin {
    private static PointsTransfer instance;
    private PlayerPointsAPI ppAPI;
    private Manager manager;
    private GUI gui;

    public static PointsTransfer getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.ppAPI = PlayerPoints.getInstance().getAPI();
        getServer().getPluginManager().registerEvents(new GUI(this), this);
        Objects.requireNonNull(getCommand("chuyenpoints")).setExecutor(new Command(this));

        manager = new Manager(this);
        gui = new GUI(this);

        getManager().loadServer();
        getManager().loadSQL();
    }

    @Override
    public void onDisable() {
        getManager().clear();
    }

    public PlayerPointsAPI getPpAPI() {
        return ppAPI;
    }

    public Manager getManager() {
        return manager;
    }

    public GUI getGui() {
        return gui;
    }
}