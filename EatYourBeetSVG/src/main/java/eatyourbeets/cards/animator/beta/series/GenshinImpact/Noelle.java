package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
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
        SetUpgrade(0, 2, 0);
        SetAffinity_Red(1, 0, 0);
        SetAffinity_Light(1, 1, 0);
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

        if (info.IsSynergizing)
        {
            GameActions.Bottom.SelectFromHand(name, magicNumber, false)
                    .SetOptions(true, true, true)
                    .SetMessage(RetainCardsAction.TEXT[0])
                    .SetFilter(c -> !c.isEthereal && (GameUtilities.HasLightAffinity(c) || GameUtilities.HasRedAffinity(c)))
                    .AddCallback(cards ->
                    {
                        if (cards.size() > 0)
                        {
                            AbstractCard card = cards.get(0);
                            GameUtilities.Retain(card);
                        }
                    });
        }
    }
}