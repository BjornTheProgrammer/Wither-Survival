package net.zhendema.withersurvival;

import org.bukkit.Server;
import org.bukkit.entity.Player;

public class FakeTime implements Runnable {
    Server server;
    long playerTime;

    FakeTime(Server server) {
        super();
        this.server = server;
        this.playerTime = 0;
    }

    @Override
    public void run() {
        this.playerTime++;
        for (Player player : this.server.getOnlinePlayers()) {
            player.setPlayerTime(this.playerTime, false);
        }

        this.server.getWorlds().stream().forEach(world -> {
            world.setTime(15000);
        });
    }
}
