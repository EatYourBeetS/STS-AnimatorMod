package pinacolada.cards.pcl.series.TouhouProject;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCardTarget;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.powers.PCLPower;
import pinacolada.powers.common.ResistancePower;
import pinacolada.stances.pcl.WisdomStance;
import pinacolada.utilities.PCLActions;

public class ByakurenHijiri extends PCLCard
{
    public static final PCLCardData DATA = Register(ByakurenHijiri.class).SetSkill(1, CardRarity.UNCOMMON, PCLCardTarget.Self).SetSeriesFromClassPackage(true);

    public ByakurenHijiri()
    {
        super(DATA);

        Initialize(0, 2, 8, 0);
        SetUpgrade(0, 0, 0, 0);
        SetAffinity_Orange(1);
        SetAffinity_Light(1, 0, 1);
        SetAffinity_Dark(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.Orange, 7);

        SetEthereal(true);
        SetExhaust(true);
    }

    @Override
    public void OnUpgrade() {
        SetEthereal(false);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CheckAffinity(PCLAffinity.Orange) && CombatStats.TryActivateSemiLimited(cardID)) {
            PCLActions.Bottom.ChangeStance(WisdomStance.STANCE_ID);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.ApplyPower(new ByakurenHijiriPower(player, magicNumber));
    }

    public static class ByakurenHijiriPower extends PCLPower
    {
        public ByakurenHijiriPower(AbstractPlayer owner, int amount)
        {
            super(owner, ByakurenHijiri.DATA);

            Initialize(amount);
            updateDescription();
        }

        @Override
        protected void onAmountChanged(int previousAmount, int difference)
        {
            super.onAmountChanged(previousAmount, difference);

            PCLActions.Top.StackPower(new ResistancePower(owner, difference));
        }

        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m)
        {
            super.onPlayCard(card, m);

            if (!card.isInAutoplay) {
                RemovePower();
            }
        }
    }
}

