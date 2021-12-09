package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.ui.animator.combat.EYBAffinityMeter;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Cecily extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Cecily.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage();

    public Cecily()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
        SetUpgrade(0,0,1);

        SetAffinity_Light(1);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        if (info.IsSynergizing) {
            GameActions.Top.Cycle(name, magicNumber);
        }

        GameActions.Bottom.RerollAffinity(EYBAffinityMeter.Target.CurrentAffinity).AddCallback(a -> {
            GameActions.Last.SelectFromHand(name, secondaryValue, false)
                    .SetOptions(false, false, false)
                    .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
                    .SetFilter(c -> c instanceof AnimatorCard && !(GameUtilities.GetAffinityLevel(c, a, true) > 0))
                    .AddCallback(cards ->
                    {
                        AnimatorCard card = JUtils.SafeCast(cards.get(0), AnimatorCard.class);
                        if (card != null)
                        {
                            card.affinities.Set(a, 1);
                            card.flash();
                        }
                    });
        });
    }
}