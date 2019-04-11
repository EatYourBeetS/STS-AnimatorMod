package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_Boost;
import eatyourbeets.cards.Synergies;

public class Spearman extends AnimatorCard_Boost
{
    public static final String ID = CreateFullID(Spearman.class.getSimpleName());

    public Spearman()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(7, 0,1);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.SLASH_VERTICAL);

        if (ProgressBoost())
        {
            GameActionsHelper.ApplyPower(p, p, new DexterityPower(p, this.magicNumber), this.magicNumber);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(4);
            //upgradeSecondaryValue(1);
        }
    }

    @Override
    protected int GetBaseBoost()
    {
        return 2;
    }
}