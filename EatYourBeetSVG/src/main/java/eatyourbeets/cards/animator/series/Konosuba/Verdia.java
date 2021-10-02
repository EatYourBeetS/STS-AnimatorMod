package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
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

        Initialize(1, 17, 1);
        SetUpgrade(1, 3, 0);

        SetAffinity_Red(2, 0, 1);
        SetAffinity_Dark(2, 0, 1);

        SetAffinityRequirement(Affinity.Red, 3);
        SetAffinityRequirement(Affinity.Dark, 3);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        CardGroup[] groups = upgraded ? new CardGroup[]{player.hand, player.drawPile, player.discardPile} : new CardGroup[]{player.hand};

        GameActions.Bottom.SelectFromPile(name, 1, groups)
                .SetOptions(false, false)
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