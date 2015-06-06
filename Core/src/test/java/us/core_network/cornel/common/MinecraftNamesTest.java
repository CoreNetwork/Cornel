package us.core_network.cornel.common;

import net.minecraft.server.v1_8_R3.DispenserRegistry;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MinecraftNamesTest
{
    @Before
    public void initMinecraftBlocks()
    {
        DispenserRegistry.c(); //Initialize dispenser registry before accessing Blocks class (Minecraft code complains otherwise)
    }

    @Test
    public void testGetEnchantmentId() throws Exception
    {
        assertEquals(Enchantment.ARROW_DAMAGE.getId(), MinecraftNames.getEnchantmentId("power").intValue());
        assertNull(MinecraftNames.getEnchantmentId("invalid_name"));
    }

    @Test
    public void testGetMaterialId() throws Exception
    {
        assertEquals(Material.STONE.getId(), MinecraftNames.getMaterialId("stone").intValue());
        assertNull(MinecraftNames.getMaterialId("invalid_name"));
    }

    @Test
    public void testGetPotionEffectId() throws Exception
    {
        assertEquals(PotionEffectType.SPEED.getId(), MinecraftNames.getPotionEffectId("speed").intValue());
        assertNull(MinecraftNames.getPotionEffectId("invalid_name"));
    }
}