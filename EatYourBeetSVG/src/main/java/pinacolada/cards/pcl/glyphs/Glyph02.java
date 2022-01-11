package pinacolada.cards.pcl.glyphs;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.PCLCardData;
import pinacolada.utilities.PCLGameUtilities;

public class Glyph02 extends Glyph
{
    public static final PCLCardData DATA = RegisterInternal(Glyph02.class);

    public Glyph02()
    {
        super(DATA);

        Initialize(0, 0, 10, 0);
        SetUpgrade(0, 0, 2, 0);
    }

    @Override
    public void AtEndOfTurnEffect(boolean isPlayer) {}

    @Override
    public void AtStartOfBattleEffect() {
        for (AbstractMonster mo : PCLGameUtilities.GetEnemies(true)) {
            mo.increaseMaxHp(Math.max(1, mo.maxHealth * magicNumber / 100), true);
        }
    }

    @Override
    public void AtStartOfTurnEffect() {}
}