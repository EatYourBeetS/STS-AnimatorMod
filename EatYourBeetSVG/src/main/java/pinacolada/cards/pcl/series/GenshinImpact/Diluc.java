package pinacolada.cards.pcl.series.GenshinImpact;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.common.FreezingPower;
import pinacolada.powers.special.BurningWeaponPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Diluc extends PCLCard
{
    public static final PCLCardData DATA = Register(Diluc.class).SetAttack(3, CardRarity.UNCOMMON, PCLAttackType.Normal, eatyourbeets.cards.base.EYBCardTarget.ALL).SetSeriesFromClassPackage(true);

    public Diluc()
    {
        super(DATA);

        Initialize(14, 0, 3, 3);
        SetUpgrade(2,0,1,0);
        SetAffinity_Red(1, 0, 3);

        SetAffinityRequirement(PCLAffinity.Red, 7);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.SLASH_HEAVY).forEach(d -> d.SetVFXColor(Color.FIREBRICK));

        int amount = 0;
        for (AbstractCreature c : PCLGameUtilities.GetEnemies(true)) {
            if (c.hasPower(FreezingPower.POWER_ID)) {
                PCLActions.Bottom.RemovePower(p, c, FreezingPower.POWER_ID);
                amount += magicNumber;
            }
        }
        if (amount > 0) {
            PCLActions.Bottom.StackPower(new NextTurnBlockPower(p, amount));
        }

        if (TrySpendAffinity(PCLAffinity.Red)) {
            PCLActions.Bottom.StackPower(new BurningWeaponPower(p, secondaryValue));
        }
    }
}

