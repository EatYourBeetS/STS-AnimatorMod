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

            int bonusHealth = 6 + (int)Math.ceil(m.maxHealth * 0.18);

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