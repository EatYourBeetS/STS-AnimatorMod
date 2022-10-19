package eatyourbeets.cards.animator.series.MadokaMagica;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Frost;
import eatyourbeets.actions.special.SelectCreature;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.AnimatorClickablePower;
import eatyourbeets.powers.PowerTriggerConditionType;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class YachiyoNanami extends AnimatorCard
{
    public static final EYBCardData DATA = Register(YachiyoNanami.class)
            .SetPower(2, CardRarity.UNCOMMON)
            .SetSeriesFromClassPackage();
    public static final int TEMP_HP_AMOUNT = 3;
    public static final int DAMAGE_AMOUNT = 7;

    public YachiyoNanami()
    {
        super(DATA);

        Initialize(0, 0, TEMP_HP_AMOUNT, DAMAGE_AMOUNT);
        SetCostUpgrade(-1);

        SetAffinity_Blue(2);
        SetAffinity_Light(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainOrbSlots(1);
        GameActions.Bottom.ChannelOrb(new Frost());
        GameActions.Bottom.StackPower(new YachiyoNanamiPower(p, 1));
    }

    public static class YachiyoNanamiPower extends AnimatorClickablePower
    {
        public YachiyoNanamiPower(AbstractPlayer owner, int amount)
        {
            super(owner, YachiyoNanami.DATA, PowerTriggerConditionType.Special, 0);

            triggerCondition.SetUses(1, true, true);
            triggerCondition.SetCondition(__ -> FindOrb() != null);

            Initialize(amount);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(0, TEMP_HP_AMOUNT, DAMAGE_AMOUNT);
        }

        @Override
        public void update(int slot)
        {
            final boolean wasHovered = hb.hovered;

            super.update(slot);

            if (clickable && enabled)
            {
                final AbstractOrb orb = FindOrb();
                if (orb != null)
                {
                    if (hb.justHovered)
                    {
                        orb.showEvokeValue();
                    }
                    else if (wasHovered && !hb.hovered)
                    {
                        orb.hideEvokeValues();
                    }
                }
            }
        }

        @Override
        public void OnUse(AbstractMonster m)
        {
            final AbstractOrb orb = FindOrb();
            if (orb == null)
            {
                return;
            }

            GameActions.Bottom.EvokeOrb(1, orb)
            .AddCallback(orbs ->
            {
               for (AbstractOrb o : orbs)
               {
                   if (Frost.ORB_ID.equals(o.ID))
                   {
                       GameActions.Bottom.SelectCreature(SelectCreature.Targeting.Enemy, name)
                       .AutoSelectSingleTarget(true)
                       .IsCancellable(false)
                       .AddCallback(target ->
                       {
                          GameActions.Bottom.DealDamage(owner, target, DAMAGE_AMOUNT, DamageInfo.DamageType.THORNS, AttackEffects.SPEAR).SetVFXColor(Color.SKY);
                          GameActions.Bottom.ApplyVulnerable(owner, target, 1);
                       });
                   }
                   else
                   {
                       GameActions.Bottom.GainTemporaryHP(TEMP_HP_AMOUNT);
                   }
               }
            });
        }

        protected AbstractOrb FindOrb()
        {
            for (int i = player.orbs.size() - 1; i >= 0; i--)
            {
                final AbstractOrb orb = player.orbs.get(i);
                if (GameUtilities.IsValidOrb(orb))
                {
                    return orb;
                }
            }

            return null;
        }
    }
}