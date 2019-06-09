package eatyourbeets.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.SearingBlow;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

import java.util.ArrayList;

public class UnnamedRelicEquipEffect extends AbstractGameEffect
{
    private final int relicGoldBonus;

    public UnnamedRelicEquipEffect(int relicGoldBonus)
    {
        this.relicGoldBonus = relicGoldBonus;
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
                UnlockTracker.markCardAsSeen(SearingBlow.ID);
                replacement.add(CardLibrary.getCard(SearingBlow.ID).makeCopy());
            }
            else
            {
                replacement.add(card.makeCopy());
            }
        }

        CharSelectInfo info = AbstractDungeon.player.getLoadout();
        int hp = 100;
        if (info != null)
        {
            hp = info.maxHp;
        }

        if (hp <= 60)
        {
            hp = 80;
        }
        else if (hp <= 100)
        {
            hp = 100;
        }
        else if (hp <= 120)
        {
            hp = 120;
        }
        else
        {
            hp = 150;
        }

        p.maxHealth = p.currentHealth = hp;
        p.gold = relicGoldBonus;
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

    public static int CalculateRelicGoldBonus()
    {
        AbstractPlayer p = AbstractDungeon.player;

        int relicBonus = 300;
        for (AbstractRelic r : p.relics)
        {
            switch (r.tier)
            {
                case STARTER:
                    relicBonus += 3;
                    break;

                case COMMON:
                    relicBonus += 3;
                    break;

                case UNCOMMON:
                    relicBonus += 5;
                    break;

                case RARE:
                    relicBonus += 9;
                    break;

                case SPECIAL:
                    relicBonus += 21;
                    break;

                case BOSS:
                    relicBonus += 14;
                    break;

                case SHOP:
                    relicBonus += 6;
                    break;
            }
        }

        return relicBonus;
    }
}