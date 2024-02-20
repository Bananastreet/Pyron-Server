package io.xeros.content.bosses.nex;


import io.xeros.content.instances.InstanceConfiguration;
import io.xeros.content.instances.impl.LegacySoloPlayerInstance;
import io.xeros.model.entity.player.Boundary;
import io.xeros.model.entity.player.Player;
import io.xeros.model.entity.player.Position;

public class Nex extends LegacySoloPlayerInstance {

    public Nex(Player player, Boundary boundary) {
        super(InstanceConfiguration.CLOSE_ON_EMPTY, player, boundary);
    }

    public static void enter(Player player, Nex instance) {
        try {
            instance.add(player);
            NexNPC npc = new NexNPC(11278, new Position(2924, 5202, instance.getHeight()), instance);
            player.moveTo(new Position(2910, 5203, instance.getHeight()));
            player.getAttributes().setInt("nex_damage", 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDispose() {
        getPlayers().forEach(this::remove);
    }
}