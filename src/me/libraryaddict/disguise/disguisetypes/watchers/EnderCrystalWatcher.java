package me.libraryaddict.disguise.disguisetypes.watchers;

import com.comphenix.protocol.wrappers.BlockPosition;
import com.google.common.base.Optional;

import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.FlagType;
import me.libraryaddict.disguise.disguisetypes.FlagWatcher;

/**
 * @author Navid
 */
public class EnderCrystalWatcher extends FlagWatcher
{

    public EnderCrystalWatcher(Disguise disguise)
    {
        super(disguise);
    }

    public void setBeamTarget(BlockPosition position)
    {
        setValue(FlagType.ENDER_CRYSTAL_BEAM, Optional.of(position));
        sendData(FlagType.ENDER_CRYSTAL_BEAM);
    }

    public Optional<BlockPosition> getBeamTarget()
    {
        return getValue(FlagType.ENDER_CRYSTAL_BEAM);
    }

    public void setShowBottom(boolean bool)
    {
        setValue(FlagType.ENDER_CRYSTAL_PLATE, bool);
        sendData(FlagType.ENDER_CRYSTAL_PLATE);
    }

    public boolean isShowBottom()
    {
        return getValue(FlagType.ENDER_CRYSTAL_PLATE);
    }

}
