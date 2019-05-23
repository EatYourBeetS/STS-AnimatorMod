package eatyourbeets.monsters.Bosses.KrulTepesMoveset;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.Utilities;
import eatyourbeets.monsters.AbstractMove;

public class Move_MultiSlash extends AbstractMove
{
    private final DamageInfo damageInfo;
    private final int TIMES;

    private int timesCounter;

    public Move_MultiSlash(int id, int ascensionLevel, AbstractMonster owner)
    {
        super((byte) id, ascensionLevel, owner);

        if (ascensionLevel >= 6)
        {
            damageInfo = new DamageInfo(owner, 2);
            TIMES = 4;
        }
        else
        {
            damageInfo = new DamageInfo(owner, 3);
            TIMES = 4;
        }
    }

    @Override
    public boolean CanUse(Byte previousMove)
    {
        if (super.CanUse(previousMove))
        {
            StrengthPower strength = Utilities.SafeCast(owner.getPower(StrengthPower.POWER_ID), StrengthPower.class);

            return strength != null && strength.amount > 3;
        }

        return false;
    }

    public void SetMove()
    {
        owner.setMove(id, AbstractMonster.Intent.ATTACK, damageInfo.base, TIMES, true);
    }

    public void Execute(AbstractPlayer target)
    {
        damageInfo.applyPowers(owner, target);
        timesCounter = TIMES;
        Attack(target);
    }

    private void Attack(AbstractPlayer target)
    {
        if (timesCounter > 0)
        {
            GameActionsHelper.Callback(new DamageAction(target, damageInfo, AbstractGameAction.AttackEffect.SLASH_HEAVY), this::OnDamage, target.currentHealth);
            timesCounter -= 1;
        }
    }

    private void OnDamage(Object state, AbstractGameAction action)
    {
        Integer previousHealth = Utilities.SafeCast(state, Integer.class);
        if (previousHealth != null)
        {
            AbstractPlayer p = AbstractDungeon.player;

            int difference = previousHealth - p.currentHealth;
            if (difference > 0)
            {
                GameActionsHelper.AddToBottom(new HealAction(owner, owner, difference));
            }

            Attack(p);
        }
    }
}