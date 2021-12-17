package pinacolada.cards.pcl.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.misc.GenericEffects.GenericEffect;
import pinacolada.resources.GR;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class GinIchimaru extends PCLCard
{
    public static final PCLCardData DATA = Register(GinIchimaru.class).SetAttack(1, CardRarity.UNCOMMON, PCLAttackType.Piercing, eatyourbeets.cards.base.EYBCardTarget.Random).SetSeriesFromClassPackage();

    private static final CardEffectChoice choices = new CardEffectChoice();
    public static final int MAX_AMOUNT = 10;

    public GinIchimaru()
    {
        super(DATA);

        Initialize(4, 0, 2, 0);
        SetUpgrade(0, 0, 0, 0);
        SetAffinity_Dark(1, 0, 0);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Blue(0,0,1);

        SetAffinityRequirement(PCLAffinity.Red, 2);
        SetAffinityRequirement(PCLAffinity.Green, 2);
        SetHitCount(2, 1);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            PCLActions.Bottom.ApplyVulnerable(TargetHelper.Enemies(), magicNumber);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.VFX(new DieDieDieEffect());
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.NONE);
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.Callback(() -> makeChoice(m, 1));
    }

    private void makeChoice(AbstractMonster m, int selections) {
        if (choices.TryInitialize(this))
        {
            if (CheckAffinity(PCLAffinity.Red)) {
                choices.AddEffect(new GenericEffect_Gin(PCLAffinity.Red, affinities.GetRequirement(PCLAffinity.Red)));
            }
            if (CheckAffinity(PCLAffinity.Green)) {
                choices.AddEffect(new GenericEffect_Gin(PCLAffinity.Green,  affinities.GetRequirement(PCLAffinity.Green)));
            }
        }
        choices.Select(selections, m);
    }

    protected static class GenericEffect_Gin extends GenericEffect
    {
        protected final PCLAffinity affinity;

        public GenericEffect_Gin(PCLAffinity affinity, int amount)
        {
            this.affinity = affinity;
            this.amount = amount;
        }

        @Override
        public String GetText()
        {
            return GR.PCL.Strings.Actions.PayCost(amount, affinity.GetTooltip(), true) + "NL" + GR.PCL.Strings.Actions.GainAmount(amount, affinity.equals(PCLAffinity.Green) ? GR.Tooltips.Velocity : GR.Tooltips.Might, true);
        }

        @Override
        public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
        {
            if (PCLGameUtilities.TrySpendAffinity(affinity,amount,true)) {
                PCLActions.Bottom.StackAffinityPower(affinity,1,false);
            }
        }
    }
}