package eatyourbeets.relics;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.GameActionsHelper;

public class TheEgnaroPiece extends AnimatorRelic
{
    public static final String ID = CreateFullID(TheEgnaroPiece.class.getSimpleName());

    private static final int TEMPORARY_HP = 7;

    public TheEgnaroPiece()
    {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + TEMPORARY_HP + DESCRIPTIONS[1];
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        AbstractPlayer p =  AbstractDungeon.player;
        int healthDiff = p.maxHealth - p.currentHealth;
        if (healthDiff > 0)
        {
            healthDiff = Math.min(TEMPORARY_HP, healthDiff);
            p.heal(healthDiff);
        }

        if (healthDiff < TEMPORARY_HP)
        {
            GameActionsHelper.GainTemporaryHP(p, p, TEMPORARY_HP - healthDiff);
        }

        this.flash();
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        GameActionsHelper.DrawCard(AbstractDungeon.player, 1);
    }

    public void onEquip()
    {
        AbstractDungeon.player.energy.energyMaster += 1;
    }

    public void onUnequip()
    {
        AbstractDungeon.player.energy.energyMaster += 1;
    }
}