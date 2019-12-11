package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameUtilities;

public class Spearman extends AnimatorCard
{
    public static final String ID = Register(Spearman.class.getSimpleName(), EYBCardBadge.Special);

    public Spearman()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(9, 0, 1);

        SetPiercing(true);
        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL)
        .SetOptions(true, true)
        .AddCallback(enemy ->
        {
            if (GameUtilities.TriggerOnKill(enemy, true) && EffectHistory.TryActivateLimited(cardID))
            {
                GameActions.Bottom.Motivate(2);
            }
        });

        GameActions.Bottom.GainAgility(magicNumber);
        GameActions.Bottom.MakeCardInHand(new Wound());
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(4);
        }
    }
}