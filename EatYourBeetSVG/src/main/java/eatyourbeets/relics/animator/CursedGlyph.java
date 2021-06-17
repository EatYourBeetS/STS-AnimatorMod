package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.listeners.OnEquipUnnamedReignRelicListener;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class CursedGlyph extends AnimatorRelic implements OnEquipUnnamedReignRelicListener
{
    public static final String ID = CreateFullID(CursedGlyph.class);
    public static final int MAX_HP_LOSS = 4;

    public CursedGlyph()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(MAX_HP_LOSS);
    }

    @Override
    public void atBattleStart()
    {
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters)
        {
            IncreaseMaxHP(m);
        }

        AbstractDungeon.onModifyPower();
    }

    @Override
    public void onSpawnMonster(AbstractMonster monster)
    {
        IncreaseMaxHP(monster);

        AbstractDungeon.onModifyPower();
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        player.decreaseMaxHealth(MAX_HP_LOSS);
    }

    @Override
    public void OnEquipUnnamedReignRelic()
    {
        player.decreaseMaxHealth(MAX_HP_LOSS);
    }

    protected void IncreaseMaxHP(AbstractMonster monster)
    {
        GameActions.Top.Add(new RelicAboveCreatureAction(monster, this));

        int bonusHealth = 6;
        if (GR.Common.Dungeon.IsUnnamedReign())
        {
            bonusHealth += (int) Math.ceil(monster.maxHealth * 0.07);
        }
        else if (AbstractDungeon.ascensionLevel >= 7)
        {
            bonusHealth += (int) Math.ceil(monster.maxHealth * 0.09);
        }
        else
        {
            bonusHealth += (int) Math.ceil(monster.maxHealth * 0.13);
        }

        monster.increaseMaxHp(bonusHealth, true);
    }
}