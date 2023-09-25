package eatyourbeets.cards.animator.series.Overlord;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Entoma extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Entoma.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Piercing)
            .SetSeriesFromClassPackage();

    public Entoma()
    {
        super(DATA);

        Initialize(6, 0, 2);
        SetUpgrade(0, 0, 2);

        SetAffinity_Dark(2, 0, 1);

        SetAffinityRequirement(Affinity.Dark, 3);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.POISON)
        .SetDamageEffect(e -> GameEffects.List.Add(VFX.Bite(e.hb, Color.GREEN)).duration);
        GameActions.Bottom.GainAffinity(Affinity.Dark);
        GameActions.Bottom.ApplyPoison(p, m, magicNumber);

        if (!m.hasPower(EntomaPower.DeriveID(cardID)) && CheckSpecialCondition(true))
        {
            GameActions.Bottom.ApplyPower(new EntomaPower(m, p, 1));
        }
    }

    public static class EntomaPower extends AnimatorPower
    {
        public EntomaPower(AbstractCreature owner, AbstractCreature source, int amount)
        {
            super(owner, source, Entoma.DATA);

            this.maxAmount = 1;

            Initialize(amount, PowerType.DEBUFF, false);
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0);
        }

        @Override
        public void wasHPLost(DamageInfo info, int damageAmount)
        {
            if (AbstractDungeon.actionManager.currentAction instanceof PoisonLoseHpAction)
            {
                final AbstractPower p = GameUtilities.GetPower(owner, PoisonPower.POWER_ID);
                if (p != null)
                {
                    p.amount += 1;
                }

                flashWithoutSound();
            }

            super.wasHPLost(info, damageAmount);
        }
    }
}