package eatyourbeets.relics.animator;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

public class ColorlessFragment extends AbstractMissingPiece
{
    public static final String ID = CreateFullID(ColorlessFragment.class);
    public static final int COLORLESS_WEIGHT = 12;
    public static final int REWARD_INTERVAL = 3;

    private boolean whyAreThere300ObtainMethods = false;

    public ColorlessFragment()
    {
        super(ID, RelicTier.SHOP, LandingSound.FLAT);
    }

    @Override
    public boolean canSpawn()
    {
        return super.canSpawn() && AbstractDungeon.floorNum <= 33 && player.hasRelic(TheMissingPiece.ID);
    }

    @Override
    public int getPrice()
    {
        return MathUtils.ceil(super.getPrice() * 1.15f);
    }

    @Override
    protected int GetRewardInterval()
    {
        return REWARD_INTERVAL;
    }

    @Override
    public void obtain()
    {
        ArrayList<AbstractRelic> relics = player.relics;
        for (int i = 0; i < relics.size(); i++)
        {
            if (relics.get(i).relicId.equals(TheMissingPiece.ID))
            {
                whyAreThere300ObtainMethods = true;
                instantObtain(player, i, true);
                return;
            }
        }

        super.obtain();
    }

    @Override
    public void instantObtain()
    {
        ArrayList<AbstractRelic> relics = player.relics;
        for (int i = 0; i < relics.size(); i++)
        {
            if (relics.get(i).relicId.equals(TheMissingPiece.ID))
            {
                whyAreThere300ObtainMethods = true;
                instantObtain(player, i, true);
                return;
            }
        }

        super.instantObtain();
    }

    @Override
    public void instantObtain(AbstractPlayer p, int slot, boolean callOnEquip)
    {
        if (!whyAreThere300ObtainMethods)
        {
            ArrayList<AbstractRelic> relics = p.relics;
            for (int i = 0; i < relics.size(); i++)
            {
                if (relics.get(i).relicId.equals(TheMissingPiece.ID))
                {
                    whyAreThere300ObtainMethods = true;
                    instantObtain(p, i, true);
                    return;
                }
            }
        }

        super.instantObtain(p, slot, callOnEquip);
    }
}