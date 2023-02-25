package net.minevn.pointstransfer;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;

public class Manager {
    private final PointsTransfer main;
    private final HashMap<String, Server> servers;

    public Manager(PointsTransfer main) {
        this.main = main;
        servers = new HashMap<>();
    }

    public void loadServer() {
        clear();
        int loaded = 0;
        var serverConfig = Objects.requireNonNull(main.getConfig().getConfigurationSection("servers")).getKeys(false);
        try {
            serverConfig.forEach(id -> {
                var server = main.getConfig().getConfigurationSection("servers." + id);
                String name = server.getString("name");
                String username = server.getString("username");
                String password = server.getString("password");
                String database = server.getString("database");
                addServer(id, new Server(name, username, password, database));
                Utils.log("Connected to " + database, Level.INFO);
            });
            loaded = serverConfig.size();
        } catch (Exception e) {
            Utils.log("Error while loading servers: " + e, Level.SEVERE);
        }
        Utils.log("Loaded " + loaded + " servers", Level.INFO);
    }

    public void loadSQL() {
        getServers().forEach((id, server) -> server.loadSQL());
    }

    public HashMap<String, Server> getServers() {
        return servers;
    }

    public void addServer(String name, Server server) {
        getServers().put(name, server);
    }

    public Server fromID(String id) {
        return getServers().get(id);
    }

    public void clear() {
        getServers().clear();
        getServers().forEach((id, server) -> {
            try {
                server.getConnection().close();
            } catch (SQLException e) {
                Utils.log("Error while closing connection: " + e, Level.SEVERE);
            }
        });
    }
}