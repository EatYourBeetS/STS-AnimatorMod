package pinacolada.cards.pcl.glyphs;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCardData;
import pinacolada.powers.replacement.PCLCurlUpPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Glyph09 extends Glyph
{
    public static final PCLCardData DATA = RegisterInternal(Glyph09.class);

    public Glyph09()
    {
        super(DATA);

        Initialize(0, 0, 5, 0);
        SetUpgrade(0, 0, 3, 0);
    }

    @Override
    public void AtEndOfTurnEffect(boolean isPlayer) {}

    @Override
    public void AtStartOfBattleEffect() {
    }

    @Override
    public void AtStartOfTurnEffect() {
        for (AbstractMonster mo : PCLGameUtilities.GetEnemies(true)) {
            PCLActions.Top.StackPower(mo, new PCLCurlUpPower(mo, magicNumber));
        }
    }
}