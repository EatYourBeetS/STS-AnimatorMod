package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.interfaces.OnEquipUnnamedReignRelicSubscriber;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.utilities.JavaUtilities;

public class CursedGlyph extends AnimatorRelic implements OnEquipUnnamedReignRelicSubscriber
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
        return JavaUtilities.Format(DESCRIPTIONS[0], LOSE_MAX_HP);
    }

    public void atBattleStart()
    {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
        {
            GameActions.Top.Add(new RelicAboveCreatureAction(m, this));

            int bonusHealth = 6;
            if (PlayerStatistics.SaveData.EnteredUnnamedReign)
            {
                bonusHealth += (int)Math.ceil(m.maxHealth * 0.07);
            }
            else if (AbstractDungeon.ascensionLevel >= 7)
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

    @Override
    public void OnEquipUnnamedReignRelic()
    {
        AbstractDungeon.player.decreaseMaxHealth(LOSE_MAX_HP);
    }
}