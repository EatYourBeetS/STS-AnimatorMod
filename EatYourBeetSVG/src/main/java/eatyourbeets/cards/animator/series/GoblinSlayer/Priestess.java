package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardBadge;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RenderHelpers;

public class Priestess extends AnimatorCard
{
    public static final String ID = Register(Priestess.class, EYBCardBadge.Synergy);

    public Priestess()
    {
        super(ID, 1, CardRarity.COMMON, CardType.SKILL, CardTarget.SELF_AND_ENEMY);

        Initialize(0, 0, 4, 1);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetText(RenderHelpers.GetMagicNumberString(this));
    }

    @Override
    protected void OnUpgrade()
    {
        target = CardTarget.ALL;
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

        if (HasSynergy())
        {
            GameActions.Top.ExhaustFromPile(name, 1, p.drawPile, p.hand, p.discardPile)
            .ShowEffect(true, true)
            .SetOptions(true, true)
            .SetFilter(GameUtilities::IsCurseOrStatus);
        }
    }
}