package eatyourbeets.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.interfaces.AllowedUnnamedReignRelic;
import eatyourbeets.powers.PlayerStatistics;

public class CursedGlyph extends AnimatorRelic implements AllowedUnnamedReignRelic
{
    private static final int LOSE_MAX_HP = 4;

    public static final String ID = CreateFullID(CursedGlyph.class.getSimpleName());

    public CursedGlyph()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + LOSE_MAX_HP + DESCRIPTIONS[1];
    }

    public void atBattleStart()
    {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
        {
            GameActionsHelper.AddToTop(new RelicAboveCreatureAction(m, this));

            int bonusHealth = 6;
            if (AbstractDungeon.ascensionLevel >= 7 || PlayerStatistics.SaveData.EnteredUnnamedReign)
            {
                bonusHealth += (int)Math.ceil(m.maxHealth * 0.11);
            }
            else
            {
                bonusHealth += (int)Math.ceil(m.maxHealth * 0.14);
            }

            m.increaseMaxHp(bonusHealth, true);
        }

        AbstractDungeon.onModifyPower();
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        AbstractDungeon.player.decreaseMaxHealth(LOSE_MAX_HP);
    }
}