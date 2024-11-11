package net.zhendema.withersurvival;

import org.bukkit.Server;
import org.bukkit.entity.SpawnCategory;
import org.bukkit.plugin.java.JavaPlugin;

public final class WitherSurvival extends JavaPlugin {
    @Override
    public void onEnable() {
        Server server = this.getServer();
        int monsterSpawnLimit = server.getSpawnLimit(SpawnCategory.MONSTER);
        // Plugin startup logic
        server.getPluginManager().registerEvents(new MobSpawnListener(monsterSpawnLimit), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
