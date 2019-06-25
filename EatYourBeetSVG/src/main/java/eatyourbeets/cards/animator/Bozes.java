package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.BozesPower;

public class Bozes extends AnimatorCard
{
    public static final String ID = CreateFullID(Bozes.class.getSimpleName());

    public Bozes()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(7,0,2, 1);

        this.exhaust = true;

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
//        AbstractMonster enemy = null;
//        int lowestHealth = Integer.MAX_VALUE;
//        for (AbstractMonster m1 : PlayerStatistics.GetCurrentEnemies(true))
//        {
//            if (m1.currentHealth < lowestHealth)
//            {
//                enemy = m1;
//                lowestHealth = m1.currentHealth;
//            }
//        }
//
//        if (enemy != null)
//        {
//            GameActionsHelper.DamageTarget(p, enemy, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
//        }

        GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        GameActionsHelper.RandomCostReduction(magicNumber,1, false);
        GameActionsHelper.ApplyPower(p, p, new BozesPower(p, this.secondaryValue), this.secondaryValue);
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }
}