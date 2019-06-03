package eatyourbeets.monsters;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public abstract class AbstractMove
{
    public DamageInfo damageInfo;
    public boolean disabled;
    public AbstractMonster owner;
    public final int ascensionLevel;
    public byte id;

    public void Init(byte id, AbstractMonster owner)
    {
        this.owner = owner;
        this.id = id;

        if (damageInfo != null)
        {
            damageInfo.owner = owner;
        }
    }

    public AbstractMove()
    {
        this.disabled = false;
        this.ascensionLevel = AbstractDungeon.ascensionLevel;
    }

    public abstract void Execute(AbstractPlayer target);
    public abstract void SetMove();

    public boolean CanUse(Byte previousMove)
    {
        return !disabled && previousMove != id;
    }
}
