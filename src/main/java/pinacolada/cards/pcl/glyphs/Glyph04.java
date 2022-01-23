package pinacolada.cards.pcl.glyphs;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class Glyph04 extends Glyph
{
    public static final PCLCardData DATA = RegisterInternal(Glyph04.class);
    public static final int MAX_UPGRADE_LEVEL = 50;
    public PCLAffinity affinity;
    public PCLAffinity affinity2;

    public Glyph04()
    {
        super(DATA);

        Initialize(0, 0, 30, 0);
        SetUpgrade(0, 0, 3, 0);

        maxUpgradeLevel = MAX_UPGRADE_LEVEL;
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(GetAffinity().GetFormattedAffinitySymbol(), GetAffinity2().GetFormattedAffinitySymbol());
    }

    @Override
    public void AtEndOfTurnEffect(boolean isPlayer) {}

    @Override
    public void AtStartOfBattleEffect() {
        PCLActions.Bottom.StackPower(new Glyph04Power(player, affinity, affinity2, magicNumber));
    }

    @Override
    public void AtStartOfTurnEffect() {}

    public PCLAffinity GetAffinity() {
        if (affinity == null) {
            affinity = PCLGameUtilities.GetRandomElement(PCLAffinity.Basic());
        }
        return affinity;
    }

    public PCLAffinity GetAffinity2() {
        if (affinity2 == null) {
            affinity2 = PCLGameUtilities.GetRandomElement(PCLJUtils.Filter(PCLAffinity.Basic(), af -> af != GetAffinity()));
        }
        return affinity2;
    }
    public static class Glyph04Power extends PCLPower
    {
        public final PCLAffinity affinity;
        public final PCLAffinity affinity2;

        public Glyph04Power(AbstractCreature owner,  PCLAffinity affinity, PCLAffinity affinity2, int amount)
        {
            super(owner, Glyph04.DATA);
            this.affinity = affinity;
            this.affinity2 = affinity2;
            Initialize(amount);
        }

        @Override
        public float modifyBlock(float blockAmount, AbstractCard card) {
            return (PCLGameUtilities.HasAffinity(card, affinity) || PCLGameUtilities.HasAffinity(card, affinity2)) ? CalculateBlock(blockAmount, amount) : blockAmount;
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, affinity.GetFormattedAffinitySymbol(), affinity2.GetFormattedAffinitySymbol(), amount);
        }

        public static float CalculateBlock(float block, float multiplier)
        {
            return Math.max(0, block - Math.max(0, block * (multiplier / 100f)));
        }
    }
}