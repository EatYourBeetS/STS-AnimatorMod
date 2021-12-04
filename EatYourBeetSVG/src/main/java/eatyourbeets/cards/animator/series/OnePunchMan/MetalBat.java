package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnEndOfTurnSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.PowerHelper;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class MetalBat extends AnimatorCard implements OnEndOfTurnSubscriber
{
    public static final EYBCardData DATA = Register(MetalBat.class)
            .SetAttack(0, CardRarity.COMMON)
            .SetSeriesFromClassPackage();
    protected int gainedForce;

    public MetalBat()
    {
        super(DATA);

        Initialize(1, 0, 1, 2);
        SetUpgrade(1, 0, 1, 0);

        SetAffinity_Red(2, 0, 1);
        SetAffinity_Light(1);

        SetAffinityRequirement(Affinity.Red, 4);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_LIGHT);
        GameActions.Bottom.StackAffinityPower(Affinity.Red, magicNumber);
        CombatStats.onEndOfTurn.Subscribe(this);

        if (TrySpendAffinity(Affinity.Red)) {
            GameActions.Bottom.StackPower(TargetHelper.Player(), PowerHelper.TemporaryStrength, secondaryValue);
        }
    }

    @Override
    public void OnEndOfTurn(boolean isPlayer) {
        GameActions.Bottom.StackAffinityPower(Affinity.Red, -magicNumber);
        CombatStats.onEndOfTurn.Unsubscribe(this);
    }
}