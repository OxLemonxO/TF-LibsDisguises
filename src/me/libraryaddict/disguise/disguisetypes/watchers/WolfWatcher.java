package me.libraryaddict.disguise.disguisetypes.watchers;

import org.bukkit.DyeColor;

import me.libraryaddict.disguise.disguisetypes.AnimalColor;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.FlagType;

public class WolfWatcher extends TameableWatcher
{

    public WolfWatcher(Disguise disguise)
    {
        super(disguise);
    }

    public AnimalColor getCollarColor()
    {
        return AnimalColor.getColor(getValue(FlagType.WOLF_COLLAR));
    }

    /**
     * Used for tail rotation.
     *
     * @return
     */
    public float getDamageTaken()
    {
        return (float) getValue(FlagType.WOLF_DAMAGE);
    }

    /**
     * Used for tail rotation.
     *
     * @param damage
     */
    public void setDamageTaken(float damage)
    {
        setValue(FlagType.WOLF_DAMAGE, damage);
        sendData(FlagType.WOLF_DAMAGE);
    }

    public boolean isBegging()
    {
        return (boolean) getValue(FlagType.WOLF_BEGGING);
    }

    public void setBegging(boolean begging)
    {
        setValue(FlagType.WOLF_BEGGING, begging);
        sendData(FlagType.WOLF_BEGGING);
    }

    public boolean isAngry()
    {
        return isTameableFlag(2);
    }

    public void setAngry(boolean angry)
    {
        setTameableFlag(2, angry);
    }

    public void setCollarColor(AnimalColor color)
    {
        setCollarColor(DyeColor.getByWoolData((byte) color.getId()));
    }

    public void setCollarColor(DyeColor newColor)
    {
        if (!isTamed())
        {
            setTamed(true);
        }

        if (newColor.getWoolData() == getCollarColor().getId())
        {
            return;
        }

        setValue(FlagType.WOLF_COLLAR, (int) newColor.getDyeData());
        sendData(FlagType.WOLF_COLLAR);
    }

}
