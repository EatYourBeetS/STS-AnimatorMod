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
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.modifiers.PersistentCardModifiers;
import eatyourbeets.dungeons.TheUnnamedReign;
import eatyourbeets.interfaces.listeners.OnEquipUnnamedReignRelicListener;
import eatyourbeets.relics.EnchantableRelic;
import eatyourbeets.relics.animator.ExquisiteBloodVial;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.misc.AnimatorRuntimeLoadout;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.Mathf;

import java.util.ArrayList;

public class UnnamedRelicEquipEffect extends AbstractGameEffect
{
    private final int baseGold;
    private final int baseHP;
    private EYBCardData customApparition;

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
        PersistentCardModifiers.Clear();
        ModHelper.setModsFalse();

        if (customApparition == null)
        {
            customApparition = GameUtilities.GetReplacement(null, Apparition.ID);
        }

        final AbstractPlayer p = AbstractDungeon.player;
        final ArrayList<AbstractCard> replacement = TheUnnamedReign.GetCardReplacements(p.masterDeck.group, true);
        final int apparitionsCount = customApparition != null ? JUtils.Count(replacement, c -> Apparition.ID.equals(c.cardID) || customApparition.ID.equals(c.cardID)) : JUtils.Count(replacement, c -> Apparition.ID.equals(c.cardID));
        final float hpPercentage = GameUtilities.GetHealthPercentage(p);

        int hp = this.baseHP;
        int goldBonus = this.baseGold;

        for (AnimatorRuntimeLoadout series : GR.Animator.Dungeon.Loadouts)
        {
            if (series.promoted)
            {
                goldBonus += series.bonus * 7;
                hp += series.bonus;
            }
        }

        if (hp < 999 && apparitionsCount > 0)
        {
            hp *= 1 - (0.1f * apparitionsCount);

            if (hp < 10)
            {
                hp = 10;
            }
        }

        p.gold = goldBonus;
        p.maxHealth = hp;
        p.currentHealth = Math.min(p.maxHealth, Mathf.FloorToInt(p.maxHealth * hpPercentage * 1.075f));
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
            if (GameUtilities.IsPlayerClass(GR.Animator.PlayerClass))
            {
                if (GameUtilities.GetAscensionLevel() >= 14)
                {
                    hp = (int) Math.ceil(Math.min(999, startingHP * 1.1f));
                }
                else
                {
                    hp = (int) Math.ceil(Math.min(999, startingHP * 1.2f));
                }
            }
            else
            {
                if (GameUtilities.GetAscensionLevel() >= 14)
                {
                    hp = (int) Math.ceil(Math.min(999, startingHP * 1.25f));
                }
                else
                {
                    hp = (int) Math.ceil(Math.min(999, startingHP * 1.35f));
                }
            }
        }

        if (hp < 75)
        {
            hp = (int)Mathf.Clamp(hp + ((75 - hp) * 0.4f), 1, 75);
        }
        else if (hp > 75)
        {
            hp = (int)Mathf.Clamp(hp - ((hp - 75) * 0.4f), 75, 999);
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
                bonus += GetRelicTierGoldValue(r.tier);
                if (r instanceof ExquisiteBloodVial)
                {
                    bonus += ((r.counter > 0) ? (r.counter * 10) : 0);
                }
            }
            else if (r instanceof EnchantableRelic)
            {
                final int level = ((EnchantableRelic)r).GetEnchantmentLevel();
                bonus += GetRelicTierGoldValue(r.tier);
                bonus -= level >= 2 ? 220 : level == 1 ? 130 : 75;
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

        //bonus += player.currentHealth / 2;
        bonus += player.gold / 7;

        return Mathf.Clamp(bonus, 10, 999);
    }

    private static int GetRelicTierGoldValue(AbstractRelic.RelicTier tier)
    {
        switch (tier)
        {
            case COMMON: return 6;
            case UNCOMMON: return 10;
            case RARE: return 18;
            case BOSS: return 30;
            case SHOP: return 10;
            case SPECIAL: return 25;
            default: return 1;
        }
    }
}