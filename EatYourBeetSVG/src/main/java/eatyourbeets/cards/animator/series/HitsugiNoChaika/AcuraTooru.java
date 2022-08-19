package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.AcuraTooru_Dragoon;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.ui.common.EYBCardPopupActions;
import eatyourbeets.utilities.GameActions;

public class AcuraTooru extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AcuraTooru.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeries(CardSeries.HitsugiNoChaika)
            .PostInitialize(data ->
            {
                data.AddPopupAction(new EYBCardPopupActions.HitsugiNoChaika_Tooru(6, Fredrika.DATA, AcuraTooru_Dragoon.DATA));
                data.AddPreview(new AcuraTooru_Dragoon(), true);
                for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
                {
                    data.AddPreview(knife, false);
                }
            });

    public AcuraTooru()
    {
        super(DATA);

        Initialize(0, 5, 0, 1);
        SetUpgrade(0, 0, 0, 1);

        SetAffinity_Green(1);
        SetAffinity_Red(1);
    }

    @Override
    public void triggerOnAffinitySeal(boolean reshuffle)
    {
        super.triggerOnAffinitySeal(reshuffle);

        if (CombatStats.TryActivateLimited(cardID))
        {
            final CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            group.group.add(AffinityToken.GetCopy(Affinity.Red, false));
            group.group.add(AffinityToken.GetCopy(Affinity.Green, false));
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
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.CreateThrowingKnives(secondaryValue);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Callback(() ->
        {
            for (AbstractCard c : player.hand.group)
            {
                if (ThrowingKnife.DATA.IsCard(c) && c.canUpgrade())
                {
                    c.upgrade();
                    c.flash();
                }
            }
        });
    }
}