package eatyourbeets.relics;

import com.evacipated.cardcrawl.mod.stslib.patches.HitboxRightClick;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;

public class CerealBox extends AnimatorRelic
{
    public static final String ID = CreateFullID(CerealBox.class.getSimpleName());

    private static final int HEAL_AMOUNT = 1;
    private static final int BASE_CHARGES = 20;
    private static final int SHOP_CHARGES = 5;
    private static final int MAX_CHARGES = 30;

    public CerealBox()
    {
        super(ID, RelicTier.SHOP, LandingSound.FLAT);
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

        this.counter = BASE_CHARGES;
    }

    @Override
    public void update()
    {
        super.update();

        if (HitboxRightClick.rightClicked.get(this.hb))
        {
            Use();
        }
    }

    @Override
    public void justEnteredRoom(AbstractRoom room)
    {
        if (room instanceof ShopRoom)
        {
            counter += SHOP_CHARGES;
            if (counter > MAX_CHARGES)
            {
                counter = MAX_CHARGES;
            }

            this.flash();
        }

        super.justEnteredRoom(room);
    }

    public void Use()
    {
        if (counter > 0)
        {
            AbstractPlayer p = AbstractDungeon.player;
            if (p.currentHealth < p.maxHealth)
            {
                p.heal(HEAL_AMOUNT);
//                if (PlayerStatistics.InBattle())
//                {
//                    GameActionsHelper.AddToBottom(new HealAction(AbstractDungeon.player, AbstractDungeon.player, HEAL_AMOUNT));
//                }
//                else
//                {
//                    p.heal(HEAL_AMOUNT);
//                }

                counter -= 1;
            }
        }
    }
}