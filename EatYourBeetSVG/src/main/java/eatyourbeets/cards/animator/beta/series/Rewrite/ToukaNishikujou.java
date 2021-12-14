package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
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
        SetAffinity_Light(1, 0, 0);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActions.Bottom.Cycle(name, secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.CreateThrowingKnives(magicNumber).AddCallback(card -> {
            if (card != null) {
                GameActions.Bottom.IncreaseScaling(card, CombatStats.Affinities.AffinityMeter.GetCurrentAffinity(), secondaryValue);
            }
        });
    }
}