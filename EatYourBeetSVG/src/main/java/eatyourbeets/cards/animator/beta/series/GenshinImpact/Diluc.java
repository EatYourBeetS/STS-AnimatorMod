package eatyourbeets.cards.animator.beta.series.GenshinImpact;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
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

        Initialize(14, 0, 3, 3);
        SetUpgrade(2,0,1,0);
        SetAffinity_Red(1, 0, 3);

        SetAffinityRequirement(Affinity.Red, 7);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamageToAll(this, AttackEffects.SLASH_HEAVY).forEach(d -> d.SetVFXColor(Color.FIREBRICK));

        int amount = 0;
        for (AbstractCreature c : GameUtilities.GetEnemies(true)) {
            if (c.hasPower(FreezingPower.POWER_ID)) {
                GameActions.Bottom.RemovePower(p, c, FreezingPower.POWER_ID);
                amount += magicNumber;
            }
        }
        if (amount > 0) {
            GameActions.Bottom.StackPower(new NextTurnBlockPower(p, amount));
        }

        if (TrySpendAffinity(Affinity.Red)) {
            GameActions.Bottom.StackPower(new BurningWeaponPower(p, secondaryValue));
        }
    }
}

