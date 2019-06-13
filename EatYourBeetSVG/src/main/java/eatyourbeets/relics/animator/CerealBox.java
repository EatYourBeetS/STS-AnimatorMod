package eatyourbeets.relics.animator;

import com.evacipated.cardcrawl.mod.stslib.patches.HitboxRightClick;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import eatyourbeets.relics.AnimatorRelic;

public class CerealBox extends AnimatorRelic
{
    public static final String ID = CreateFullID(CerealBox.class.getSimpleName());

    private static final int HEAL_AMOUNT = 2;
    private static final int BASE_CHARGES = 12;
    private static final int SHOP_CHARGES = 4;
    private static final int MAX_CHARGES = 20;

    public CerealBox()
    {
        super(ID, RelicTier.SHOP, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + HEAL_AMOUNT + DESCRIPTIONS[1] + BASE_CHARGES + DESCRIPTIONS[2] + SHOP_CHARGES + DESCRIPTIONS[3] + MAX_CHARGES + DESCRIPTIONS[4];
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
            stopPulse();
            Use();
        }
    }

    @Override
    public void onVictory()
    {
        super.onVictory();

        this.stopPulse();
    }

    @Override
    public void atTurnStartPostDraw()
    {
        super.atTurnStartPostDraw();

        AbstractPlayer p = AbstractDungeon.player;
        if (counter > 0 && (p.currentHealth / (float)p.maxHealth) < 0.25f)
        {
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            this.beginLongPulse();
        }
        else
        {
            this.stopPulse();
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

                counter -= 1;
            }
        }
    }
}