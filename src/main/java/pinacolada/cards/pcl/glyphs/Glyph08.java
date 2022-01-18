package pinacolada.cards.pcl.glyphs;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Glyph08 extends Glyph
{
    public static final PCLCardData DATA = RegisterInternal(Glyph08.class);

    public Glyph08()
    {
        super(DATA);

        Initialize(0, 0, 3, 0);
        SetUpgrade(0, 0, 1, 0);
    }

    @Override
    public void AtEndOfTurnEffect(boolean isPlayer) {}

    @Override
    public void AtStartOfBattleEffect() {
        for (AbstractMonster mo : PCLGameUtilities.GetEnemies(true)) {
            PCLActions.Top.StackPower(mo, new ThornsPower(mo, magicNumber));
            PCLActions.Top.StackPower(mo, new ArtifactPower(mo, magicNumber));
        }
    }

    @Override
    public void AtStartOfTurnEffect() {}
}