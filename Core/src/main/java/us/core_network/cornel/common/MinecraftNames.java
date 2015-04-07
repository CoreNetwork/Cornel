package us.core_network.cornel.common;

import net.minecraft.server.v1_8_R2.Item;
import net.minecraft.server.v1_8_R2.MobEffectList;

/**
 * Created by Matej on 2.12.2014.
 */
public class MinecraftNames
{
    /**
     * Get enchantment ID from enchantment name.
     * List of possible combinations: <a href="http://minecraft.gamepedia.com/Data_values#Enchantment_IDs">Minecraft Wiki</a>.
     * @param name Enchantment name. (<code>minecraft:</code> prefix is optional)
     * @return ID of enchantment or <code>null</code> if enchantment with given name does not exist
     */
    public static Integer getEnchantmentId(String name)
    {
        net.minecraft.server.v1_8_R2.Enchantment nmsEnchantment = net.minecraft.server.v1_8_R2.Enchantment.getByName(name);
        if (nmsEnchantment == null)
            return null;

        return nmsEnchantment.id;
    }

    /**
     * Get material ID from material name.
     * List of possible combinations: <a href="http://minecraft.gamepedia.com/Data_values#Block_IDs">Minecraft Wiki</a>.
     * @param name Material name. (<code>minecraft:</code> prefix is optional)
     * @return ID of material or <code>null</code> if material with given name does not exist
     */
    public static Integer getMaterialId(String name)
    {
        Item item = Item.d(name);
        if (item == null)
            return null;

        return Item.getId(item);
    }

    /**
     * Get potion effect ID from potion effect Name.
     * List of possible combinations: <a href="http://minecraft.gamepedia.com/Data_values#Status_effects">Minecraft Wiki</a>.
     * @param name Potion effect name. (<code>minecraft:</code> prefix is optional)
     * @return ID of potion effect or <code>null</code> if potion effect with given name does not exist
     */
    public static Integer getPotionEffectId(String name)
    {
        MobEffectList effect = net.minecraft.server.v1_8_R2.MobEffectList.b(name);
        if (effect == null)
            return null;

        return effect.getId();
    }
}
