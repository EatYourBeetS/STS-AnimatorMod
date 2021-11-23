package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class ToukaNishikujou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ToukaNishikujou.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None).SetSeriesFromClassPackage();

    public ToukaNishikujou()
    {
        super(DATA);

        Initialize(0, 6, 1, 1);
        SetUpgrade(0, 0, 1, 0);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Light(1, 1, 0);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.GainEndurance(secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.CreateThrowingKnives(magicNumber).AddCallback(card -> {
            if (card != null) {
                if (CheckPrimaryCondition(false)) {
                    GameActions.Bottom.IncreaseScaling(card, Affinity.Light, CombatStats.Affinities.GetAffinityLevel(Affinity.Light, true));
                }
                else {
                    GameActions.Bottom.IncreaseScaling(card, Affinity.Orange, CombatStats.Affinities.GetAffinityLevel(Affinity.Orange, true));
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
        return CombatStats.Affinities.GetAffinityLevel(Affinity.Light, true) > CombatStats.Affinities.GetAffinityLevel(Affinity.Orange, true);
    }
}