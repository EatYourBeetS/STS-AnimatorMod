package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameActionsHelper2;

public class AcuraTooru extends AnimatorCard
{
    public static final String ID = Register(AcuraTooru.class.getSimpleName(), EYBCardBadge.Synergy, EYBCardBadge.Discard);

    public AcuraTooru()
    {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);

        Initialize(4, 0, 4, 2);

        SetSynergy(Synergies.Chaika);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActionsHelper2.GainBlock(magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper2.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);
        GameActionsHelper2.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActionsHelper2.CreateThrowingKnives(secondaryValue);

        if (HasActiveSynergy())
        {
            GameActionsHelper2.GainBlock(magicNumber);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(1);
        }
    }
}