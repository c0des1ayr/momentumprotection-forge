package com.shutdoor.momentumprotection;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.shutdoor.momentumprotection.MomentumProtection.MODID;

@Mod.EventBusSubscriber(modid = MODID)
public class EnchantMomentumProtection extends Enchantment {
    public EnchantMomentumProtection() {
        super(Rarity.VERY_RARE, EnchantmentType.ARMOR_CHEST, new EquipmentSlotType[]{
                EquipmentSlotType.CHEST
        });
    }

    @Override
    public int getMinCost(int level) {
        switch (level) {
            case 5:
                return 48;
            default:
                return 20;
        }
    }

    @Override
    public int getMaxCost(int level) {
        return (getMinCost(level) * level);
    }

    @Override
    public int getMaxLevel() {return 5;}


    @Override
    public boolean canEnchant(ItemStack p_44689_){
        return (p_44689_.getItem() instanceof ElytraItem);
    }

    @SubscribeEvent
    public static void onPlayerDamage(LivingDamageEvent damageEvent){
        if(damageEvent.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) damageEvent.getEntity();
            ItemStack chestPlate = player.getItemBySlot(EquipmentSlotType.CHEST);
            if(ElytraItem.isFlyEnabled(chestPlate)) {
                int enchantLvl = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentReg.MOMENTUM_PROTECTION.get(), chestPlate);
                if(enchantLvl > 0) {
                    if (damageEvent.getSource() == DamageSource.FLY_INTO_WALL) {
                        switch (enchantLvl) {
                            case 5: //if you have a level 5 elytra
                                damageEvent.setAmount(0F);
                                damageEvent.setCanceled(true); //negate all damage
                                break;
                            default:
                                damageEvent.setAmount((damageEvent.getAmount() * (0.95F / enchantLvl)));
                        }
                    }
                }
            }

        }
    }
}
