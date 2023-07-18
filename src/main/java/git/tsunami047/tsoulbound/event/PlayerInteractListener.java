/*
 * ?2021 August-soft Corporation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package git.tsunami047.tsoulbound.event;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import git.tsunami047.tsoulbound.ConfigManager;
import git.tsunami047.tsoulbound.TSoulBound;
import git.tsunami047.tsoulbound.utils.ItemUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.*;

import static git.tsunami047.tsoulbound.event.AttrEquipmentUpdateListener.ANCHOR;
import static git.tsunami047.tsoulbound.utils.ItemUtil.needToHandle;

/**
 * @author ：Arisa
 * @date ：Created in 2020/3/1 19:14
 * @description：
 * @version: $
 */
public class PlayerInteractListener implements Listener {

    public static List<String> list;

    public static void load(){
        list = ConfigManager.basicConfig.getStringList("deny_left_click");
    }

    public static ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

//    public static Set<Block> getScopeBlocksByVector(Player player, Block target, int rangeX, int rangeY, int rangeZ) {
//        Set<Block> set = new HashSet<>();
//
//        // 玩家与准星上的方块之间的距离
//        Vector baseLocation = target.getLocation().add(0.5, 0.5, 0.5);
//        double baseX = baseLocation.distance(player.getEyeLocation());
//
//        int halfRangeX = rangeX / 2;
//        int halfRangeY = rangeY / 2;
//
//        // 获取相对坐标系
//        Vector relativeCoordinate = getRelativeCoordinate(player);
//        org.bukkit.Location eyeLocation = player.getEyeLocation();
//
//        // 宽
//        for (int x = -halfRangeX; x < rangeX - halfRangeX; x++) {
//            // 高
//            for (int y = -halfRangeY; y < rangeY - halfRangeY; y++) {
//                // 深度, 从被挖的方块往里
//                for (int z = 0; z < rangeZ; z++) {
//                    // 相对坐标转世界坐标并获取对应的方块
//                    Block block = getRelativeByCoordinate(eyeLocation, relativeCoordinate, (double) x, (double) y, baseX + (double) z).getBlock();
//                    set.add(block);
//                }
//            }
//        }
//
//        set.remove(target);
//        return set;
//    }
//
//    public static Location getRelativeByCoordinate(Location location, Vector[] coordinate, double x, double y, double z) {
//        Location result = location.clone();
//        result.add(coordinate[0].clone().multiply(x));
//        result.add(coordinate[1].clone().multiply(y));
//        result.add(coordinate[2].clone().multiply(z));
//        return result;
//    }
//
//    private static org.bukkit.Location getRelativeByCoordinate(org.bukkit.Location eyeLocation, Vector relativeCoordinate, double x, double y, double z) {
//        // 在这里实现根据相对坐标获取世界坐标的逻辑
//        // 返回对应的位置
//    }

    public static Entity getTargetEntity(Player player, double range) {
        Entity target = null;
        double closestDistanceSquared = range * range;

        for (Entity entity : player.getNearbyEntities(range, range, range)) {
            if (entity == player) {
                continue;
            }

            double distanceSquared = player.getLocation().distanceSquared(entity.getLocation());
            if (distanceSquared < closestDistanceSquared) {
                closestDistanceSquared = distanceSquared;
                target = entity;
            }
        }

        return target;
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            event.setExpToDrop(0);
        }
    }

    public static void restrictLeftClickOnCertainItems() {
        PacketAdapter adapter = new PacketAdapter(TSoulBound.plugin, ListenerPriority.NORMAL, PacketType.Play.Client.USE_ENTITY) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player player = event.getPlayer();
                EnumWrappers.EntityUseAction action = packet.getEntityUseActions().read(0);
                if (action == EnumWrappers.EntityUseAction.ATTACK) {
                    if (player.isOp()) {
                        return;
                    }
                    ItemStack itemStack = player.getItemInHand();
                    if (!ItemUtil.needToHandle(itemStack)) {
                        return;
                    }
                    List<String> lore = itemStack.getItemMeta().getLore();
                    for (String value : lore) {
                        if (!value.contains(ANCHOR)) continue;
                        String s = value.replaceAll("§.", "");
                        String[] attr = s.split("·");
                        if (list.contains(attr[1])) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        };
        protocolManager.addPacketListener(adapter);

    }

    public void whenEntityDamageByEntities(EntityDamageByEntityEvent e) {
        if(!(e.getDamager() instanceof Player)){
            return;
        }
        Player player = (Player)e.getDamager();
        if (player.isOp()) {
            return;
        }
        ItemStack itemStack = player.getItemInHand();
        if (!ItemUtil.needToHandle(itemStack)) {
            return;
        }
        List<String> lore = itemStack.getItemMeta().getLore();
        for (String value : lore) {
            if (!value.contains(ANCHOR)) continue;
            String s = value.replaceAll("§.", "");
            String[] attr = s.split("·");
            if (list.contains(attr[1])) {
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void whenPlayerInteract(PlayerInteractEvent e) {
        if(!ConfigBean.not_allow_other_interact){
            return;
        }
        Player player = e.getPlayer();
        if (player.isOp()) {
            return;
        }
        ItemStack item = e.getItem();
        if(!needToHandle(item)){
            return;
        }
        int itemStackOwner = ItemUtil.isItemStackOwner(player.getName(), item);
        if(itemStackOwner==-1){
            return;
        }
        if (itemStackOwner==0) {
            ConfigBean.sendMessage(player,"interact_cancelled");
            e.setCancelled(true);
        }
    }
}
