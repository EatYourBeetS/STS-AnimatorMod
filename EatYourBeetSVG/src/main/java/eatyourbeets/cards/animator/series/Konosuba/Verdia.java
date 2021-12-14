package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
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

        Initialize(1, 20, 1);
        SetUpgrade(1, 1, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Dark(2, 0, 1);

        SetAffinityRequirement(Affinity.Red, 4);
        SetAffinityRequirement(Affinity.Dark, 4);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.SelectFromPile(name, 1, player.hand)
                .SetOptions(false, false)
                .SetMessage(DATA.Strings.EXTENDED_DESCRIPTION[0])
                .SetFilter(c -> (upgraded || GameUtilities.HasRedAffinity(c) || GameUtilities.HasDarkAffinity(c)) && (c.baseDamage >= 0 || c.baseBlock >= 0))
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
        GameActions.Bottom.DealCardDamage(this,m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActions.Bottom.GainBlock(block);
        if (TrySpendAffinity(Affinity.Red)) {
            GameActions.Bottom.IncreaseScaling(this, Affinity.Red, 1);
        }
        if (TrySpendAffinity(Affinity.Dark)) {
            GameActions.Bottom.IncreaseScaling(this, Affinity.Red, 1);
        }
    }
}