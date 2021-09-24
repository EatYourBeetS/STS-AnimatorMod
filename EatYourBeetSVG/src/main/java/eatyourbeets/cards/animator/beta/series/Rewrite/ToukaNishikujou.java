package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;

public class ToukaNishikujou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ToukaNishikujou.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None).SetSeriesFromClassPackage();

    public ToukaNishikujou()
    {
        super(DATA);

        Initialize(0, 5, 1, 1);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Light(1, 1, 0);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.Cycle(name, secondaryValue)
        .SetFilter(c -> c.cardID.equals(ThrowingKnife.DATA.ID));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.CreateThrowingKnives(magicNumber).AddCallback(card -> {
            if (card != null) {
                if (GetHandAffinity(Affinity.Light) > GetHandAffinity(Affinity.Orange)) {
                    GameActions.Bottom.IncreaseScaling(card, Affinity.Light, GetHandAffinity(Affinity.Light));
                }
                else {
                    GameActions.Bottom.IncreaseScaling(card, Affinity.Orange, GetHandAffinity(Affinity.Orange));
                }
            }

            if (info.IsSynergizing)
            {
                GameActions.Bottom.Cycle(name, secondaryValue)
                        .SetFilter(c -> c.cardID.equals(ThrowingKnife.DATA.ID));
            }
        });
    }
}