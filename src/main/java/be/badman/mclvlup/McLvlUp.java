package be.badman.mclvlup;

import be.badman.mclvlup.events.blockBreakHandler;
import be.badman.mclvlup.events.mcLvlUpEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

@Mod(modid = McLvlUp.MODID, version = McLvlUp.VERSION, name = McLvlUp.NAME)
public class McLvlUp {
    public static final String MODID = "mclvlup";
    public static final String VERSION = "1.2";
    public static final String NAME = "MC Level Up";
    @EventHandler
    public void init(FMLInitializationEvent event)
    {

    }
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {

        MinecraftForge.EVENT_BUS.register(new mcLvlUpEvent());

    }
}
