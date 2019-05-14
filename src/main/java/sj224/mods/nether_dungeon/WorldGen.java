package sj224.mods.nether_dungeon;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGen implements IWorldGenerator{

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		if(world.provider.getDimension()!=-1)return;
		if(world.isRemote)return;
		if(random.nextInt(1000)>=NetherMod.dungeonProbability)return;
		final int posX=(int)((chunkX+random.nextDouble())*16);
		final int posZ=(int)((chunkZ+random.nextDouble())*16);
		final int posY=1+random.nextInt(120);
		if(validatePosition(world,posX,posY,posZ)){
			generateStructure(world,posX,posY,posZ,random);
		}
	}

	private void generateStructure(World world, int posX, int posY, int posZ, Random random) {
		for(int x=posX-4;x<=posX+4;x++){
			for(int z=posZ-4;z<=posZ+4;z++){
				world.setBlockState(new BlockPos(x,posY+6,z),Blocks.NETHER_BRICK.getDefaultState());
				for(int y=posY;y<=posY+5;y++){
					world.setBlockState(new BlockPos(x,y,z),((x==posX-4||x==posX+4||z==posZ-4||z==posZ+4)?Blocks.NETHER_BRICK:(y==posY?Blocks.SOUL_SAND:Blocks.AIR)).getDefaultState());
				}
				if(world.getBlockState(new BlockPos(x,posY-1,z)).getBlock()==Blocks.AIR)world.setBlockState(new BlockPos(x,posY-1,z),Blocks.NETHER_BRICK.getDefaultState());
			}
			
		}
		for(int k=-2;k<=2;k+=2){
			for(int h=2;h<=4;h++){
				world.setBlockState(new BlockPos(posX+4,posY+h,posZ+k),Blocks.NETHER_BRICK_FENCE.getDefaultState());
				world.setBlockState(new BlockPos(posX-4,posY+h,posZ+k),Blocks.NETHER_BRICK_FENCE.getDefaultState());
				world.setBlockState(new BlockPos(posX+k,posY+h,posZ+4),Blocks.NETHER_BRICK_FENCE.getDefaultState());
				world.setBlockState(new BlockPos(posX+k,posY+h,posZ-4),Blocks.NETHER_BRICK_FENCE.getDefaultState());
			}
		}
		
		world.setBlockState(new BlockPos(posX+2,posY+6,posZ+2),Blocks.NETHER_BRICK_FENCE.getDefaultState());
		world.setBlockState(new BlockPos(posX+2,posY+6,posZ-2),Blocks.NETHER_BRICK_FENCE.getDefaultState());
		world.setBlockState(new BlockPos(posX-2,posY+6,posZ+2),Blocks.NETHER_BRICK_FENCE.getDefaultState());
		world.setBlockState(new BlockPos(posX-2,posY+6,posZ-2),Blocks.NETHER_BRICK_FENCE.getDefaultState());
		
		world.setBlockState(new BlockPos(posX+1,posY,posZ),Blocks.NETHER_BRICK.getDefaultState());
		world.setBlockState(new BlockPos(posX-1,posY,posZ),Blocks.NETHER_BRICK.getDefaultState());
		world.setBlockState(new BlockPos(posX,posY,posZ+1),Blocks.NETHER_BRICK.getDefaultState());
		world.setBlockState(new BlockPos(posX,posY,posZ-1),Blocks.NETHER_BRICK.getDefaultState());
		world.setBlockState(new BlockPos(posX,posY,posZ),Blocks.LAVA.getDefaultState());
		world.setBlockState(new BlockPos(posX,posY+2,posZ),Blocks.MOB_SPAWNER.getDefaultState());
		TileEntityMobSpawner e=(TileEntityMobSpawner) world.getTileEntity(new BlockPos(posX,posY+2,posZ));
		MobSpawnerBaseLogic spawnerLogic=e.getSpawnerBaseLogic();
		int v=random.nextInt(1000);
		if(v<400){
			spawnerLogic.setEntityId(new ResourceLocation("minecraft:blaze"));
		}else if(v<750){
			spawnerLogic.setEntityId(new ResourceLocation("minecraft:skeleton"));
		}else if(v<900){
			spawnerLogic.setEntityId(new ResourceLocation("minecraft:wither_skeleton"));
		}else{
			spawnerLogic.setEntityId(new ResourceLocation("minecraft:spider"));
		}
		for(int i=1+random.nextInt(2);i>0;i--){
			TileEntityChest te=null;
			int k=random.nextInt(7)-3;
			switch(random.nextInt(4)){
			case 1:world.setBlockState(new BlockPos(posX+3,posY+1,posZ+k),Blocks.CHEST.getStateFromMeta(4));te=(TileEntityChest)world.getTileEntity(new BlockPos(posX+3,posY+1,posZ+k));break;
			case 2:world.setBlockState(new BlockPos(posX-3,posY+1,posZ+k),Blocks.CHEST.getStateFromMeta(5));te=(TileEntityChest)world.getTileEntity(new BlockPos(posX-3,posY+1,posZ+k));break;
			case 3:world.setBlockState(new BlockPos(posX+k,posY+1,posZ+3),Blocks.CHEST.getStateFromMeta(2));te=(TileEntityChest)world.getTileEntity(new BlockPos(posX+k,posY+1,posZ+3));break;
			default:world.setBlockState(new BlockPos(posX+k,posY+1,posZ-3),Blocks.CHEST.getStateFromMeta(3));te=(TileEntityChest)world.getTileEntity(new BlockPos(posX+k,posY+1,posZ-3));break;
			}
			te.setLootTable(NetherMod.lootTable,random.nextLong());
		}
		
	}

	private boolean validatePosition(World world, int posX, int posY, int posZ) {
		int ct=0;
		int gd=0;
		for(int x=posX-4;x<=posX+4;x++){
			for(int z=posZ-4;z<=posZ+4;z++){
				for(int y=posY;y<=posY+7;y++){
					if(world.getBlockState(new BlockPos(x,y,z)).getBlock()==Blocks.NETHERRACK||world.getBlockState(new BlockPos(x,y,z)).getBlock()==Blocks.SOUL_SAND||world.getBlockState(new BlockPos(x,y,z)).getBlock()==Blocks.QUARTZ_ORE){
						ct++;
						if(y==posY)gd++;
					}else if(world.getBlockState(new BlockPos(x,y,z)).getBlock()==Blocks.LAVA){
						ct+=1;
					}
					else if(world.getBlockState(new BlockPos(x,y,z)).getBlock()==Blocks.NETHER_BRICK)return false;
				}
			}
		}
		return gd>32&&ct>120&&ct<540;
	}
	
}