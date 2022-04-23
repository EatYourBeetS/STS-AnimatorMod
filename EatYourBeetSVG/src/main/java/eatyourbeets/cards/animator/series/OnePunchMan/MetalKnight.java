package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import eatyourbeets.actions.orbs.RemoveOrb;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MetalKnight extends AnimatorCard
{
    public static final EYBCardData DATA = Register(MetalKnight.class)
            .SetAttack(3, CardRarity.UNCOMMON, EYBAttackType.Ranged)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public MetalKnight()
    {
        super(DATA);

        Initialize(14, 2, 3);

        SetAffinity_Red(1, 1, 0);
        SetAffinity_Blue(1);
        SetAffinity_Dark(1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.VFX(new WeightyImpactEffect(m.hb.cX, m.hb.cY), 0.6f, true);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_HEAVY);
        GameActions.Bottom.StackPower(new MetalKnightPower(p, 1));

        if (magicNumber > 0)
        {
            GameActions.Bottom.GainMetallicize(magicNumber);
            GameActions.Bottom.ModifyAllCopies(cardID, c -> GameUtilities.DecreaseMagicNumber(c, 1, false));
        }
    }

    public static class MetalKnightPower extends AnimatorClickablePower
    {
        public MetalKnightPower(AbstractCreature owner, int amount)
        {
            super(owner, MetalKnight.DATA, PowerTriggerConditionType.Energy, 1);

            triggerCondition.SetUses(amount, false, true).SetCondition(__ -> GameUtilities.HasOrb(Lightning.ORB_ID));

            Initialize(amount);
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            super.OnUse(m);

            GameActions.Bottom.Callback(() ->
            {
                final AbstractOrb orb = GameUtilities.GetFirstOrb(Lightning.ORB_ID);
                if (orb != null)
                {
                    GameActions.Top.ChannelOrb(new Plasma());
                    GameActions.Top.Add(new RemoveOrb(orb));
                }
            });
            ReducePower(1);
        }
    }
}