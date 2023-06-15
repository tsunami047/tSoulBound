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

import git.tsunami047.tsoulbound.utils.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

import static git.tsunami047.tsoulbound.utils.ItemUtil.needToHandle;

/**
 * @author ：Arisa
 * @date ：Created in 2020/3/1 18:48
 * @description：
 * @version: $
 */
public class PlayerDropItemListener implements Listener {
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void whenPlayerDropItem(PlayerDropItemEvent e) {
        if(!ConfigBean.not_allow_throw_out_owner_item){
            return;
        }
        Player player = e.getPlayer();
        if (player.isOp()) {
            return;
        }
        ItemStack item = e.getItemDrop().getItemStack();
        if(!needToHandle(item)){
            return;
        }
        if(ItemUtil.isItemLoreHasKey(item,ConfigBean.bind_lore_key)){
            ItemUtil.updateItemStack(item, ConfigBean.bind_lore_key,ConfigBean.bound_lore,player.getName());
        }
        int itemStackOwner = ItemUtil.isItemStackOwner(player.getName(), item, ConfigBean.bound_key);
        if(itemStackOwner==-1){
            return;
        }

        if (itemStackOwner==1) {
            ConfigBean.sendMessage(player,"drop_item_cancelled");
            e.setCancelled(true);
        }
    }
    
}
