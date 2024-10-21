package world.astrearp.infinistorage

import net.fabricmc.api.ModInitializer
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import world.astrearp.infinistorage.blocks.TheStorageBlock
import world.astrearp.infinistorage.entity.TheStorageBlockEntity

class Infinistorage : ModInitializer {

    override fun onInitialize() {
        INFINI_ENTITY_TYPE = BlockEntityType.Builder.create(::TheStorageBlockEntity, TheStorageBlock()).build(null)
        Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier("infinistorage", "the_storage"),
            INFINI_ENTITY_TYPE
        )
    }

    companion object {
        lateinit var INFINI_ENTITY_TYPE: BlockEntityType<TheStorageBlockEntity>

    }

}
