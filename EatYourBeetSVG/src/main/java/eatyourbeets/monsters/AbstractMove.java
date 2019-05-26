package eatyourbeets.monsters;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public abstract class AbstractMove
{
    public boolean disabled;
    public final AbstractMonster owner;
    public final int ascensionLevel;
    public final byte id;

    public AbstractMove(byte id, int ascensionLevel, AbstractMonster owner)
    {
        this.owner = owner;
        this.ascensionLevel = ascensionLevel;
        this.id = id;
    }

    public abstract void Execute(AbstractPlayer target);
    public abstract void SetMove();

    public boolean CanUse(Byte previousMove)
    {
        return !disabled && previousMove != id;
    }
}
