package pinacolada.cards.pcl.glyphs;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Glyph07 extends Glyph
{
    public static final PCLCardData DATA = RegisterInternal(Glyph07.class);
    public PCLAffinity affinity;

    public Glyph07()
    {
        super(DATA);

        Initialize(0, 0, 1, 0);
        SetUpgrade(0, 0, 1, 0);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(GetAffinity().GetFormattedAffinitySymbol());
    }

    @Override
    public void AtEndOfTurnEffect(boolean isPlayer) {}

    @Override
    public void AtStartOfBattleEffect() {
        PCLActions.Bottom.StackPower(new Glyph07Power(player, affinity, magicNumber));
    }

    @Override
    public void AtStartOfTurnEffect() {}

    public PCLAffinity GetAffinity() {
        if (affinity == null) {
            affinity = PCLGameUtilities.GetRandomElement(PCLAffinity.Basic());
        }
        return affinity;
    }

    public static class Glyph07Power extends PCLPower
    {
        public final PCLAffinity affinity;

        public Glyph07Power(AbstractCreature owner, PCLAffinity affinity, int amount)
        {
            super(owner, Glyph07.DATA);
            this.affinity = affinity;
            Initialize(amount);
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, affinity.GetFormattedAffinitySymbol(), amount);
        }

        @Override
        public void onCardDraw(AbstractCard card) {
            if (this.amount > 0 && PCLGameUtilities.HasAffinity(card, affinity)) {
                PCLActions.Bottom.Discard(card, player.hand);
                amount -= 1;
                if (this.amount <= 0) {
                    RemovePower();
                }
            }
        }
    }
}