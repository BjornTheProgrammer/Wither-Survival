package net.zhendema.withersurvival;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Creature;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EnderDragonPart;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.world.EntitiesLoadEvent;

import java.util.stream.Collectors;

public class MobSpawnListener implements Listener {
    int monsterSpawnLimit;

    MobSpawnListener(int monsterSpawnLimit) {
        super();
        this.monsterSpawnLimit = monsterSpawnLimit;
    }

    public boolean is_allowed_to_exist(Entity entity) {
        if (entity instanceof Enderman && entity.getWorld().getEnvironment() == Environment.THE_END) return false;
        if (entity instanceof EnderDragon || entity instanceof EnderDragonPart) return true;
        if (entity instanceof Enderman) return true;
        if (entity instanceof Blaze) return true;
        if (entity instanceof Wither) return true;
        if (entity instanceof Player) return true;
        return false;
    }

    public void remove_boss_bar(Entity entity) {
        if (entity instanceof Wither) {
            Wither wither = (Wither) entity;
            wither.getBossBar().setVisible(false);
            return;
        }
    }

    public void spawnWither(CreatureSpawnEvent e) {
        World world = e.getEntity().getWorld();
        Location location = e.getLocation();

        if (world
                .getEntities()
                .stream()
                .filter(f -> f instanceof Monster)
                .collect(Collectors.toList())
                .size() < this.monsterSpawnLimit * 2
        ) world.spawnEntity(location, EntityType.WITHER);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onCreatureSpawn(CreatureSpawnEvent e) {
        Entity entity = e.getEntity();
        World world = e.getEntity().getWorld();

        remove_boss_bar(entity);
        if (entity instanceof Enderman) {
            if (world.getEnvironment() == Environment.THE_END) e.setCancelled(true);
            else if (Math.random() < 0.2) e.setCancelled(true);
            else spawnWither(e);
        }
        if (is_allowed_to_exist(entity)) return;

        spawnWither(e);

        e.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onEntitiesLoadEvent(EntitiesLoadEvent event) {
        for (Entity entity : event.getEntities()) {
            remove_boss_bar(entity);
        }
        event.getEntities().stream().filter(e -> !is_allowed_to_exist(e)).forEach(e -> e.remove());
    }
}
