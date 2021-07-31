package eatyourbeets.cards.animator.beta.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.powers.LockOnPower;
import com.megacrit.cardcrawl.vfx.combat.ClawEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.powers.animator.NegateBlockPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Kokomi extends AnimatorCard implements Hidden
{
    public static final EYBCardData DATA = Register(Kokomi.class).SetAttack(2, CardRarity.SPECIAL, EYBAttackType.Ranged).SetSeries(CardSeries.GenshinImpact);
    private static final int NO_BLOCK_TURNS = 2;

    public Kokomi()
    {
        super(DATA);

        Initialize(2, 0, 30, 2);
        SetUpgrade(1, 0, 10);
        SetAffinity_Blue(2, 0, 1);
        SetAffinity_Orange(1, 0, 0);

        SetExhaust(true);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (enemy != null && GameUtilities.GetPowerAmount(enemy, LockOnPower.POWER_ID) >= 1)
        {
            amount += magicNumber;
        }

        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (GameUtilities.GetPowerAmount(m, LockOnPower.POWER_ID) >= 1) {
            GameActions.Bottom.StackPower(new NegateBlockPower(p, NO_BLOCK_TURNS));
            GameActions.Bottom.VFX(new ClawEffect(m.hb.cX, m.hb.cY, Color.TEAL, Color.WHITE));
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        }
        else {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
            GameActions.Bottom.InduceOrbs(Frost::new, secondaryValue);
        }


    }
}