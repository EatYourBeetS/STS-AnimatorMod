package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.colorless.Shiv;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class AcuraTooru extends AnimatorCard
{
    public static final String ID = CreateFullID(AcuraTooru.class.getSimpleName());

    public AcuraTooru()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(5, 0, 3);

        baseSecondaryValue = secondaryValue = 1;

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (int i = 0; i < this.magicNumber; i++)
        {
            GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        }

        GameActionsHelper.ApplyPower(p, p, new DrawCardNextTurnPower(p, 1), 1);

        if (HasActiveSynergy())
        {
            for (int i = 0; i < this.secondaryValue; i++)
            {
                GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(new Shiv()));
            }
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
}