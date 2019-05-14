package sj224.mods.nether_dungeon;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = NetherMod.MODID, name = NetherMod.NAME, version = NetherMod.VERSION, acceptedMinecraftVersions = "[1.12,1.13)")
public class NetherMod
{
    public static final String MODID = "nether_dungeon";
    public static final String NAME = "Example Mod";
    public static final String VERSION = "1.0";
    
    public static int dungeonProbability;
    public static ResourceLocation lootTable;


    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        Configuration cfg=new Configuration(event.getSuggestedConfigurationFile());
        dungeonProbability=cfg.getInt("dungeon_probability","",275,1,1000,"Dungeon attempts per 1000 chunks");
        lootTable=new ResourceLocation(cfg.getString("loot_table","", "nether_dungeon:chests/nether_dungeon","Loot table used by nether dungeon chests"));
        LootTableList.register(lootTable);
        cfg.save();
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        GameRegistry.registerWorldGenerator(new WorldGen(),99);
    }
}
