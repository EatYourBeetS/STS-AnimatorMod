package eatyourbeets.relics;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.RegenPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import eatyourbeets.GameActionsHelper;

public class HallowedScabbard extends AnimatorRelic
{
    public static final String ID = CreateFullID(HallowedScabbard.class.getSimpleName());

    private static final int DAMAGE_THRESHOLD = 15;
    private static final int REGENERATION = 5;
    private static final int STRENGTH = 2;

    private boolean used = false;
    private int damageThisCombat = 0;

    public HallowedScabbard()
    {
        super(ID, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        damageThisCombat = 0;
        used = false;
    }

    @Override
    public void onLoseHp(int damageAmount)
    {
        super.onLoseHp(damageAmount);

        damageThisCombat += damageAmount;
        if (!used && damageThisCombat >= DAMAGE_THRESHOLD)
        {
            AbstractPlayer p = AbstractDungeon.player;
            GameActionsHelper.ApplyPower(p, p, new RegenPower(p, REGENERATION), REGENERATION);
            GameActionsHelper.ApplyPower(p, p, new StrengthPower(p, STRENGTH), STRENGTH);
            used = true;
            this.flash();
        }
    }
}