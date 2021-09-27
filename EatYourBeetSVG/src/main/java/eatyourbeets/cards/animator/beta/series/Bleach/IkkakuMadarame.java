package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.beta.special.IkkakuBankai;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnEndOfTurnSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.affinity.AgilityPower;
import eatyourbeets.powers.affinity.ForcePower;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class IkkakuMadarame extends AnimatorCard  implements OnEndOfTurnSubscriber {
    public static final EYBCardData DATA = Register(IkkakuMadarame.class).SetAttack(2, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.ALL).SetSeriesFromClassPackage()
            .PostInitialize(data -> {
                data.AddPreview(new ZarakiKenpachi(), false);
                data.AddPreview(new IkkakuBankai(), false);
            });

    public IkkakuMadarame() {
        super(DATA);

        Initialize(4, 0, 0, 4);
        SetUpgrade(3, 0, 0);
        SetAffinity_Red(2, 0, 2);
        SetAffinity_Green(1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.SLASH_HORIZONTAL);

        if (GameUtilities.InStance(ForceStance.STANCE_ID)) {
            CombatStats.onEndOfTurn.Subscribe(this);
            CombatStats.Affinities.Agility.SetEnabled(true);
        }

        GameActions.Bottom.Callback(card -> {
            if (GameUtilities.GetPowerAmount(player, ForcePower.POWER_ID) > secondaryValue || GameUtilities.GetPowerAmount(player, AgilityPower.POWER_ID) > secondaryValue) {
                GameActions.Bottom.MakeCardInDrawPile(new IkkakuBankai());
                GameActions.Last.ModifyAllInstances(uuid).AddCallback(GameActions.Bottom::Exhaust);
            }
        });
    }

    @Override
    public void OnEndOfTurn(boolean isPlayer) {
        CombatStats.onEndOfTurn.Unsubscribe(this);
        if (GameUtilities.InStance(ForceStance.STANCE_ID)) {
            CombatStats.Affinities.Agility.SetEnabled(false);
        }
    }
}