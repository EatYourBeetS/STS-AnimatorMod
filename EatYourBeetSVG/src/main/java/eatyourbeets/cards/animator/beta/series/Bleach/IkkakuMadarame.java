package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.IkkakuBankai;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.affinity.AgilityPower;
import eatyourbeets.powers.affinity.ForcePower;
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

        Initialize(6, 1, 2, 16);
        SetUpgrade(3, 0, 0);
        SetAffinity_Red(2, 0, 1);
        SetAffinity_Green(1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.SLASH_HORIZONTAL);
        GameActions.Bottom.GainBlock(block);

        if (GameUtilities.InStance(ForceStance.STANCE_ID)) {
            GameActions.Bottom.GainAgility(magicNumber, true);
        }
        else {
            GameActions.Bottom.GainForce(magicNumber, true);
        }

        GameActions.Bottom.Callback(card -> {
            if (GameUtilities.GetPowerAmount(player, ForcePower.POWER_ID) > secondaryValue || GameUtilities.GetPowerAmount(player, AgilityPower.POWER_ID) > secondaryValue) {
                GameActions.Bottom.MakeCardInDrawPile(new IkkakuBankai());
                GameActions.Last.ModifyAllInstances(uuid).AddCallback(GameActions.Bottom::Exhaust);
            }
        });
    }
}