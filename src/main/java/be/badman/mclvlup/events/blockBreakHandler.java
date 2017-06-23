package be.badman.mclvlup.events;

import be.badman.mclvlup.capabilities.LvlUp;
import be.badman.mclvlup.gui.mclvlupGui;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import scala.util.Random;

import java.util.ArrayList;

/**
 * Created by Kevin on 19-6-2017.
 */
public class blockBreakHandler {

    private ArrayList<String> A = new ArrayList<>();
    private EntityPlayer player;
    private LvlUp Mining;
    private LvlUp Excavation;
    private LvlUp Woodcutting;
    private LvlUp Herbalism;
    private LvlUp Fishing;
    private LvlUp Unarmed;
    private Random rand = new Random();
    private Long rightClickTime = 0L;
    private Long ActivationMiningTime = 0L;
    private ArrayList<LvlUp> lvlups = new ArrayList<>();


    public void updateLvlups() {
        lvlups.clear();
        lvlups.add(Mining);
        lvlups.add(Excavation);
        lvlups.add(Woodcutting);
        lvlups.add(Herbalism);
        lvlups.add(Fishing);
        lvlups.add(Unarmed);
    }


    @SubscribeEvent
    public void potionDurration(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            if (event.getEntityLiving().isPotionActive(Potion.getPotionById(3))) {
                if (event.getEntityLiving().getActivePotionEffect(Potion.getPotionById(3)).getDuration() == 0) {
                    event.getEntityLiving().removePotionEffect(Potion.getPotionById(3));
                }
            }
        }
    }

    @SubscribeEvent
    public void onJoin(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) {

        event.player.sendMessage(new TextComponentString(ChatFormatting.RED + "Joined"));
        Mining = new LvlUp("Mining", "mclvlupMining", (EntityPlayer) event.player);
        Excavation = new LvlUp("Excavation", "mclvlupExcavation", (EntityPlayer) event.player);
        Woodcutting = new LvlUp("Woodcutting", "mclvlupWoodcutting", (EntityPlayer) event.player);
        Herbalism = new LvlUp("Herbalism", "mclvlupHerbalism", (EntityPlayer) event.player);
        Fishing = new LvlUp("Fishing", "mclvlupFishing", (EntityPlayer) event.player);
        Unarmed = new LvlUp("Unarmed", "mclvlupUnarmed", (EntityPlayer) event.player);
        updateLvlups();
    }

    @SubscribeEvent
    public void respawn(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent event) {
        if (player != null) {
            for (LvlUp l : lvlups) {
                event.player.getEntityData().setFloat(l.getKey(), player.getEntityData().getFloat(l.getKey()));
            }
            for (String x : A) {
                event.player.getEntityData().setBoolean(x, true);
            }

        }
    }

    @SubscribeEvent
    public void deathEvent(LivingDeathEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            player = (EntityPlayer) event.getEntity();
        }else{
            if(event.getSource().getTrueSource() instanceof EntityPlayer){
               Unarmed.add(5);
               if(event.getEntity() instanceof EntityIronGolem){
                   Unarmed.add(20);
               }
               if(event.getEntity() instanceof net.minecraft.entity.monster.IMob){
                  Unarmed.add(45);
               }
            }
        }
    }

    @SubscribeEvent
    public void blockPlaceEvent(BlockEvent.PlaceEvent event) {
        Block placed = event.getPlacedBlock().getBlock();
        if (placed == Blocks.STONE || placed == Blocks.NETHERRACK || placed == Blocks.MOSSY_COBBLESTONE || placed == Blocks.SANDSTONE || placed == Blocks.PACKED_ICE || placed == Blocks.STAINED_HARDENED_CLAY || placed == Blocks.PRISMARINE || placed == Blocks.RED_SANDSTONE || placed == Blocks.END_STONE || placed == Blocks.OBSIDIAN || placed == Blocks.IRON_ORE || placed == Blocks.GOLD_ORE) {
            event.getPlayer().getEntityData().setBoolean(event.getPos().toString(), true);
            A.add(event.getPos().toString());
        }


    }

    @SubscribeEvent
    public void catchFish(ItemFishedEvent event) {
        Fishing.add(15);
        if (Fishing.getLvl() > rand.nextInt(250)) {

            event.getEntityPlayer().getEntityWorld().spawnEntity(new EntityItem(event.getEntityPlayer().getEntityWorld(), event.getEntityPlayer().getPosition().getX(), event.getEntityPlayer().getPosition().getY(), event.getEntityPlayer().getPosition().getZ(), new ItemStack(event.getDrops().get(0).getItem())));
        }
    }

    @SubscribeEvent
    public void activateE(PlayerInteractEvent.RightClickBlock event) throws InterruptedException {
        updateLvlups();
        Item held = Minecraft.getMinecraft().player.getHeldItemMainhand().getItem();
        if (held == Items.WHEAT_SEEDS) {
            if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.COBBLESTONE) {
                event.getItemStack().setCount(event.getItemStack().getCount() - 1);
                if (Herbalism.getLvl() > rand.nextInt(250)) {
                    event.getWorld().setBlockState(event.getPos(), Blocks.MOSSY_COBBLESTONE.getDefaultState());
                    event.getEntityPlayer().sendMessage(new TextComponentString(ChatFormatting.GREEN + "McLvlUp: Green Thumb Luck!"));
                }
            }
            if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.DIRT) {
                event.getItemStack().setCount(event.getItemStack().getCount() - 1);
                if (Herbalism.getLvl() > rand.nextInt(250)) {
                    event.getWorld().setBlockState(event.getPos(), Blocks.GRASS.getDefaultState());
                    event.getEntityPlayer().sendMessage(new TextComponentString(ChatFormatting.GREEN + "McLvlUp: Green Thumb Luck!"));
                }
            }
        }
        Item subHand = Minecraft.getMinecraft().player.getHeldItemOffhand().getItem();

        if (held == Items.DIAMOND_PICKAXE || held == Items.WOODEN_PICKAXE || held == Items.STONE_PICKAXE || held == Items.IRON_PICKAXE || held == Items.GOLDEN_PICKAXE) {
            if (event.getWorld().getTotalWorldTime() - rightClickTime > 100) {
                if (subHand == Items.COAL) {
                    if (held == Items.DIAMOND_PICKAXE || held == Items.WOODEN_PICKAXE || held == Items.STONE_PICKAXE || held == Items.IRON_PICKAXE || held == Items.GOLDEN_PICKAXE && subHand == Items.COAL) {
                        if (event.getWorld().getTotalWorldTime() - ActivationMiningTime > 6000) {
                            ActivationMiningTime = event.getWorld().getTotalWorldTime();
                            event.getEntityPlayer().sendMessage(new TextComponentString(ChatFormatting.BLUE + "Activated: " + Mining.getName() + " ability."));
                            event.getEntityPlayer().addPotionEffect(new PotionEffect(Potion.getPotionById(3), (int) (Mining.getLvl() * 20), 15));
                        } else {
                            if (event.getWorld().getTotalWorldTime() - rightClickTime > 100) {
                                event.getEntityPlayer().sendMessage(new TextComponentString(ChatFormatting.BLUE + "Wait: " + (6000 - (event.getWorld().getTotalWorldTime() - ActivationMiningTime)) + " ticks."));
                            }
                        }
                    }
                }
                if (subHand == Items.AIR) {


                    Mining.sayLvl();
                    Mining.sayPoints();

                }
                rightClickTime = event.getWorld().getTotalWorldTime();
            }
        }

        if (held == Items.DIAMOND_SHOVEL || held == Items.WOODEN_SHOVEL || held == Items.STONE_SHOVEL || held == Items.IRON_SHOVEL || held == Items.GOLDEN_SHOVEL) {
            if (event.getWorld().getTotalWorldTime() - rightClickTime > 100) {

                Excavation.sayLvl();
                Excavation.sayPoints();
                rightClickTime = event.getWorld().getTotalWorldTime();
            }
        }

        if (held == Items.DIAMOND_AXE || held == Items.WOODEN_AXE || held == Items.STONE_AXE || held == Items.IRON_AXE || held == Items.GOLDEN_AXE) {
            if (event.getWorld().getTotalWorldTime() - rightClickTime > 100) {

                Woodcutting.sayLvl();
                Woodcutting.sayPoints();
                rightClickTime = event.getWorld().getTotalWorldTime();
            }
        }
    }

    @SubscribeEvent
    public void breakEvent(BlockEvent.BreakEvent event) {
        if (!event.getPlayer().getEntityData().hasKey(event.getPos().toString())) {
            Block e = event.getState().getBlock();
            Item held = event.getPlayer().getHeldItemMainhand().getItem();
            if (held == Items.DIAMOND_PICKAXE || held == Items.WOODEN_PICKAXE || held == Items.STONE_PICKAXE || held == Items.IRON_PICKAXE || held == Items.GOLDEN_PICKAXE) {
                Mining = new LvlUp("Mining", "mclvlupMining", event.getPlayer());
                float miningLvl = Mining.getLvl();
                if (e == Blocks.HARDENED_CLAY || e == Blocks.MOSSY_COBBLESTONE || e == Blocks.NETHERRACK || e == Blocks.SANDSTONE || e == Blocks.STONE) {
                    Mining.add(3);
                    doubleDrop(event, Mining);
                }
                if (e == Blocks.PACKED_ICE || e == Blocks.STAINED_HARDENED_CLAY) {
                    Mining.add(5);
                    doubleDrop(event, Mining);
                }
                if (e == Blocks.COAL_ORE || e == Blocks.RED_SANDSTONE || e == Blocks.QUARTZ_ORE) {
                    Mining.add(10);
                    doubleDrop(event, Mining);
                }
                if (e == Blocks.REDSTONE_ORE || e == Blocks.END_STONE || e == Blocks.OBSIDIAN) {
                    Mining.add(15);
                    doubleDrop(event, Mining);
                }
                if (e == Blocks.IRON_ORE || e == Blocks.GOLD_ORE) {
                    Mining.add(25);
                    doubleDrop(event, Mining);
                }
                if (e == Blocks.LAPIS_ORE) {
                    Mining.add(40);
                    doubleDrop(event, Mining);
                }
                if (e == Blocks.DIAMOND_ORE) {
                    Mining.add(75);
                    doubleDrop(event, Mining);
                }
                if (e == Blocks.EMERALD_ORE) {
                    Mining.add(100);
                    doubleDrop(event, Mining);
                }
                if (miningLvl < Mining.getLvl()) {
                    Mining.sayLvl();
                }
            }
            if (e == Blocks.DIRT || e == Blocks.SAND || e == Blocks.GRASS || e == Blocks.CLAY || e == Blocks.GRAVEL || e == Blocks.SOUL_SAND || e == Blocks.MYCELIUM) {
                Excavation = new LvlUp("Excavation", "mclvlupExcavation", event.getPlayer());

                float excLVL = Excavation.getLvl();
                Excavation.add(8);
                doubleDrop(event, Excavation);
                if (Excavation.getLvl() > 10) {
                    if (5 == rand.nextInt(20)) {
                        Excavation.add(8);
                        event.getWorld().spawnEntity(new EntityItem(event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), new ItemStack(Items.GLOWSTONE_DUST)));
                    }
                }
                if (Excavation.getLvl() > 35) {
                    if (225 == rand.nextInt(1000)) {
                        Excavation.add(8);
                        event.getWorld().spawnEntity(new EntityItem(event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), new ItemStack(Items.DIAMOND)));
                        event.getWorld().spawnEntity(new EntityItem(event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), new ItemStack(Items.DIAMOND)));
                    }
                }
                if (e == Blocks.GRAVEL) {
                    if (Excavation.getLvl() > 35) {
                        if (5 == rand.nextInt(10)) {
                            event.getWorld().spawnEntity(new EntityItem(event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), new ItemStack(Items.GUNPOWDER)));
                            Excavation.add(5);

                        }
                    }
                    if (Excavation.getLvl() > 75) {
                        if (5 == rand.nextInt(10)) {
                            event.getWorld().spawnEntity(new EntityItem(event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), new ItemStack(Items.BONE)));
                            Excavation.add(5);

                        }
                    }
                }
                if (e == Blocks.CLAY) {
                    if (Excavation.getLvl() > 100) {
                        if (5 == rand.nextInt(10)) {
                            event.getWorld().spawnEntity(new EntityItem(event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), new ItemStack(Items.BUCKET)));
                            Excavation.add(5);

                        }
                    }
                    if (Excavation.getLvl() > 35) {
                        if (5 == rand.nextInt(10)) {
                            event.getWorld().spawnEntity(new EntityItem(event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), new ItemStack(Items.SLIME_BALL)));
                            Excavation.add(5);

                        }
                    }
                }
                if (excLVL < Excavation.getLvl()) {
                    Excavation.sayLvl();
                }
            }

            if (e == Blocks.LOG || e == Blocks.LOG2 || e == Blocks.BROWN_MUSHROOM_BLOCK || e == Blocks.RED_MUSHROOM_BLOCK) {
                Woodcutting = new LvlUp("Woodcutting", "mclvlupWoodcutting", event.getPlayer());

                float woodLVL = Woodcutting.getLvl();
                Woodcutting.add(10);
                doubleDrop(event, Woodcutting);
                if (woodLVL < Woodcutting.getLvl()) {
                    Woodcutting.sayLvl();
                }
            }
            if (e == Blocks.LEAVES || e == Blocks.LEAVES2) {
                Woodcutting = new LvlUp("Woodcutting", "mclvlupWoodcutting", event.getPlayer());

                float woodLVL = Woodcutting.getLvl();
                Woodcutting.add(1);
                if (Woodcutting.getLvl() > rand.nextInt(150)) {

                    event.getWorld().spawnEntity(new EntityItem(event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), new ItemStack(Items.APPLE)));
                }
                if (woodLVL < Woodcutting.getLvl()) {
                    Woodcutting.sayLvl();
                }
            }
            if (e == Blocks.WHEAT || e == Blocks.CARROTS || e == Blocks.POTATOES || e == Blocks.NETHER_WART) {
                BlockCrops ts = (BlockCrops) e;
                if (ts.isMaxAge(event.getState())) {
                    Herbalism.add(10);
                    doubleDrop(event, Herbalism);
                }

            }
            if (e == Blocks.CACTUS || e == Blocks.REEDS || e == Blocks.PUMPKIN || e == Blocks.VINE || e == Blocks.TALLGRASS || e == Blocks.MELON_BLOCK) {
                Herbalism.add(6);
                doubleDrop(event, Herbalism);
            }

        }
    }


    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.DEBUG) {
            new mclvlupGui(Minecraft.getMinecraft(), lvlups);
        }

    }

    public void doubleDrop(BlockEvent.BreakEvent event, LvlUp lvl) {

        ItemStack x = new ItemStack(event.getState().getBlock().getItemDropped(event.getState(), new java.util.Random(1), 0));
        if (lvl.getLvl() > rand.nextInt(500)) {
            event.getPlayer().sendMessage(new TextComponentString(ChatFormatting.YELLOW + "McLvlUp: Double Drop!"));
            event.getWorld().spawnEntity(new EntityItem(event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), x));
        }
    }
}
