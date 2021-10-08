package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.affinity.AgilityPower;
import eatyourbeets.powers.affinity.ForcePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.TargetHelper;

public class MayuriKurotsuchi extends AnimatorCard {
    public static final EYBCardData DATA = Register(MayuriKurotsuchi.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.Normal).SetSeriesFromClassPackage();

    public MayuriKurotsuchi() {
        super(DATA);

        Initialize(0, 0, 2, 4);
        SetUpgrade(0, 0, 2, 1);

        SetAffinity_Orange(2, 0, 0);
        SetAffinity_Blue(1, 0, 0);
    }

    @Override
    public void Refresh(AbstractMonster enemy) {
        super.Refresh(enemy);

        int force = GameUtilities.GetPowerAmount(player, ForcePower.POWER_ID);
        int agility = GameUtilities.GetPowerAmount(player, AgilityPower.POWER_ID);

        GameUtilities.IncreaseMagicNumber(this, force + agility, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.ApplyPoison(TargetHelper.Normal(m), magicNumber)
                .AddCallback(m, (enemy, cards) -> {
                    int poisonThreshold = 30;

                    if (GameUtilities.GetPowerAmount(enemy, PoisonPower.POWER_ID) >= poisonThreshold) {
                        GameActions.Bottom.StackPower(TargetHelper.Normal(m), GameUtilities.GetRandomElement(GameUtilities.GetCommonDebuffs()), secondaryValue)
                                .ShowEffect(false, true);
                    }
                });
    }
}