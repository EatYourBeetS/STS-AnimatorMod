package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ToukaNishikujou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ToukaNishikujou.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None).SetSeriesFromClassPackage();

    public ToukaNishikujou()
    {
        super(DATA);

        Initialize(0, 5, 1, 1);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Air(1, 0, 0);
        SetAffinity_Earth(1, 0, 0);
        SetAffinity_Light(1, 1, 0);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameUtilities.MaintainPower(Affinity.Earth);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.CreateThrowingKnives(magicNumber).AddCallback(card -> {
            if (card != null) {
                if (CheckPrimaryCondition(false)) {
                    GameActions.Bottom.IncreaseScaling(card, Affinity.Light, GetHandAffinity(Affinity.Light));
                }
                else {
                    GameActions.Bottom.IncreaseScaling(card, Affinity.Earth, GetHandAffinity(Affinity.Earth));
                }
            }

            if (info.IsSynergizing)
            {
                GameActions.Bottom.Cycle(name, secondaryValue)
                        .SetFilter(c -> c.cardID.equals(ThrowingKnife.DATA.ID));
            }
        });
    }

    @Override
    public boolean CheckPrimaryCondition(boolean tryUse)
    {
        return GetHandAffinity(Affinity.Light) > GetHandAffinity(Affinity.Earth);
    }
}