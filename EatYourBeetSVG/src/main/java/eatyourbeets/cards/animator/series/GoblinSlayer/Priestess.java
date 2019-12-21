package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Priestess extends AnimatorCard
{
    public static final String ID = Register(Priestess.class.getSimpleName(), EYBCardBadge.Synergy);

    public Priestess()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF_AND_ENEMY);

        Initialize(0, 0, 4, 1);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        if (upgraded)
        {
            target = CardTarget.ALL;
        }
        else
        {
            target = CardTarget.SELF_AND_ENEMY;
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (upgraded)
        {
            for (AbstractMonster enemy : GameUtilities.GetCurrentEnemies(true))
            {
                GameActions.Bottom.ApplyWeak(p, enemy, secondaryValue);
            }
        }
        else if (m != null)
        {
            GameActions.Bottom.ApplyWeak(p, m, secondaryValue);
        }

        GameActions.Bottom.GainTemporaryHP(magicNumber);

        if (HasActiveSynergy())
        {
            GameActions.Top.ExhaustFromPile(name, 1, p.drawPile, p.hand, p.discardPile).ShowEffect(true)
            .SetOptions(true, true)
            .SetFilter(GameUtilities::IsCurseOrStatus);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            this.target = CardTarget.ALL;
        }
    }
}