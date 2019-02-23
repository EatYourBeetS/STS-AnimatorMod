package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.unique.SwordBoomerangAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class Alexander extends AnimatorCard
{
    public static final String ID = CreateFullID(Alexander.class.getSimpleName());

    public Alexander()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(10,0,3);

        secondaryValue = baseSecondaryValue = 5;

        SetSynergy(Synergies.Fate);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();
        secondaryValue = baseSecondaryValue = this.damage / 2;
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        AbstractPlayer p = AbstractDungeon.player;
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, p, new PlatedArmorPower(p, this.secondaryValue), this.secondaryValue));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        for (AbstractCreature m1 : PlayerStatistics.GetCurrentEnemies(true))
        {
            if (m1 == m)
            {
                GameActionsHelper.DamageTarget(p, m1, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_HEAVY);
            }
            else
            {
                GameActionsHelper.DamageTarget(p, m1, this.secondaryValue, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE);
            }
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {          
            upgradeDamage(3);
            upgradeSecondaryValue(3 / 2);
            upgradeMagicNumber(2);
        }
    }
}