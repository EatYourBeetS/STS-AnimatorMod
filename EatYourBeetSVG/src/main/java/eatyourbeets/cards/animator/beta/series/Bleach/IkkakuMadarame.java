package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.IkkakuBankai;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class IkkakuMadarame extends AnimatorCard{
    public static final EYBCardData DATA = Register(IkkakuMadarame.class).SetAttack(2, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.ALL).SetSeriesFromClassPackage()
            .PostInitialize(data -> {
                data.AddPreview(new ZarakiKenpachi(), false);
                data.AddPreview(new IkkakuBankai(), false);
            });

    public IkkakuMadarame() {
        super(DATA);

        Initialize(6, 1, 2, 5);
        SetUpgrade(3, 0, 0);
        SetAffinity_Red(2, 0, 1);
        SetAffinity_Green(0, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.DealCardDamageToAll(this, AttackEffects.SLASH_HORIZONTAL);
        GameActions.Bottom.GainBlock(block);

        GameUtilities.MaintainPower(Affinity.Green);

        if (CheckSpecialCondition(true)) {
            GameActions.Bottom.MakeCardInDrawPile(new IkkakuBankai());
            GameActions.Last.ModifyAllInstances(uuid).AddCallback(GameActions.Bottom::Exhaust);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse){
        return CombatStats.Affinities.GetPowerAmount(Affinity.Red) >= secondaryValue || CombatStats.Affinities.GetPowerAmount(Affinity.Green) >= secondaryValue;
    }
}