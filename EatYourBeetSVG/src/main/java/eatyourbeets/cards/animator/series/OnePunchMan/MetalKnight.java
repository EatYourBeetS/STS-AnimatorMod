package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MetalKnight extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MetalKnight.class)
            .SetAttack(3, CardRarity.UNCOMMON, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public MetalKnight()
    {
        super(DATA);

        Initialize(13, 3, 9, 2);
        SetUpgrade(3, 0, 2, 1);

        SetAffinity_Red(2);
        SetAffinity_Blue(2);
        SetAffinity_Dark(1);

        SetEvokeOrbCount(1);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        if (GameUtilities.CanTriggerSupercharged()) {
            return super.ModifyDamage(enemy, amount + magicNumber);
        }
        return super.ModifyDamage(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.VFX(new WeightyImpactEffect(m.hb.cX, m.hb.cY), 0.6f, true);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_HEAVY);
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.ChannelOrbs(Plasma::new, 1);
        if (GameUtilities.CanTriggerSupercharged()) {
            GameActions.Bottom.GainMetallicize(secondaryValue);
        }
    }
}