package eatyourbeets.cards.animator.series.LogHorizon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Serara extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Serara.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.None);

    public Serara()
    {
        super(DATA);

        Initialize(0, 0, 6, 17);
        SetUpgrade(0, 0, 0, 6);

        SetSynergy(Synergies.LogHorizon);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        if (!GameUtilities.InBattle() || GameUtilities.GetTempHP() <= secondaryValue)
        {
            return TempHPAttribute.Instance.SetCard(this, true);
        }

        return null;
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (GameUtilities.GetTempHP(p) <= secondaryValue)
        {
            GameActions.Bottom.GainTemporaryHP(magicNumber);
        }

        for (AbstractCard c : p.drawPile.group)
        {
            if (c.cardID.equals(Nyanta.DATA.ID))
            {
                GameActions.Bottom.Motivate(c, 1);
            }
        }

        for (AbstractCard c : p.hand.group)
        {
            if (c.cardID.equals(Nyanta.DATA.ID))
            {
                GameActions.Bottom.Motivate(c, 1);
            }
        }
    }
}