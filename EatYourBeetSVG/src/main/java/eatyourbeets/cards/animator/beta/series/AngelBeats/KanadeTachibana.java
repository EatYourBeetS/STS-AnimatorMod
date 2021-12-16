package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class KanadeTachibana extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KanadeTachibana.class).SetSkill(1, CardRarity.RARE, EYBCardTarget.None).SetSeriesFromClassPackage();

    public KanadeTachibana()
    {
        super(DATA);

        Initialize(0, 1, 2, 2);
        SetUpgrade(0, 0, 1, 1);

        SetPurge(true);
        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Light(2, 0, 2);
        SetAffinity_Green(0,0,1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainInvocation(secondaryValue);
        GameActions.Bottom.FetchFromPile(name, magicNumber, p.discardPile)
        .SetOptions(false, true)
        .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0]).AddCallback(
                cards -> {
                    boolean canGiveAfterlife = info.IsSynergizing && CombatStats.TryActivateLimited(cardID);

                    for (AbstractCard c : cards) {
                        AnimatorCard card = JUtils.SafeCast(c, AnimatorCard.class);
                        if (card != null && GameUtilities.GetAffinityLevel(card, Affinity.Light, true) < 2)
                        {
                            card.affinities.Add(Affinity.Light, 1);
                            card.flash();

                            if (canGiveAfterlife) {
                                card.SetAfterlife(true, true);
                            }
                        }
                    }
                }
        );
    }
}