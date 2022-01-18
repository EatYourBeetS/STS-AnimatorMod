package pinacolada.cards.pcl.glyphs;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RitualPower;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Glyph01 extends Glyph
{
    public static final PCLCardData DATA = RegisterInternal(Glyph01.class);

    public Glyph01()
    {
        super(DATA);

        Initialize(0, 0, 1, 0);
        SetUpgrade(0, 0, 1, 0);
    }

    @Override
    public void AtEndOfTurnEffect(boolean isPlayer) {}

    @Override
    public void AtStartOfBattleEffect() {
        for (AbstractMonster mo : PCLGameUtilities.GetEnemies(true)) {
            PCLActions.Top.StackPower(mo, new RitualPower(mo, magicNumber, false));
        }
    }

    @Override
    public void AtStartOfTurnEffect() {}
}