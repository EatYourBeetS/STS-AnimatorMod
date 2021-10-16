package eatyourbeets.cards.animator.beta.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.misc.GenericEffects.GenericEffect;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.TargetHelper;

public class GinIchimaru extends AnimatorCard
{
    public static final EYBCardData DATA = Register(GinIchimaru.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Piercing, EYBCardTarget.Random).SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();
    public static final int MAX_AMOUNT = 5;

    public GinIchimaru()
    {
        super(DATA);

        Initialize(3, 0, 2, 3);
        SetUpgrade(1, 0, 0);
        SetAffinity_Dark(1, 0, 0);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Green(2, 0, 2);
        SetAffinity_Blue(0,0,1);

        SetAffinityRequirement(Affinity.Blue, 3);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.VFX(new DieDieDieEffect());
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE);
        }

        if (CombatStats.Affinities.GetAffinityLevel(Affinity.Red,true) >= secondaryValue || CombatStats.Affinities.GetAffinityLevel(Affinity.Green,true) >= secondaryValue)
        {
            GameActions.Bottom.Exhaust(this);
        }

        if (TrySpendAffinity(Affinity.Blue))
        {
            GameActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), magicNumber);
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.Callback(() -> makeChoice(m, 1));
    }

    private void makeChoice(AbstractMonster m, int selections) {
        if (choices.TryInitialize(this))
        {
            int amountRed = Math.min(MAX_AMOUNT, CombatStats.Affinities.GetAffinityLevel(Affinity.Red,true));
            int amountGreen = Math.min(MAX_AMOUNT, CombatStats.Affinities.GetAffinityLevel(Affinity.Red,true));
            if (amountRed > 0) {
                choices.AddEffect(new GenericEffect_Gin(Affinity.Red, amountRed));
            }
            if (amountGreen > 0) {
                choices.AddEffect(new GenericEffect_Gin(Affinity.Green, amountGreen));
            }
        }
        choices.Select(selections, m);
    }

    protected static class GenericEffect_Gin extends GenericEffect
    {
        protected final Affinity affinity;

        public GenericEffect_Gin(Affinity affinity, int amount)
        {
            this.affinity = affinity;
            this.amount = amount;
        }

        @Override
        public String GetText()
        {
            return GR.Animator.Strings.Actions.PayCost(amount, affinity.GetTooltip(), true) + "NL" + GR.Animator.Strings.Actions.GainAmount(amount, affinity.equals(Affinity.Green) ? GR.Tooltips.Agility : GR.Tooltips.Force, true);
        }

        @Override
        public void Use(AnimatorCard card, AbstractPlayer p, AbstractMonster m)
        {
            if (CombatStats.Affinities.TrySpendAffinity(affinity,amount,true)) {
                GameActions.Bottom.StackAffinityPower(affinity,amount,false);
            }
        }
    }
}