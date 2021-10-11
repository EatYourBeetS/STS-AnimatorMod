package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.IkkakuBankai;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.affinity.AirLevelPower;
import eatyourbeets.powers.affinity.FireLevelPower;
import eatyourbeets.stances.ForceStance;
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

        Initialize(4, 0, 0, 4);
        SetUpgrade(3, 0, 0);
        SetAffinity_Fire(2, 0, 2);
        SetAffinity_Air(1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.SLASH_HORIZONTAL);

        if (GameUtilities.InStance(ForceStance.STANCE_ID)) {
            GameUtilities.MaintainPower(Affinity.Air);
        }
        else {
            GameUtilities.MaintainPower(Affinity.Fire);
        }

        GameActions.Bottom.Callback(card -> {
            if (GameUtilities.GetPowerAmount(player, FireLevelPower.POWER_ID) > secondaryValue || GameUtilities.GetPowerAmount(player, AirLevelPower.POWER_ID) > secondaryValue) {
                GameActions.Bottom.MakeCardInDrawPile(new IkkakuBankai());
                GameActions.Last.ModifyAllInstances(uuid).AddCallback(GameActions.Bottom::Exhaust);
            }
        });
    }
}