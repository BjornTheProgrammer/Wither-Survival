package net.zhendema.withersurvival;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

public class ClearFarAwayEntities implements Runnable {
    Server server;

    ClearFarAwayEntities(Server server) {
        super();
        this.server = server;
    }

    @Override
    public void run() {
        Set<Entity> entitiesToNotRemove = new HashSet<Entity>();
        for (Player player : this.server.getOnlinePlayers()) {
            Location playerLocation = player.getLocation();
            entitiesToNotRemove.addAll(player
                .getWorld()
                .getEntities()
                .stream()
                .filter(e -> playerLocation.distance(e.getLocation()) <= 50.0 || !(e instanceof Monster))
                .collect(Collectors.toSet()));
        }

        this.server.getWorlds().stream().forEach(world -> {
            world.getEntities().stream().forEach(e -> {
                if (!entitiesToNotRemove.contains(e)) e.remove();
            });
        });
    }
}
