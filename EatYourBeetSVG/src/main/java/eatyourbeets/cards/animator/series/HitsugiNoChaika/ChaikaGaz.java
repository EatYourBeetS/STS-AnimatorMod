package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ChaikaGaz extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ChaikaGaz.class)
            .SetSkill(0, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .PostInitialize(data ->
            {
                data.AddPreview(AffinityToken.GetCard(Affinity.Blue), false);
                data.AddPreview(AffinityToken.GetCard(Affinity.Dark), false);
            });
    public static final int TEMP_HP_AMOUNT = 2;

    public ChaikaGaz()
    {
        super(DATA);

        Initialize(0, 0, 1, 4);
        SetUpgrade(0, 0, 1);

        SetAffinity_Blue(1);
        SetAffinity_Dark(1);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, TEMP_HP_AMOUNT);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryHP(TEMP_HP_AMOUNT);
        GameActions.Bottom.GainCorruption(magicNumber, false);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Callback(() ->
        {
           if (CheckSpecialCondition(true))
           {
               GameActions.Bottom.ChannelOrb(new Dark());
               GameActions.Bottom.Exhaust(this);
           }
        });
    }

    @Override
    public void triggerOnAffinitySeal(boolean reshuffle)
    {
        super.triggerOnAffinitySeal(reshuffle);
        final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        group.group.add(AffinityToken.GetCopy(Affinity.Blue, false));
        group.group.add(AffinityToken.GetCopy(Affinity.Dark, false));
        GameActions.Bottom.SelectFromPile(name, 1, group)
        .SetOptions(false, false)
        .AddCallback(cards2 ->
        {
            for (AbstractCard c : cards2)
            {
                GameActions.Bottom.MakeCardInHand(c);
            }
        });
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return GameUtilities.GetPowerAmount(Affinity.Dark) >= (tryUse ? secondaryValue : (secondaryValue - magicNumber));
    }
}