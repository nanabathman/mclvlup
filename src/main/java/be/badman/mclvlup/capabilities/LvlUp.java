package be.badman.mclvlup.capabilities;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;

/**
 * Created by Kevin on 21-6-2017.
 */
public class LvlUp implements IlvlUp {
    private String key;
    private String name;
    private EntityPlayer player;

    public LvlUp(String name, String key, EntityPlayer player) {
        this.name = name;
        this.key = key;
        this.player = player;
        if (!player.getEntityData().hasKey(key)) {
            player.getEntityData().setFloat(key, 0F);
        }else{
            player.getEntityData().setFloat(key,  player.getEntityData().getFloat(key));
        }
    }

    @Override
    public void add(float points) {
        player.getEntityData().setFloat(key, getPoints() + points);
    }

    @Override
    public void remove(float points) {
        player.getEntityData().setFloat(key, getPoints() - points);
    }

    @Override
    public float getLvl() {
        return (float) Math.floor(getPoints() / 1020);
    }

    @Override
    public float getPoints() {
        return player.getEntityData().getFloat(key);
    }

    @Override
    public void sayLvl() {
        player.sendMessage(new TextComponentString(ChatFormatting.RED +name + " level: " + Float.toString(getLvl())));
    }

    @Override
    public void sayPoints() {
        player.sendMessage(new TextComponentString(ChatFormatting.RED  +name + " experience: " + Float.toString(getPoints())));
    }
    @Override
    public String getName(){
        return name;
    }
    @Override
    public String getKey(){
        return key;
    }
}
