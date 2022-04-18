package eatyourbeets.cards.animator.series.HitsugiNoChaika;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.AcuraTooru_Dragoon;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.*;
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

        SetAffinity_Green(2);
        SetAffinity_Red(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.CreateThrowingKnives(secondaryValue);

        if (info.IsSynergizing && info.TryActivateLimited())
        {
            GameActions.Bottom.ObtainAffinityToken(Affinity.Green, false);
        }
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