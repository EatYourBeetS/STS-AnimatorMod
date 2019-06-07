package eatyourbeets.monsters;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.PlayerStatistics;

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
        this.ascensionLevel = PlayerStatistics.GetAscensionLevel();
    }

    public int GetBonus(int base, float percentage)
    {
       return Math.round(base * percentage * (ascensionLevel / 20f));
    }

    public abstract void Execute(AbstractPlayer target);
    public abstract void SetMove();

    public boolean CanUse(Byte previousMove)
    {
        return !disabled && previousMove != id;
    }
}
