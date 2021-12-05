package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.animator.BurningWeaponPower;
import eatyourbeets.powers.common.FreezingPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Diluc extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Diluc.class).SetAttack(3, CardRarity.UNCOMMON, EYBAttackType.Normal, EYBCardTarget.ALL).SetSeriesFromClassPackage(true);

    public Diluc()
    {
        super(DATA);

        Initialize(14, 0, 5, 3);
        SetUpgrade(2,0,1,0);
        SetAffinity_Red(2, 0, 3);

        SetAffinityRequirement(Affinity.Red, 7);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (enemy != null)
        {
            return super.ModifyDamage(enemy, amount + GameUtilities.GetPowerAmount(enemy, FreezingPower.POWER_ID) * magicNumber);
        }
        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.SLASH_HEAVY).forEach(d -> d.SetVFXColor(Color.FIREBRICK));

        for (AbstractCreature c : GameUtilities.GetEnemies(true)) {
            GameActions.Bottom.RemovePower(p, c, FreezingPower.POWER_ID);
        }

        if (TrySpendAffinity(Affinity.Red)) {
            GameActions.Bottom.StackPower(new BurningWeaponPower(p, secondaryValue));
        }
    }
}

