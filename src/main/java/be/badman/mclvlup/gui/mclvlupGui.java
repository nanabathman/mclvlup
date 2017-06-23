package be.badman.mclvlup.gui;

import be.badman.mclvlup.capabilities.LvlUp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.inventory.GuiInventory;

import java.util.ArrayList;

/**
 * Created by Kevin on 22-6-2017.
 */
public class mclvlupGui extends Gui {
    public mclvlupGui(Minecraft mc, ArrayList<LvlUp> x)
    {

            mc.entityRenderer.setupOverlayRendering();
            int start = 200;
            for (LvlUp i : x) {
                String text = i.getName() + ": lvl " + i.getLvl() + " - experience " + i.getPoints();
                drawString(mc.fontRenderer, text, 5, start, Integer.parseInt("FFAA00", 16));
                start += 15;
            }
        }

    }
