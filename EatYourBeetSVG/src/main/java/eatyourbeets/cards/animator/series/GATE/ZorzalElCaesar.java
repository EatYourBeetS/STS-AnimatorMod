package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class ZorzalElCaesar extends AnimatorCard {
    public static final EYBCardData DATA = Register(ZorzalElCaesar.class)
            .SetAttack(-1, CardRarity.RARE, EYBAttackType.Normal, EYBCardTarget.Random)
            .SetSeriesFromClassPackage();

    public ZorzalElCaesar() {
        super(DATA);

        Initialize(5, 0, 5, 2);
        SetUpgrade(0, 0, 3, 1);

        SetAffinity_Earth(2);
        SetAffinity_Poison(2);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(GameUtilities.GetXCostEnergy(this));
    }

    @Override
    public void triggerOnExhaust() {
        super.triggerOnExhaust();

        GameActions.Bottom.DrawNextTurn(secondaryValue);
        GameActions.Bottom.GainEnergyNextTurn(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        int stacks = GameUtilities.UseXCostEnergy(this);

        for (int i=0; i<stacks; i++) {
            GameActions.Bottom.DealDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                    .AddCallback(enemy -> {
                        for (AbstractPower power : enemy.powers)
                        {
                            if (power != null && power.type == AbstractPower.PowerType.DEBUFF)
                            {
                                GameActions.Bottom.RaiseEarthLevel(magicNumber);
                            }
                        }
                    });
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
    }
}