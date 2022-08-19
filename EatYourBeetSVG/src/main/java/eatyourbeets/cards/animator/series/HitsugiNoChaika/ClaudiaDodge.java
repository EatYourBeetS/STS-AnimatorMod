package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.status.Status_Dazed;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;

public class ClaudiaDodge extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ClaudiaDodge.class)
            .SetSkill(2, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();

    public ClaudiaDodge()
    {
        super(DATA);

        Initialize(0, 6, 2);
        SetUpgrade(0, 0, 2);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Green(1);
        SetAffinity_Red(1);

        SetExhaust(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.MakeCardInHand(new Status_Dazed());
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ApplyLockOn(p, m, magicNumber);
        GameActions.Bottom.Reload(name, cards ->
        {
            if (cards.size() > 0)
            {
                GameActions.Bottom.TriggerOrbPassive(cards.size());
            }
        });
    }
}