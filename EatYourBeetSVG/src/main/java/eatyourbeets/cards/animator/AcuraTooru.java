package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class AcuraTooru extends AnimatorCard
{
    public static final String ID = CreateFullID(AcuraTooru.class.getSimpleName());

    public AcuraTooru()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(6, 0, 1);

        baseSecondaryValue = secondaryValue = 2;

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);

        for (int i = 0; i < this.secondaryValue; i++)
        {
            GameActionsHelper.AddToBottom(new MakeTempCardInHandAction(ThrowingKnife.GetRandomCard()));
        }

        if (HasActiveSynergy())
        {
            GameActionsHelper.CycleCardAction(this.magicNumber);
            //GameActionsHelper.ApplyPower(p, p, new DrawCardNextTurnPower(p, 1), 1);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
            upgradeDamage(2);
        }
    }
}