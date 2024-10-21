package world.astrearp.infinistorage.entity

import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.inventory.Inventory
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.registry.RegistryWrapper
import net.minecraft.text.Text
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.MathHelper
import world.astrearp.infinistorage.Infinistorage

class TheStorageBlockEntity(pos: BlockPos, state: BlockState) :
    BlockEntity(Infinistorage.INFINI_ENTITY_TYPE, pos, state),
    Inventory {

    private var items: DefaultedList<ItemStack> = DefaultedList.ofSize(54, ItemStack.EMPTY)

    fun listItems(player: PlayerEntity) {
        val localItems = items.filter { !it.isEmpty }
        if (localItems.isEmpty()) {
            player.sendMessage(Text.literal("Storage is empty!"), true)
        } else {
            val message = StringBuilder("Stored items:")
            for ((item, amount) in localItems.withIndex()) {
                message.append("\n$item: $amount")
            }
            player.sendMessage(Text.literal(message.toString()), false)
        }
    }


    override fun writeNbt(nbt: NbtCompound?, registryLookup: RegistryWrapper.WrapperLookup?) {
        super.writeNbt(nbt, registryLookup)
        Inventories.writeNbt(nbt, items, registryLookup)
    }

    override fun readNbt(nbt: NbtCompound?, registryLookup: RegistryWrapper.WrapperLookup?) {
        super.readNbt(nbt, registryLookup)
        this.items = DefaultedList.ofSize(size(), ItemStack.EMPTY)
        Inventories.readNbt(nbt, items, registryLookup)
    }

    private fun extractItem(slot: Int, amount: Int): ItemStack {
        if (items[slot].isEmpty) return ItemStack.EMPTY
        val stackToExtract = items[slot].copy()

        val extractedCount = MathHelper.clamp(amount, 0, stackToExtract.count)
        stackToExtract.count = extractedCount

        items[slot].decrement(extractedCount)
        return stackToExtract
    }

    override fun clear() = items.clear()

    override fun size() = items.size

    override fun isEmpty() = items.isEmpty()

    override fun getStack(slot: Int) = items[slot]

    override fun removeStack(slot: Int, amount: Int) = extractItem(slot, amount)

    override fun removeStack(slot: Int) = items[slot].also { items[slot] = ItemStack.EMPTY }

    override fun setStack(slot: Int, stack: ItemStack) {
        items[slot] = stack
        if (stack.count > maxCountPerStack) {
            stack.count = maxCountPerStack
        }
    }

    override fun markDirty() = super.markDirty()

    override fun canPlayerUse(player: PlayerEntity?): Boolean {
        return true
    }


}