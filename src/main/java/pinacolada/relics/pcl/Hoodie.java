package pinacolada.relics.pcl;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.interfaces.markers.Hidden;
import pinacolada.relics.PCLRelic;

public class Hoodie extends PCLRelic implements Hidden
{
    public static final String ID = CreateFullID(Hoodie.class);
    public static final int MAX_HP_BONUS = 3;

    public Hoodie()
    {
        super(ID, RelicTier.UNCOMMON, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return FormatDescription(0, MAX_HP_BONUS);
    }

    @Override
    public void onVictory()
    {
        super.onVictory();
        if (GameActionManager.damageReceivedThisCombat == 0)
        {
            player.increaseMaxHp(MAX_HP_BONUS, true);
            flash();
        }
    }

    @Override
    public boolean canSpawn()
    {
        return super.canSpawn() && AbstractDungeon.floorNum < 24;
    }
}