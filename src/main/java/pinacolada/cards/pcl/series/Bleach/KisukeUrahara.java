package pinacolada.cards.pcl.series.Bleach;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.RandomizedList;
import pinacolada.cards.base.*;
import pinacolada.cards.base.cardeffects.GenericEffects.GenericEffect_EnterStance;
import pinacolada.stances.PCLStanceHelper;
import pinacolada.utilities.PCLActions;

public class KisukeUrahara extends PCLCard
{
    public static final PCLCardData DATA = Register(KisukeUrahara.class)
            .SetSkill(1, CardRarity.COMMON, PCLCardTarget.None)
            .SetSeriesFromClassPackage(true);
    private static final CardEffectChoice choices = new CardEffectChoice();

    public KisukeUrahara()
    {
        super(DATA);

        Initialize(0, 4, 2, 2);
        SetUpgrade(0, 2, 0, 1);

        SetAffinity_Red(1, 0, 1);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(1, 0, 1);

        SetExhaust(true);

        SetAffinityRequirement(PCLAffinity.Orange, 4);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CheckAffinity(PCLAffinity.Orange) && CombatStats.TryActivateSemiLimited(cardID) && TrySpendAffinity(PCLAffinity.Orange)) {
            PCLActions.Bottom.MoveCard(this,player.hand).ShowEffect(true, true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        RandomizedList<PCLAffinity> affinityOptions = new RandomizedList<PCLAffinity>(PCLAffinity.Basic());
        PCLAffinity af1 = affinityOptions.Retrieve(rng, true);
        PCLAffinity af2 = affinityOptions.Retrieve(rng, true);
        PCLStanceHelper stance1 = PCLStanceHelper.Get(af1);
        PCLStanceHelper stance2 = PCLStanceHelper.Get(af2);

        choices.Initialize(this);
        if (stance1 != null) {
            choices.AddEffect(new GenericEffect_KisukeUrahara(stance1, af1, secondaryValue));
        }
        if (stance2 != null) {
            choices.AddEffect(new GenericEffect_KisukeUrahara(stance2, af2, secondaryValue));
        }
        choices.Select(PCLActions.Top, 1, m);
    }

    public static class GenericEffect_KisukeUrahara extends GenericEffect_EnterStance
    {
        PCLAffinity affinity;
        public GenericEffect_KisukeUrahara(PCLStanceHelper stance, PCLAffinity affinity, int amount)
        {
            super(stance);
            this.amount = amount;
            this.affinity = affinity;
        }

        @Override
        public void Use(PCLCard card, AbstractPlayer p, AbstractMonster m)
        {
            PCLActions.Bottom.ChangeStance(stance);
            PCLActions.Bottom.AddAffinity(affinity, amount);
        }
    }
}