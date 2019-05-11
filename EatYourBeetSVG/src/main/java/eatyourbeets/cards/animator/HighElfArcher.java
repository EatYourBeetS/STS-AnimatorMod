package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard_Boost;
import eatyourbeets.cards.Synergies;

public class HighElfArcher extends AnimatorCard_Boost
{
    public static final String ID = CreateFullID(HighElfArcher.class.getSimpleName());

    public HighElfArcher()
    {
        super(ID, 0, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(4, 0, 1);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTargetPiercing(p, m, this, AbstractGameAction.AttackEffect.BLUNT_LIGHT);

//        int count = 0;
//        for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisTurn)
//        {
//            if (c instanceof HighElfArcher)
//            {
//                count += 1;
//            }
//        }

        if (ProgressBoost())
        {
            GameActionsHelper.DrawCard(p, this.magicNumber);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(1);
            upgradeSecondaryValue(1);
        }
    }

    @Override
    protected int GetBaseBoost()
    {
        return upgraded ? 3 : 2;
    }
}