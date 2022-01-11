package pinacolada.blights.common;

import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RitualPower;
import pinacolada.resources.pcl.PCLPlayerData;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class GlyphBlight1 extends AbstractGlyphBlight
{
    public static final String ID = CreateFullID(GlyphBlight1.class);

    public GlyphBlight1()
    {
        this(0);
    }

    public GlyphBlight1(int counter)
    {
        super(ID, PCLPlayerData.ASCENSION_GLYPH1_UNLOCK, PCLPlayerData.ASCENSION_GLYPH1_LEVEL_STEP, 0, 1, counter);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        int potency = GetPotency();
        if (potency > 0) {
            for (AbstractMonster mo : PCLGameUtilities.GetEnemies(true)) {
                PCLActions.Top.StackPower(mo, new RitualPower(mo, potency, false));
            }
        }
    }

}