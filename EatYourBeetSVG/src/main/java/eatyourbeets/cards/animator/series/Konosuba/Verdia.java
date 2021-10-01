package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Verdia extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Verdia.class)
            .SetAttack(3, CardRarity.UNCOMMON)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public Verdia()
    {
        super(DATA);

        Initialize(1, 16, 2, 2);
        SetUpgrade(0, 0, 1, 1);

        SetAffinity_Red(2);
        SetAffinity_Dark(2, 0, 1);

        SetAffinityRequirement(Affinity.Red, 3);
        SetAffinityRequirement(Affinity.Dark, 3);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.SelectFromHand(name, 1, false)
                .SetOptions(false, false, false)
                .SetMessage(GR.Common.Strings.HandSelection.GenericBuff)
                .SetFilter(c -> GameUtilities.IsSameSeries(this, c))
                .AddCallback(cards ->
                {
                    for (AbstractCard c : cards)
                    {
                        GameActions.Bottom.IncreaseScaling(c, Affinity.Red, affinities.GetScaling(Affinity.Red,true));
                        GameActions.Bottom.IncreaseScaling(c, Affinity.Dark, affinities.GetScaling(Affinity.Dark,true));
                        c.flash();
                    }
                });
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this,m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActions.Bottom.GainBlock(block);
        if (CheckAffinity(Affinity.Red)) {
            GameActions.Bottom.IncreaseScaling(this, Affinity.Red, 1);
        }
        if (CheckAffinity(Affinity.Dark)) {
            GameActions.Bottom.IncreaseScaling(this, Affinity.Dark, 1);
        }
    }
}