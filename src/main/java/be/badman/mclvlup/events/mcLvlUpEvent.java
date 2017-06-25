package be.badman.mclvlup.events;

import be.badman.mclvlup.capabilities.LvlUp;
import be.badman.mclvlup.gui.mclvlupGui;
import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import scala.util.Random;

import java.util.*;

/**
 * Created by Kevin on 24-6-2017.
 */
public class mcLvlUpEvent {
    private ArrayList<String> BlocksPlaced = new ArrayList<>();
    private EntityPlayer player;

    private ArrayList<LvlUp> lvlUpsAL = new ArrayList<>();
    private Map<String, LvlUp> lvlUps = new HashMap<>();
    private Random rand = new Random();
    private Long rightClickTime = 0L;
    private Long ActivationMiningTime = 0L;
    private Map<Block, Float> farmingBlocks = new HashMap<>();
    private Map<Block, Float> miningBlocks = new HashMap<>();
    private Map<Block, Float> woodCuttingBlocks = new HashMap<>();
    private Map<Block, Float> excavtionBlocks = new HashMap<>();
    private void brokenBlocks() {
        miningBlocks.clear();
        farmingBlocks.clear();
        excavtionBlocks.clear();
        woodCuttingBlocks.clear();
        //Mining
        miningBlocks.put(Blocks.STONE, 3F);
        miningBlocks.put(Blocks.HARDENED_CLAY, 3F);
        miningBlocks.put(Blocks.NETHERRACK, 3F);
        miningBlocks.put(Blocks.SANDSTONE, 3F);
        miningBlocks.put(Blocks.PACKED_ICE, 5F);
        miningBlocks.put(Blocks.STAINED_HARDENED_CLAY, 5F);
        miningBlocks.put(Blocks.MOSSY_COBBLESTONE, 5F);
        miningBlocks.put(Blocks.COAL_ORE, 10F);
        miningBlocks.put(Blocks.PRISMARINE, 5F);
        miningBlocks.put(Blocks.RED_SANDSTONE, 10F);
        miningBlocks.put(Blocks.QUARTZ_ORE, 10F);
        miningBlocks.put(Blocks.REDSTONE_ORE, 15F);
        miningBlocks.put(Blocks.END_STONE, 15F);
        miningBlocks.put(Blocks.OBSIDIAN, 15F);
        miningBlocks.put(Blocks.IRON_ORE, 25F);
        miningBlocks.put(Blocks.GOLD_ORE, 25F);
        miningBlocks.put(Blocks.LAPIS_ORE, 40F);
        miningBlocks.put(Blocks.DIAMOND_ORE, 75F);
        miningBlocks.put(Blocks.EMERALD_ORE, 100F);
        //Woodcutting
        woodCuttingBlocks.put(Blocks.LOG, 10F);
        woodCuttingBlocks.put(Blocks.LOG2, 10F);
        woodCuttingBlocks.put(Blocks.BROWN_MUSHROOM_BLOCK, 10F);
        woodCuttingBlocks.put(Blocks.RED_MUSHROOM_BLOCK, 10F);
        woodCuttingBlocks.put(Blocks.LEAVES, 1F);
        woodCuttingBlocks.put(Blocks.LEAVES2, 1F);
        //Excavation
        excavtionBlocks.put(Blocks.DIRT, 5F);
        excavtionBlocks.put(Blocks.SAND, 5F);
        excavtionBlocks.put(Blocks.CLAY, 5F);
        excavtionBlocks.put(Blocks.GRAVEL, 5F);
        excavtionBlocks.put(Blocks.GRASS, 5F);
        excavtionBlocks.put(Blocks.SOUL_SAND, 5F);
        excavtionBlocks.put(Blocks.MYCELIUM, 6F);
        //Farming
        farmingBlocks.put(Blocks.WHEAT,10F);
        farmingBlocks.put(Blocks.CARROTS,10F);
        farmingBlocks.put(Blocks.POTATOES,10F);
        farmingBlocks.put(Blocks.NETHER_WART,10F);
        farmingBlocks.put(Blocks.CACTUS,5F);
        farmingBlocks.put(Blocks.REEDS,5F);
        farmingBlocks.put(Blocks.PUMPKIN,5F);
        farmingBlocks.put(Blocks.VINE,5F);
        farmingBlocks.put(Blocks.TALLGRASS,5F);
        farmingBlocks.put(Blocks.MELON_BLOCK,5F);
    }
    //region PlayerConstructingStuff
    public void ConstructMcLvlUp(EntityPlayer player) {
        lvlUps.clear();
        lvlUps.put("Mining", new LvlUp("Mining", "mclvlupMining", player));
        lvlUps.put("Excavation", new LvlUp("Excavation", "mclvlupExcavation", player));
        lvlUps.put("Woodcutting", new LvlUp("Woodcutting", "mclvlupWoodcutting", player));
        lvlUps.put("Herbalism", new LvlUp("Herbalism", "mclvlupHerbalism", player));
        lvlUps.put("Fishing", new LvlUp("Fishing", "mclvlupFishing", player));
        lvlUps.put("Unarmed", new LvlUp("Unarmed", "mclvlupUnarmed", player));
        lvlUpsAL = new ArrayList<>(lvlUps.values());
        brokenBlocks();
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

    public void doubleDrop(BlockEvent.BreakEvent event, LvlUp lvl, int chance) {
        ItemStack x = new ItemStack(event.getState().getBlock().getItemDropped(event.getState(), new java.util.Random(1), 0));
        if (lvl.getLvl() > rand.nextInt(chance)) {
            event.getPlayer().sendMessage(new TextComponentString(ChatFormatting.YELLOW + "McLvlUp: Double Drop - " + x.getItem().getItemStackDisplayName(x)));
            event.getWorld().spawnEntity(new EntityItem(event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), x));
        }
    }

    @SubscribeEvent
    public void onJoin(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent event) {
        ConstructMcLvlUp(event.player);
    }

    @SubscribeEvent
    public void respawn(net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent event) {
        if (player != null) {
            for (Map.Entry<String, LvlUp> entry : lvlUps.entrySet()) {
                event.player.getEntityData().setFloat(entry.getValue().getKey(), player.getEntityData().getFloat(entry.getValue().getKey()));
            }
            for (String x : BlocksPlaced) {
                event.player.getEntityData().setBoolean(x, true);
            }
            ConstructMcLvlUp(event.player);
        }
    }

    @SubscribeEvent
    public void entityDied(LivingDeathEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            player = (EntityPlayer) event.getEntity();
        } else {
            //region Unarmed
        if (event.getSource().getTrueSource() instanceof EntityPlayer) {
            if(Minecraft.getMinecraft().player.getHeldItem(EnumHand.MAIN_HAND).getItem() == Items.AIR) {
                lvlUps.get("Unarmed").add(5);
                if (event.getEntity() instanceof EntityIronGolem) {
                    lvlUps.get("Unarmed").add(20);
                }
                if (event.getEntity() instanceof net.minecraft.entity.monster.IMob) {
                    lvlUps.get("Unarmed").add(45);
                }
            }
        }
        //endregion
    }
    }

    @SubscribeEvent
    public void onRenderGui(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.DEBUG) {
            new mclvlupGui(Minecraft.getMinecraft(), lvlUpsAL);
        }
    }
//endregion


    @SubscribeEvent
    public void miningBlockPlaced(BlockEvent.PlaceEvent event) {
        Block placed = event.getPlacedBlock().getBlock();
        if (miningBlocks.containsKey(placed)) {
            event.getPlayer().getEntityData().setBoolean(event.getPos().toString(), true);
            BlocksPlaced.add(event.getPos().toString());
        }
    }

    @SubscribeEvent
    public void blockBroken(BlockEvent.BreakEvent event) {
        if (!event.getPlayer().getEntityData().hasKey(event.getPos().toString())) {
            Block brokenBlock = event.getState().getBlock();
            Item heldItem = event.getPlayer().getHeldItemMainhand().getItem();
            if (heldItem == Items.DIAMOND_PICKAXE || heldItem == Items.WOODEN_PICKAXE || heldItem == Items.STONE_PICKAXE || heldItem == Items.IRON_PICKAXE || heldItem == Items.GOLDEN_PICKAXE) {
                if (miningBlocks.containsKey(brokenBlock)) {
                    lvlUps.get("Mining").add(miningBlocks.get(brokenBlock));
                    doubleDrop(event, lvlUps.get("Mining"), 1000);
                }
            }
            if (excavtionBlocks.containsKey(brokenBlock)) {
                lvlUps.get("Excavation").add(excavtionBlocks.get(brokenBlock));
                doubleDrop(event, lvlUps.get("Excavation"), 1000);
                //region specialthingsbroken
                if (brokenBlock == Blocks.GRAVEL) {
                    if (lvlUps.get("Excavation").getLvl() > 35) {
                        if (5 == rand.nextInt(100)) {
                            event.getWorld().spawnEntity(new EntityItem(event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), new ItemStack(Items.GUNPOWDER)));
                            lvlUps.get("Excavation").add(5);

                        }
                    }
                    if (lvlUps.get("Excavation").getLvl() > 75) {
                        if (5 == rand.nextInt(100)) {
                            event.getWorld().spawnEntity(new EntityItem(event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), new ItemStack(Items.BONE)));
                            lvlUps.get("Excavation").add(5);

                        }
                    }
                }
                if (brokenBlock == Blocks.CLAY) {
                    if (lvlUps.get("Excavation").getLvl() > 100) {
                        if (5 == rand.nextInt(100)) {
                            event.getWorld().spawnEntity(new EntityItem(event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), new ItemStack(Items.BUCKET)));
                            lvlUps.get("Excavation").add(5);

                        }
                    }
                    if (lvlUps.get("Excavation").getLvl() > 35) {
                        if (5 == rand.nextInt(100)) {
                            event.getWorld().spawnEntity(new EntityItem(event.getWorld(), event.getPos().getX(), event.getPos().getY(), event.getPos().getZ(), new ItemStack(Items.SLIME_BALL)));
                            lvlUps.get("Excavation").add(5);

                        }
                    }
                }
                //endregion
            }
            if(woodCuttingBlocks.containsKey(brokenBlock)){
                lvlUps.get("Woodcutting").add(woodCuttingBlocks.get(brokenBlock));
                doubleDrop(event, lvlUps.get("Woodcutting"), 1000);
            }
            if(farmingBlocks.containsKey(brokenBlock)){
                BlockCrops ts = (BlockCrops) brokenBlock;
                if (ts.isMaxAge(event.getState())) {
                    lvlUps.get("Herbalism").add(farmingBlocks.get(brokenBlock));
                    doubleDrop(event, lvlUps.get("Herbalism"), 1000);
                }
            }
        }
    }
    //region Fishing
    @SubscribeEvent
    public void catchFish(ItemFishedEvent event) {
        lvlUps.get("Fishing").add(15);
        if ( lvlUps.get("Fishing").getLvl() > rand.nextInt(250)) {
            event.getEntityPlayer().getEntityWorld().spawnEntity(new EntityItem(event.getEntityPlayer().getEntityWorld(), event.getEntityPlayer().getPosition().getX(), event.getEntityPlayer().getPosition().getY(), event.getEntityPlayer().getPosition().getZ(), new ItemStack(event.getDrops().get(0).getItem())));
        }
        //still doesn't work
        if(event.getHookEntity().getRidingEntity() instanceof IMob){
            System.out.println("datched");
        }
    }
    //endregion
    @SubscribeEvent
    public void rightClick(PlayerInteractEvent.RightClickBlock event) {
        Item held = Minecraft.getMinecraft().player.getHeldItemMainhand().getItem();
        Item subHand = Minecraft.getMinecraft().player.getHeldItemOffhand().getItem();
        //region miningRightClick
        if (held == Items.DIAMOND_PICKAXE || held == Items.WOODEN_PICKAXE || held == Items.STONE_PICKAXE || held == Items.IRON_PICKAXE || held == Items.GOLDEN_PICKAXE) {
            if (event.getWorld().getTotalWorldTime() - rightClickTime > 100) {
                if (subHand == Items.AIR) {
                    lvlUps.get("Mining").sayPoints();
                    lvlUps.get("Mining").sayLvl();
                }
                if (subHand == Items.COAL) {
                    if (event.getWorld().getTotalWorldTime() - ActivationMiningTime > 6000) {
                        ActivationMiningTime = event.getWorld().getTotalWorldTime();
                        event.getEntityPlayer().sendMessage(new TextComponentString(ChatFormatting.BLUE + "Activated: " + lvlUps.get("Mining").getName() + " ability."));
                        event.getEntityPlayer().addPotionEffect(new PotionEffect(Potion.getPotionById(3), (int) (lvlUps.get("Mining").getLvl() * 20), 15));
                    } else {
                        if (event.getWorld().getTotalWorldTime() - rightClickTime > 100) {
                            event.getEntityPlayer().sendMessage(new TextComponentString(ChatFormatting.BLUE + "Wait: " + (6000 - (event.getWorld().getTotalWorldTime() - ActivationMiningTime)) + " ticks."));
                        }
                    }
                }
            }

            rightClickTime = event.getWorld().getTotalWorldTime();
        }
        //endregion
        //region farmingRightClick
        if (held == Items.WHEAT_SEEDS) {
            if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.COBBLESTONE) {
                event.getItemStack().setCount(event.getItemStack().getCount() - 1);
                if (lvlUps.get("Herbalism").getLvl() > rand.nextInt(250)) {
                    event.getWorld().setBlockState(event.getPos(), Blocks.MOSSY_COBBLESTONE.getDefaultState());
                    event.getEntityPlayer().sendMessage(new TextComponentString(ChatFormatting.GREEN + "McLvlUp: Green Thumb Luck!"));
                }
            }
            if (event.getWorld().getBlockState(event.getPos()).getBlock() == Blocks.DIRT) {
                event.getItemStack().setCount(event.getItemStack().getCount() - 1);
                if (lvlUps.get("Herbalism").getLvl() > rand.nextInt(250)) {
                    event.getWorld().setBlockState(event.getPos(), Blocks.GRASS.getDefaultState());
                    event.getEntityPlayer().sendMessage(new TextComponentString(ChatFormatting.GREEN + "McLvlUp: Green Thumb Luck!"));
                }
            }
        }
        //endregion
        //region excavtionRightClick
        if (held == Items.DIAMOND_SHOVEL || held == Items.WOODEN_SHOVEL || held == Items.STONE_SHOVEL || held == Items.IRON_SHOVEL || held == Items.GOLDEN_SHOVEL) {
            if (event.getWorld().getTotalWorldTime() - rightClickTime > 100) {

                lvlUps.get("Excavation").sayLvl();
                lvlUps.get("Excavation").sayPoints();
                rightClickTime = event.getWorld().getTotalWorldTime();
            }
        }
        //endregion
    }

    @SubscribeEvent
    public void hurtEvent(LivingHurtEvent event) {
        if (event.getEntity() instanceof EntityPlayer) {
            if (lvlUps.get("Unarmed").getLvl() > rand.nextInt(2000)) {
                event.setCanceled(true);
                event.getEntity().sendMessage(new TextComponentString(ChatFormatting.RED + "McLvlUp: Rolled damage."));
            }
        } else {
            if (event.getSource().getTrueSource() instanceof EntityPlayer) {
                if (((EntityPlayer) event.getSource().getTrueSource()).getHeldItemMainhand().getItem() == Items.AIR) {
                    //xwerffx damage amount idea
                    float amount = (float) Math.floor(lvlUps.get("Unarmed").getLvl()/30);
                    if(amount <= 4){
                        event.setAmount(event.getAmount() + amount);
                    }else{
                        event.setAmount(event.getAmount() + 4);
                    }

                }

            }
        }
    }
}
