package eatyourbeets.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.SearingBlow;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;

public class UnnamedRelicEquipEffect extends AbstractGameEffect
{
    public UnnamedRelicEquipEffect()
    {
        this.duration = 1.0F;
    }

    public void update()
    {
        AbstractPlayer p = AbstractDungeon.player;

        ArrayList<AbstractCard> replacement = new ArrayList<>();
        for (AbstractCard card : p.masterDeck.group)
        {
            if (card.cardID.equals("hubris:InfiniteBlow")) // to prevent resetting the upgrades permanently
            {
                replacement.add(new SearingBlow());
            }
            else
            {
                replacement.add(card.makeCopy());
            }
        }

        p.maxHealth = p.currentHealth = 100;
        p.healthBarUpdatedEvent();

        p.potionSlots = AbstractDungeon.ascensionLevel < 11 ? 3 : 2;
        p.potions.clear();
        for (int i = 0; i < p.potionSlots; i++)
        {
            p.potions.add(new PotionSlot(i));
        }

        p.adjustPotionPositions();

        p.masterDeck.clear();
        p.masterDeck.group.addAll(replacement);

        this.isDone = true;
    }

    public void render(SpriteBatch sb)
    {

    }

    public void dispose()
    {

    }
}