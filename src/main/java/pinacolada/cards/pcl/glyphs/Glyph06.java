package pinacolada.cards.pcl.glyphs;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLAffinity;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Glyph06 extends Glyph
{
    public static final PCLCardData DATA = RegisterInternal(Glyph06.class);
    public PCLAffinity affinity;

    public Glyph06()
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
        PCLActions.Bottom.StackPower(new Glyph06Power(player, affinity, magicNumber));
    }

    @Override
    public void AtStartOfTurnEffect() {}

    public PCLAffinity GetAffinity() {
        if (affinity == null) {
            affinity = PCLGameUtilities.GetRandomElement(PCLAffinity.Basic());
        }
        return affinity;
    }

    public static class Glyph06Power extends PCLPower
    {
        public final PCLAffinity affinity;

        public Glyph06Power(AbstractCreature owner, PCLAffinity affinity, int amount)
        {
            super(owner, Glyph06.DATA);
            this.affinity = affinity;
            Initialize(amount);
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, affinity.GetFormattedAffinitySymbol(), amount);
        }

        @Override
        public void atStartOfTurn()
        {
            super.atStartOfTurn();

            enabled = true;
        }

        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m) {

            super.onPlayCard(card, m);

            if (enabled && this.amount > 0 && PCLGameUtilities.HasAffinity(card, affinity)) {
                for (AbstractMonster m1 : PCLGameUtilities.GetEnemies(true))
                {
                    PCLActions.Bottom.Add(new HealAction(m1, null, amount));
                }
                enabled = false;
            }
        }
    }
}