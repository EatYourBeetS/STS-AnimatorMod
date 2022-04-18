package eatyourbeets.cards_beta.special;

import com.badlogic.gdx.graphics.Color;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.ViolentAttackEffect;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.utilities.GameActions;

public class KanamiAlt extends AnimatorCard_UltraRare implements Hidden
{
    public static final EYBCardData DATA = Register(KanamiAlt.class)
            .SetAttack(2, CardRarity.SPECIAL, EYBAttackType.Normal)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.LogHorizon);

    public KanamiAlt()
    {
        super(DATA);

        Initialize(20, 2, 10);
        SetUpgrade(7, 0, 0);

        SetAffinity_Red(2, 0, 2);
        SetAffinity_Green(1);
        SetAffinity_Light(1);
    }

    @Override
    protected float ModifyBlock(AbstractMonster enemy, float amount)
    {
        if (enemy != null && enemy.hasPower(VulnerablePower.POWER_ID))
        {
            amount += magicNumber;
        }

        return super.ModifyBlock(enemy, amount);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (enemy != null)
        {
            if (enemy.type == AbstractMonster.EnemyType.ELITE)
            {
                amount *= 2;
            }
            if (enemy.type == AbstractMonster.EnemyType.BOSS)
            {
                amount *= 3;
            }
        }

        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.VFX(new ViolentAttackEffect(m.hb.cX, m.hb.cY, Color.RED.cpy()));
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE)
        .AddCallback(block, (amount, __) ->
        {
            if (amount > 0)
            {
                GameActions.Bottom.GainForce(1);
                GameActions.Bottom.GainAgility(1);
                GameActions.Bottom.GainBlock(amount);
            }
        });
    }
}