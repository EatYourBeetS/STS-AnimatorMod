package pinacolada.relics.pcl;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;
import pinacolada.relics.PCLRelic;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class ShionDessert extends PCLRelic
{
    public static final String ID = CreateFullID(ShionDessert.class);
    public static final int POISON_AMOUNT = 2;

    public ShionDessert()
    {
        super(ID, RelicTier.COMMON, LandingSound.MAGICAL);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, POISON_AMOUNT);
    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();

        int mostPoison = -1;
        AbstractMonster enemy = null;
        for (AbstractMonster m : PCLGameUtilities.GetEnemies(true))
        {
            int poison = PCLGameUtilities.GetPowerAmount(m, PoisonPower.POWER_ID);
            if (poison > mostPoison)
            {
                mostPoison = poison;
                enemy = m;
            }
        }

        if (enemy == null)
        {
            enemy = PCLGameUtilities.GetRandomEnemy(true);
        }

        if (enemy != null)
        {
            PCLActions.Bottom.Add(new RelicAboveCreatureAction(enemy, this));
            PCLActions.Bottom.ApplyPoison(player, enemy, POISON_AMOUNT);
            flash();
        }
    }
}