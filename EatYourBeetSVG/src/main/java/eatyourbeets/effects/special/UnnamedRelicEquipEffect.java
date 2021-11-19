package eatyourbeets.effects.special;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Apparition;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import eatyourbeets.dungeons.TheUnnamedReign;
import eatyourbeets.interfaces.listeners.OnEquipUnnamedReignRelicListener;
import eatyourbeets.relics.animator.ExquisiteBloodVial;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorRuntimeLoadout;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.util.ArrayList;

public class UnnamedRelicEquipEffect extends AbstractGameEffect
{
    private final int baseGold;
    private final int baseHP;

    public UnnamedRelicEquipEffect()
    {
        this.duration = 1f;
        this.baseGold = CalculateGoldBonus();
        this.baseHP = CalculateMaxHealth();
    }

    private void ReplaceCard(ArrayList<AbstractCard> replacement, String cardID)
    {
        UnlockTracker.markCardAsSeen(cardID);
        replacement.add(CardLibrary.getCard(cardID).makeCopy());
    }

    public void update()
    {
        ModHelper.setModsFalse();

        final AbstractPlayer p = AbstractDungeon.player;
        final ArrayList<AbstractCard> replacement = TheUnnamedReign.GetCardReplacements(p.masterDeck.group, true);
        final int apparitionsCount = JUtils.Count(replacement, c -> Apparition.ID.equals(c.cardID));

        int hp = this.baseHP;
        int goldBonus = this.baseGold;

        for (AnimatorRuntimeLoadout series : GR.Animator.Dungeon.Loadouts)
        {
            goldBonus += series.bonus * 7;
            hp += series.bonus;
        }

        if (hp < 999 && apparitionsCount > 1)
        {
            hp *= 1 - (0.1f * (apparitionsCount - 1));

            if (hp < 10)
            {
                hp = 10;
            }
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

        for (AbstractRelic relic : p.relics)
        {
            if (relic instanceof OnEquipUnnamedReignRelicListener)
            {
                ((OnEquipUnnamedReignRelicListener)relic).OnEquipUnnamedReignRelic();
            }
        }

        this.isDone = true;
    }

    public void render(SpriteBatch sb)
    {

    }

    public void dispose()
    {

    }

    public static int CalculateMaxHealth()
    {
        final CharSelectInfo info = AbstractDungeon.player.getLoadout();

        int hp = 100;
        if (info != null)
        {
            float startingHP = info.maxHp;
            if (startingHP > 71)
            {
                startingHP = Math.max(71, startingHP * 0.95f);
            }
            else
            {
                startingHP = Math.min(71, startingHP * 1.05f);
            }

            if (GameUtilities.GetActualAscensionLevel() >= 14)
            {
                hp = (int)Math.ceil(Math.min(999, startingHP * 1.3f));
            }
            else
            {
                hp = (int)Math.ceil(Math.min(999, startingHP * 1.4f));
            }
        }
        
        return hp;
    }

    public static int CalculateGoldBonus()
    {
        final AbstractPlayer player = AbstractDungeon.player;
        final CharSelectInfo info = player.getLoadout();

        int bonus = info.gold / 2;
        for (AbstractRelic r : player.relics)
        {
            if (!(r instanceof OnEquipUnnamedReignRelicListener))
            {
                if (r instanceof ExquisiteBloodVial)
                {
                    bonus += 30 + ((r.counter > 0) ? (r.counter * 10) : 0);
                }
                else switch (r.tier)
                {
                    case COMMON:
                        bonus += 6;
                        break;

                    case UNCOMMON:
                        bonus += 10;
                        break;

                    case RARE:
                        bonus += 18;
                        break;

                    case BOSS:
                        bonus += 30;
                        break;

                    case SHOP:
                        bonus += 10;
                        break;

                    case SPECIAL:
                        bonus += 25;
                        break;
                }
            }
        }

        for (AbstractCard c : player.masterDeck.group)
        {
            bonus += Math.min(c.timesUpgraded, 20) * 5;
        }

        for (AbstractPotion potion : player.potions)
        {
            switch (potion.rarity)
            {
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

        bonus += player.currentHealth / 2;
        bonus += player.gold / 7;

        return Math.min(999, bonus);
    }
}