package world.astrearp.infinistorage.blocks

import net.fabricmc.fabric.api.`object`.builder.v1.block.FabricBlockSettings
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.item.ItemPlacementContext
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.screen.ScreenHandler
import net.minecraft.sound.BlockSoundGroup
import net.minecraft.state.StateManager
import net.minecraft.state.property.DirectionProperty
import net.minecraft.state.property.Properties
import net.minecraft.util.*
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import world.astrearp.infinistorage.entity.TheStorageBlockEntity

class TheStorageBlock : BlockWithEntity(
    FabricBlockSettings.create()
        .mapColor(MapColor.BLUE)
        .strength(2.5F, 1200.0F)
        .sounds(BlockSoundGroup.NETHERITE)
) {

    init {
        Registry.register(Registries.BLOCK, Identifier("infinistorage", "the_storage"), this)
        Registry.register(
            Registries.ITEM,
            Identifier("infinistorage", "the_storage"),
            BlockItem(this, Item.Settings())
        )
        this.defaultState = this.stateManager.defaultState.with(FACING, Direction.DOWN)
    }

    @Deprecated("Deprecated in Java")
    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hand: Hand?,
        hit: BlockHitResult
    ): ActionResult {
        if (!world.isClient) {
            val blockEntity = world.getBlockEntity(pos) as TheStorageBlockEntity
            blockEntity.listItems(player)
        }
        return ActionResult.SUCCESS
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity = TheStorageBlockEntity(pos, state)

    @Deprecated("Deprecated in Java")
    override fun getRenderType(state: BlockState): BlockRenderType = BlockRenderType.MODEL

    @Deprecated("Deprecated in Java")
    override fun onStateReplaced(
        state: BlockState?,
        world: World?,
        pos: BlockPos?,
        newState: BlockState?,
        moved: Boolean
    ) {
        if (state?.block != newState?.block) {
            val blockEntity = world?.getBlockEntity(pos) as TheStorageBlockEntity
            ItemScatterer.spawn(world, pos, blockEntity)
            super.onStateReplaced(state, world, pos, newState, moved)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun hasComparatorOutput(state: BlockState?): Boolean = true

    @Deprecated("Deprecated in Java")
    override fun getComparatorOutput(state: BlockState?, world: World?, pos: BlockPos?): Int {
        return ScreenHandler.calculateComparatorOutput(world?.getBlockEntity(pos))
    }

    @Deprecated("Deprecated in Java")
    override fun rotate(state: BlockState, rotation: BlockRotation): BlockState {
        return state.with(Companion.FACING, rotation.rotate(state.get(Companion.FACING)))
    }

    @Deprecated("Deprecated in Java")
    override fun mirror(state: BlockState, mirror: BlockMirror): BlockState {
        return state.rotate(mirror.getRotation(state.get(Companion.FACING)))
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState? {
        return this.defaultState.with(Companion.FACING, ctx.playerLookDirection.opposite)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(FACING);
    }

    companion object {
        private val FACING: DirectionProperty = Properties.FACING
    }
}