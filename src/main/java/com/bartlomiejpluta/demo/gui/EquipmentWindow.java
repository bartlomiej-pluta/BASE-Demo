package com.bartlomiejpluta.demo.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.*;
import com.bartlomiejpluta.base.lib.gui.Label;
import com.bartlomiejpluta.base.lib.gui.VGridOptionChoice;
import com.bartlomiejpluta.base.lib.gui.VOptionChoice;
import com.bartlomiejpluta.demo.entity.Player;
import com.bartlomiejpluta.demo.runner.DemoRunner;
import com.bartlomiejpluta.demo.world.item.Item;
import com.bartlomiejpluta.demo.world.weapon.MeleeWeapon;
import com.bartlomiejpluta.demo.world.weapon.RangedWeapon;
import com.bartlomiejpluta.demo.world.weapon.Weapon;

import java.util.Map;
import java.util.function.Consumer;

import static java.lang.String.format;


public class EquipmentWindow extends DecoratedWindow {
   private final DemoRunner runner;
   private final Player player;

   private final Window eqItemMenuWindow;
   private final Button useBtn;
   private final Button dropBtn;
   private final Button cancelBtn;
   private final VOptionChoice eqItemMenu;

   @Ref("eq")
   private VGridOptionChoice eqGrid;

   @Ref("item-name")
   private Label nameLbl;

   @Ref("item-details")
   private Label detailsLbl;

   public EquipmentWindow(Context context, GUI gui, Map<String, Component> refs) {
      super(context, gui, refs);
      this.runner = (DemoRunner) context.getGameRunner();
      this.player = runner.getPlayer();
      this.eqItemMenuWindow = gui.inflateWindow(A.widgets.eq_item_menu.uid);
      this.eqItemMenu = eqItemMenuWindow.reference("menu", VOptionChoice.class);
      this.useBtn = eqItemMenuWindow.reference("use", Button.class);
      this.dropBtn = eqItemMenuWindow.reference("drop", Button.class);
      this.cancelBtn = eqItemMenuWindow.reference("cancel", Button.class);
   }

   @Override
   public void onOpen(WindowManager manager) {
      super.onOpen(manager);

      cancelBtn.setAction(manager::close);
      eqGrid.setOnSelect(this::updateItemDetails);

      var i = 0;
      for (var child : eqGrid.getChildren()) {
         var slot = (ItemIconView) child;
         slot.setItem(player.getEquipmentItem(i++));
         slot.setAction(handleItem(slot));
      }

      eqGrid.select(0, 0);
      eqGrid.focus();
   }

   private Consumer<Item> handleItem(ItemIconView slot) {
      return item -> {
         useBtn.setText(getButtonTitle(item));
         eqItemMenu.select(0);
         eqItemMenu.focus();

         manager.open(eqItemMenuWindow);

         useBtn.setAction(() -> {
            player.useEquipmentItem(item);
            manager.close();
         });

         dropBtn.setAction(() -> {
            player.dropItemFromEquipment(item);
            slot.setItem(null);
            updateItemDetails(slot);
            manager.close();
         });
      };
   }

   private void updateItemDetails(Component slot) {
      var item = ((ItemIconView) slot).getItem();

      if (item == null) {
         nameLbl.setText("");
         detailsLbl.setText("");
         return;
      }

      nameLbl.setText(item.getName());

      if (item instanceof MeleeWeapon weapon) {
         detailsLbl.setText(format("Damage: %s\nCooldown: %s\n", weapon.getDmgRoller(), weapon.getCooldown()));
         return;
      }

      if (item instanceof RangedWeapon weapon) {
         detailsLbl.setText(format("Damage: %s\nRange: %s\nCooldown: %s\n", weapon.getDmgRoller(), weapon.getRangeRoller(), weapon.getCooldown()));
      }
   }

   private String getButtonTitle(Item item) {
      if (item instanceof Weapon) {
         return "Equip";
      }

      return "Use";
   }
}