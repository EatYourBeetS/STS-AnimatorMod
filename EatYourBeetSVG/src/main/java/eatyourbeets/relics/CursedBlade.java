package eatyourbeets.relics;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.GameActionsHelper;

public class CursedBlade extends AnimatorRelic
{
    public static final String ID = CreateFullID(CursedBlade.class.getSimpleName());

    public CursedBlade()
    {
        super(ID, RelicTier.BOSS, LandingSound.HEAVY);
    }

//    @Override
//    public void atBattleStart()
//    {
//        super.atBattleStart();
//
//        AbstractPlayer p = AbstractDungeon.player;
//        GameActionsHelper.ApplyPower(p, p, new EnvenomPower(p, 1), 1);
//    }
//
//    @Override
//    public void onVictory()
//    {
//        super.onVictory();
//
//        AbstractPlayer p = AbstractDungeon.player;
//        p.decreaseMaxHealth(2);
//        this.flash();
//    }

    @Override
    public String getUpdatedDescription()
    {
        if (IsImproved())
        {
            return DESCRIPTIONS[1];
        }
        else
        {
            return DESCRIPTIONS[0];
        }
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        AbstractDungeon.player.decreaseMaxHealth(AbstractDungeon.player.maxHealth / 2);
    }

    @Override
    public void onPlayerEndTurn()
    {
        super.onPlayerEndTurn();

        if (IsImproved())
        {
            AbstractPlayer p = AbstractDungeon.player;
            if (p.currentHealth >= p.maxHealth)
            {
                GameActionsHelper.GainTemporaryHP(p, p, 3);
            }
        }
    }

    public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target)
    {
        AbstractPlayer p = AbstractDungeon.player;
        if (p.currentHealth < p.maxHealth)
        {
            if (damageAmount > 0 && target != p)
            {
                this.flash();
                GameActionsHelper.AddToBottom(new HealAction(p, p, 2));
            }
        }
    }

    private boolean IsImproved()
    {
        return AbstractDungeon.isAscensionMode && AbstractDungeon.ascensionLevel >= 14;
    }
}