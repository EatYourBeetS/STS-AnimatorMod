package eatyourbeets.relics.animator;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import eatyourbeets.relics.AnimatorRelic;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.InputManager;
import eatyourbeets.utilities.JavaUtilities;

public class CerealBox extends AnimatorRelic
{
    public static final String ID = CreateFullID(CerealBox.class);
    public static final int HEAL_AMOUNT = 2;
    public static final int BASE_CHARGES = 10;
    public static final int SHOP_CHARGES = 4;
    public static final int MAX_CHARGES = 20;

    public CerealBox()
    {
        super(ID, RelicTier.SHOP, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return JavaUtilities.Format(DESCRIPTIONS[0], HEAL_AMOUNT, BASE_CHARGES, SHOP_CHARGES, MAX_CHARGES);
    }

    @Override
    public int getPrice()
    {
        return (int)(super.getPrice() * 0.8f);
    }

    @Override
    public void onEquip()
    {
        super.onEquip();

        SetCounter(BASE_CHARGES);
    }

    @Override
    public void update()
    {
        super.update();

        if (hb.hovered && !AbstractDungeon.isScreenUp && InputManager.RightClick.IsJustPressed())
        {
            stopPulse();
            Use();
        }
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        stopPulse();
    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();

        if (counter > 0 && GameUtilities.GetHealthPercentage(player) < 0.25f)
        {
            GameActions.Bottom.Add(new RelicAboveCreatureAction(player, this));

            beginLongPulse();
        }
        else
        {
            stopPulse();
        }
    }

    @Override
    public void justEnteredRoom(AbstractRoom room)
    {
        super.justEnteredRoom(room);

        if (room instanceof ShopRoom)
        {
            if (AddCounter(SHOP_CHARGES) > MAX_CHARGES)
            {
                SetCounter(MAX_CHARGES);
            }

            flash();
        }
    }

    public void Use()
    {
        if (counter > 0)
        {
            if (player.currentHealth < player.maxHealth)
            {
                player.heal(HEAL_AMOUNT);

                AddCounter(-1);
            }
        }
    }
}