package pinacolada.blights.common;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.resources.pcl.PCLPlayerData;
import pinacolada.utilities.PCLGameUtilities;

public class GlyphBlight2 extends AbstractGlyphBlight
{
    public static final String ID = CreateFullID(GlyphBlight2.class);

    public GlyphBlight2()
    {
        this(0);
    }

    public GlyphBlight2(int counter)
    {
        super(ID, PCLPlayerData.ASCENSION_GLYPH1_UNLOCK, PCLPlayerData.ASCENSION_GLYPH1_LEVEL_STEP, 10, 5, counter);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        for (AbstractMonster mo : PCLGameUtilities.GetEnemies(true)) {
            mo.increaseMaxHp(Math.max(1, mo.maxHealth * GetPotency() / 100), true);
        }
    }

}