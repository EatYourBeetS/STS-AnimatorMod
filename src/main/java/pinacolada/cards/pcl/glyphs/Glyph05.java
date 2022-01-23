package pinacolada.cards.pcl.glyphs;

import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Glyph05 extends Glyph
{
    public static final PCLCardData DATA = RegisterInternal(Glyph05.class);
    public static final int MAX_UPGRADE_LEVEL = 50;
    public PCLAffinity affinity;

    public Glyph05()
    {
        super(DATA);

        Initialize(0, 0, 30, 0);
        SetUpgrade(0, 0, 3, 0);

        maxUpgradeLevel = 0;
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(GetAffinity().GetFormattedPowerSymbol());
    }

    @Override
    public void AtEndOfTurnEffect(boolean isPlayer) {}

    @Override
    public void AtStartOfBattleEffect() {
        PCLActions.Bottom.StackPower(new Glyph05Power(player, affinity));
    }

    @Override
    public void AtStartOfTurnEffect() {}

    public PCLAffinity GetAffinity() {
        if (affinity == null) {
            affinity = PCLGameUtilities.GetRandomElement(PCLAffinity.Basic());
        }
        return affinity;
    }

    public static class Glyph05Power extends PCLPower
    {
        public final PCLAffinity affinity;

        public Glyph05Power(AbstractCreature owner, PCLAffinity affinity)
        {
            super(owner, Glyph05.DATA);
            this.affinity = affinity;
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();
            PCLCombatStats.MatchingSystem.Powers.get(affinity.ID).SetEnabled(false);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();
            PCLCombatStats.MatchingSystem.Powers.get(affinity.ID).SetEnabled(true);
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, affinity.GetFormattedPowerSymbol());
        }
    }
}