package eatyourbeets.effects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.SearingBlow;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.relics.animator.ExquisiteBloodVial;

import java.util.ArrayList;

public class UnnamedRelicEquipEffect extends AbstractGameEffect
{
    private final int goldBonus;

    public UnnamedRelicEquipEffect(int goldBonus)
    {
        this.goldBonus = goldBonus;
        this.duration = 1.0F;
    }

    public void update()
    {
        AbstractPlayer p = AbstractDungeon.player;

        ModHelper.setModsFalse();

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
        else if (hp < 999)
        {
            hp = 150;
        }

        p.gold = goldBonus;
        p.maxHealth = p.currentHealth = hp;
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

    public static int CalculateGoldBonus()
    {
        AbstractPlayer p = AbstractDungeon.player;

        int bonus = 60;
        for (AbstractRelic r : p.relics)
        {
            if (r instanceof ExquisiteBloodVial)
            {
                bonus += 30 + ((r.counter > 0) ? (r.counter * 10) : 0);
            }
            else switch (r.tier)
            {
                case STARTER:
                    bonus += 0;
                    break;

                case COMMON:
                    bonus += 6;
                    break;

                case UNCOMMON:
                    bonus += 10;
                    break;

                case RARE:
                    bonus += 18;
                    break;

                case SPECIAL:
                    bonus += 25;
                    break;

                case BOSS:
                    bonus += 30;
                    break;

                case SHOP:
                    bonus += 10;
                    break;
            }
        }

        for (AbstractCard c : p.masterDeck.group)
        {
            bonus += Math.min(c.timesUpgraded, 20) * 3;
        }

        for (AbstractPotion potion : p.potions)
        {
            switch (potion.rarity)
            {
                case PLACEHOLDER:
                    bonus += 0;
                    break;

                case COMMON:
                    bonus += 6;
                    break;

                case UNCOMMON:
                    bonus += 10;
                    break;

                case RARE:
                    bonus += 14;
                    break;
            }
        }

        bonus += p.maxHealth / 2;
        bonus += p.gold / 7;

        return Math.min(999, bonus);
    }
}