package com.bartlomiejpluta.demo.gui;

import com.bartlomiejpluta.base.api.context.Context;
import com.bartlomiejpluta.base.api.gui.*;
import com.bartlomiejpluta.base.api.input.Key;
import com.bartlomiejpluta.base.api.input.KeyAction;
import com.bartlomiejpluta.base.api.input.KeyEvent;
import com.bartlomiejpluta.base.lib.gui.HOptionChoice;
import com.bartlomiejpluta.base.lib.gui.Label;
import com.bartlomiejpluta.base.lib.gui.VGridOptionChoice;
import com.bartlomiejpluta.base.lib.gui.VOptionChoice;
import com.bartlomiejpluta.demo.entity.Player;
import com.bartlomiejpluta.demo.runner.DemoRunner;
import com.bartlomiejpluta.demo.world.item.Item;
import com.bartlomiejpluta.demo.world.item.Useable;
import com.bartlomiejpluta.demo.world.potion.Medicament;
import com.bartlomiejpluta.demo.world.weapon.MeleeWeapon;
import com.bartlomiejpluta.demo.world.weapon.RangedWeapon;
import com.bartlomiejpluta.demo.world.weapon.ThrowingWeapon;

import java.util.Map;
import java.util.function.BiConsumer;

import static java.lang.String.format;


public class EquipmentWindow extends DecoratedWindow {
   private final Player player;

   private final Window popupMenuWindow;
   private final Button useBtn;
   private final Button dropBtn;
   private final Button cancelBtn;
   private final VOptionChoice popupMenu;

   @Ref("layout")
   private HOptionChoice layout;

   @Ref("equipment")
   private VGridOptionChoice equipment;

   @Ref("inventory")
   private VGridOptionChoice inventory;

   @Ref("weapon")
   private ItemIconView weaponSlot;

   @Ref("ammo")
   private ItemIconView ammoSlot;

   @Ref("item-name")
   private Label nameLbl;

   @Ref("item-details")
   private Label detailsLbl;


   public EquipmentWindow(Context context, GUI gui, Map<String, Component> refs) {
      super(context, gui, refs);
      this.player = DemoRunner.instance().getPlayer();
      this.popupMenuWindow = gui.inflateWindow(A.widgets.eq_item_menu.uid);
      this.popupMenu = popupMenuWindow.reference("menu", VOptionChoice.class);
      this.useBtn = popupMenuWindow.reference("use", Button.class);
      this.dropBtn = popupMenuWindow.reference("drop", Button.class);
      this.cancelBtn = popupMenuWindow.reference("cancel", Button.class);

      addEventListener(KeyEvent.TYPE, this::handleKey);
   }

   private void handleKey(KeyEvent event) {
      if (event.getKey() == Key.KEY_TAB && event.getAction() == KeyAction.PRESS) {
         layout.selectNext();
         layout.focus();
         updateItemDetails(((VGridOptionChoice) layout.getSelectedComponent()).getSelectedComponent());
         event.consume();
      }
   }

   @Override
   public void onOpen(WindowManager manager, Object... args) {
      super.onOpen(manager, args);

      cancelBtn.setAction(manager::close);
      inventory.setOnSelect(this::updateItemDetails);
      equipment.setOnSelect(this::updateItemDetails);

      updateEquipment();

      layout.select(1);
      layout.focus();
   }

   @Override
   public void onClose(WindowManager manager) {
      super.onClose(manager);
      layout.blur();
   }

   private void updateEquipment() {
      var i = 0;
      for (var child : inventory.getChildren()) {
         var slot = (ItemIconView) child;
         slot.setItem(player.getEquipmentItem(i++));
         slot.setAction(this::handleInventoryClick);
      }

      weaponSlot.setItem(player.getWeapon());
      weaponSlot.setAction(handleEquipmentClick(() -> {
         player.setWeapon(null);
         updateEquipment();
         manager.close();
      }));

      ammoSlot.setItem(player.getAmmunition());
      ammoSlot.setAction(handleEquipmentClick(() -> {
         player.setAmmunition(null);
         updateEquipment();
         manager.close();
      }));
   }

   private BiConsumer<ItemIconView, Item> handleEquipmentClick(Runnable deequip) {
      return (slot, item) -> {
         useBtn.setText("Disarm");
         popupMenu.select(0);
         popupMenu.focus();

         manager.open(popupMenuWindow);

         useBtn.setAction(deequip);

         dropBtn.setAction(() -> {
            player.dropItemFromEquipment(item);
            slot.setItem(null);
            updateItemDetails(slot);
            manager.close();
         });
      };
   }

   private void handleInventoryClick(ItemIconView slot, Item item) {
      useBtn.setText(getButtonTitle(item));
      popupMenu.select(0);
      popupMenu.focus();

      manager.open(popupMenuWindow);

      if (item instanceof Useable useable) {
         useBtn.setAction(() -> {
            useable.use(player);
            updateEquipment();
            manager.close();
         });
      }

      dropBtn.setAction(() -> {
         player.dropItemFromEquipment(item);
         slot.setItem(null);
         updateItemDetails(slot);
         manager.close();
      });
   }

   private void updateItemDetails(Component slot) {
      var item = ((ItemIconView) slot).getItem();

      if (item == null) {
         nameLbl.setText("");
         detailsLbl.setText("");
         return;
      }

      nameLbl.setText(item.getName());
      detailsLbl.setText("");

      if (item instanceof MeleeWeapon weapon) {
         detailsLbl.setText(format("Damage: %s\nCooldown: %s\n", weapon.getDmgRoller(), weapon.getCooldown()));
         return;
      }

      if (item instanceof RangedWeapon weapon) {
         detailsLbl.setText(format("Damage: %s\nRange: %s\nCooldown: %s\n", weapon.getDmgRoller(), weapon.getRangeRoller(), weapon.getCooldown()));
         return;
      }

      if (item instanceof ThrowingWeapon weapon) {
         detailsLbl.setText(format("Damage: %s\nRange: %s\nCooldown: %s\n", weapon.getDmgRoller(), weapon.getRangeRoller(), weapon.getCooldown()));
      }

      if (item instanceof Medicament medicament) {
         detailsLbl.setText(format("Restores: %s HP\n", medicament.getRoller()));
      }
   }

   private String getButtonTitle(Item item) {
      if (item instanceof Useable useable) {
         return useable.usageName();
      }

      return "Use";
   }
}