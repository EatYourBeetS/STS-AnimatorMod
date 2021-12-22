package pinacolada.cards.pcl.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnEndOfTurnSubscriber;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPowerHelper;
import pinacolada.utilities.PCLActions;

public class MetalBat extends PCLCard implements OnEndOfTurnSubscriber
{
    public static final PCLCardData DATA = Register(MetalBat.class)
            .SetAttack(0, CardRarity.COMMON)
            .SetSeriesFromClassPackage();
    protected int gainedForce;

    public MetalBat()
    {
        super(DATA);

        Initialize(1, 0, 4, 2);
        SetUpgrade(2, 0, 1, 0);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Light(1);

        SetAffinityRequirement(PCLAffinity.Red, 4);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_LIGHT);
        PCLActions.Bottom.StackAffinityPower(PCLAffinity.Red, magicNumber);
        PCLCombatStats.onEndOfTurn.Subscribe(this);

        if (TrySpendAffinity(PCLAffinity.Red)) {
            PCLActions.Bottom.StackPower(TargetHelper.Player(), PCLPowerHelper.TemporaryStrength, secondaryValue);
        }
    }

    @Override
    public void OnEndOfTurn(boolean isPlayer) {
        PCLActions.Bottom.StackAffinityPower(PCLAffinity.Red, -magicNumber);
        PCLCombatStats.onEndOfTurn.Unsubscribe(this);
    }
}