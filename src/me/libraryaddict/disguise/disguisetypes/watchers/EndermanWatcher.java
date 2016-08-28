package me.libraryaddict.disguise.disguisetypes.watchers;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.comphenix.protocol.wrappers.WrappedBlockData;
import com.google.common.base.Optional;

import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.FlagType;

public class EndermanWatcher extends InsentientWatcher
{

    public EndermanWatcher(Disguise disguise)
    {
        super(disguise);
    }

    @Override
    public ItemStack getItemInMainHand()
    {
        Optional<WrappedBlockData> value = getValue(FlagType.ENDERMAN_ITEM);

        if (value.isPresent())
        {
            WrappedBlockData pair = value.get();
            Material id = pair.getType();
            int data = pair.getData();

            return new ItemStack(id, 1, (short) data);
        } else
        {
            return null;
        }
    }

    @Override
    public void setItemInMainHand(ItemStack itemstack)
    {
        setItemInMainHand(itemstack.getTypeId(), itemstack.getDurability());
    }

    @Deprecated
    public void setItemInMainHand(int typeId)
    {
        setItemInMainHand(typeId, 0);
    }

    public void setItemInMainHand(Material type)
    {
        setItemInMainHand(type, 0);
    }

    public void setItemInMainHand(Material type, int data)
    {
        Optional<WrappedBlockData> optional;

        if (type == null)
        {
            optional = Optional.<WrappedBlockData>absent();
        } else
        {
            optional = Optional.<WrappedBlockData>of(WrappedBlockData.createData(type, data));
        }

        setValue(FlagType.ENDERMAN_ITEM, optional);
    }

    @Deprecated
    public void setItemInMainHand(int typeId, int data)
    {
        setItemInMainHand(Material.getMaterial(typeId), data);
    }

    public boolean isAggressive()
    {
        return getValue(FlagType.ENDERMAN_AGRESSIVE);
    }

    public void setAggressive(boolean isAggressive)
    {
        setValue(FlagType.ENDERMAN_AGRESSIVE, isAggressive);
        sendData(FlagType.ENDERMAN_AGRESSIVE);
    }

}
