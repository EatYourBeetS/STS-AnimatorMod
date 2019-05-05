package eatyourbeets.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class CursedGlyph extends AnimatorRelic
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
            AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(m, this));

            if (m.maxHealth < 30)
            {
                m.increaseMaxHp(10, true);
            }
            else if (m.maxHealth < 60)
            {
                m.increaseMaxHp(20, true);
            }
            else if (m.maxHealth < 120)
            {
                m.increaseMaxHp(30, true);
            }
            else if (m.maxHealth < 300)
            {
                m.increaseMaxHp(80, true);
            }
            else
            {
                m.increaseMaxHp(140, true);
            }
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