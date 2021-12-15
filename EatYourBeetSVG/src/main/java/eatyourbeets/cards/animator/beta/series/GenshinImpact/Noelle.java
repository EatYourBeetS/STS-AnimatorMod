package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Noelle extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Noelle.class).SetSkill(1, CardRarity.COMMON, EYBCardTarget.Self).SetSeriesFromClassPackage();

    public Noelle()
    {
        super(DATA);

        Initialize(0, 5, 1);
        SetUpgrade(0, 3, 0);
        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Light(1, 0, 0);

        SetAffinityRequirement(Affinity.Red, 3);
        SetAffinityRequirement(Affinity.Orange, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);

        if (IsStarter())
        {
            GameActions.Bottom.TriggerOrbPassive(1)
                    .SetFilter(o -> Earth.ORB_ID.equals(o.ID))
                    .AddCallback(orbs ->
                    {
                        if (orbs.size() <= 0)
                        {
                            GameActions.Bottom.ChannelOrb(new Earth());
                        }
                    });
        }

        if (player.hand.size() > 0) {
            GameActions.Bottom.TryChooseSpendAffinity(this, Affinity.Red,Affinity.Orange).AddConditionalCallback(() ->
            {
                GameActions.Bottom.SelectFromHand(name, magicNumber, false)
                        .SetOptions(true, true, true)
                        .SetMessage(RetainCardsAction.TEXT[0])
                        .AddCallback(cards ->
                        {
                            if (cards.size() > 0)
                            {
                                AbstractCard card = cards.get(0);
                                GameUtilities.Retain(card);
                            }
                        });
            });
        }
    }
}