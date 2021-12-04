package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class MayuriKurotsuchi extends AnimatorCard {
    public static final EYBCardData DATA = Register(MayuriKurotsuchi.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.Normal).SetSeriesFromClassPackage();
    public static final int POISON_THRESHOLD = 20;

    public MayuriKurotsuchi() {
        super(DATA);

        Initialize(0, 1, 2, 3);
        SetUpgrade(0, 0, 1, 1);

        SetAffinity_Silver(1, 0, 0);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Blue(0,0,1);
        SetAffinity_Dark(1);
    }

    @Override
    public int GetXValue() {
        return magicNumber * CombatStats.Affinities.GetPowerAmount(Affinity.Green);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.ApplyPoison(TargetHelper.Normal(m), GetXValue())
                .AddCallback(m, (enemy, cards) -> {
                    if (GameUtilities.GetPowerAmount(enemy, PoisonPower.POWER_ID) >= POISON_THRESHOLD) {
                        GameActions.Bottom.StackPower(TargetHelper.Normal(m), GameUtilities.GetRandomElement(GameUtilities.GetCommonDebuffs()), secondaryValue)
                                .ShowEffect(false, true);
                    }
                });
    }
}