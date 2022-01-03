package pinacolada.cards.pcl.series.GATE;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLPower;
import pinacolada.ui.combat.PCLAffinityMeter;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class SherryTueli extends PCLCard
{
    public static final PCLCardData DATA = Register(SherryTueli.class)
            .SetSkill(1, CardRarity.COMMON, eatyourbeets.cards.base.EYBCardTarget.None)
            .SetMaxCopies(2)
            .SetSeriesFromClassPackage();

    public SherryTueli()
    {
        super(DATA);

        Initialize(0, 8, 1);
        SetUpgrade(0, 3, 0);

        SetAffinity_Green(1);
        SetAffinity_Orange(1, 0, 0);
        SetAffinity_Light(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.Light, 7);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);

        PCLActions.Bottom.DiscardFromHand(name, magicNumber, false)
        .SetOptions(false, true, false)
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards) {
                PCLCard pC = PCLJUtils.SafeCast(c, PCLCard.class);
                if (pC != null) {
                    PCLActions.Bottom.ApplyPower(new SherryTueliPower(p, pC)).AllowDuplicates(true);
                }
            }
        });

        if (TrySpendAffinity(PCLAffinity.Light)) {
            PCLActions.Bottom.ObtainAffinityToken(PCLAffinity.Orange, false);
        }
    }

    public static class SherryTueliPower extends PCLPower
    {
        private PCLCard card;

        public SherryTueliPower(AbstractCreature owner, PCLCard card)
        {
            super(owner, SherryTueli.DATA);
            this.card = card;
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            PCLActions.Bottom.RerollAffinity(PCLAffinityMeter.Target.CurrentAffinity)
                    .SetAffinityChoices(PCLGameUtilities.GetPCLAffinities(card).GetAffinities().toArray(new PCLAffinity[] {}))
                    .SetOptions(false, true);
            ReducePower(1);
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, card.name.replace(" ", " #y"));
        }
    }
}