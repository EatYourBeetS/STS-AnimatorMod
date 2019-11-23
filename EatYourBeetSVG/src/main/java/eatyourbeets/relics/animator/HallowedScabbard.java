package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.RegenPower;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JavaUtilities;

public class HallowedScabbard extends AnimatorRelic
{
    public static final String ID = CreateFullID(HallowedScabbard.class.getSimpleName());

    private static final int DAMAGE_THRESHOLD = 12;
    private static final int REGENERATION = 4;
    private static final int FORCE = 2;

    private boolean used = false;

    public HallowedScabbard()
    {
        super(ID, RelicTier.RARE, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription()
    {
        return JavaUtilities.Format(DESCRIPTIONS[0], DAMAGE_THRESHOLD, REGENERATION, FORCE);
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        counter = 0;
        used = false;
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        counter = -1;
    }

    @Override
    public void onLoseHp(int damageAmount)
    {
        super.onLoseHp(damageAmount);

        if (GameUtilities.InBattle())
        {
            counter += damageAmount;
            if (!used && counter >= DAMAGE_THRESHOLD)
            {
                AbstractPlayer p = AbstractDungeon.player;
                GameActionsHelper.ApplyPower(p, p, new RegenPower(p, REGENERATION), REGENERATION);
                GameActionsHelper.GainForce(FORCE);
                used = true;
                this.flash();
            }
        }
    }
}