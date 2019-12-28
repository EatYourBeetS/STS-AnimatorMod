package eatyourbeets.characters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.resources.AnimatorResources;
import eatyourbeets.resources.AnimatorResources_Images;
import eatyourbeets.dungeons.CustomAbstractDungeon;
import eatyourbeets.resources.AnimatorResources_Strings;
import eatyourbeets.utilities.JavaUtilities;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.Synergy;
import eatyourbeets.relics.animator.LivingPicture;
import eatyourbeets.relics.animator.PurgingStone_Cards;
import eatyourbeets.relics.animator.TheMissingPiece;

import java.lang.reflect.Field;
import java.util.ArrayList;

public abstract class AnimatorCustomLoadout
{
    protected static final String[] trophyStrings = AnimatorResources_Strings.Trophies.TEXT;

    public static final Texture bronze = LoadTexture("Bronze");
    public static final Texture silver = LoadTexture("Silver");
    public static final Texture gold = LoadTexture("Gold");
    public static final Texture platinum = LoadTexture("Platinum");
    public static final Texture locked = LoadTexture("Locked");
    public static final Texture slot = LoadTexture("Slot1");
    public static final Texture slot2 = LoadTexture("Slot2");
    public static final Texture slot3 = LoadTexture("Slot3");

    public static AnimatorTrophies specialTrophies;

    private static Field goldField;
    private static Field hpField;

    public int ID;
    public String Name;
    public int StartingGold;
    public int MaxHP;
    public final int CardDraw;
    public final int OrbSlots;
    public boolean Locked;

    protected AnimatorTrophies trophies;
    protected String lockedDescription;
    protected String description;
    protected int unlockLevel;

    public static void LoadSpecialTrophies()
    {
        specialTrophies = GetTrophies(false, 0);
    }

    public void Refresh(int currentLevel, CharacterSelectScreen selectScreen, CharacterOption option)
    {
        try
        {
            if (goldField == null)
            {
                goldField = CharacterOption.class.getDeclaredField("gold");
                goldField.setAccessible(true);
            }
            if (hpField == null)
            {
                hpField = CharacterOption.class.getDeclaredField("hp");
                hpField.setAccessible(true);
            }
            //JavaUtilities.Logger.info("Gold FieldInfo: " + (goldField != null) + ", " + this.Name + ", " + this.StartingGold + ", Option: " + (option != null));
            goldField.set(option, this.StartingGold);
            hpField.set(option, String.valueOf(this.MaxHP));
        }
        catch (NoSuchFieldException | IllegalAccessException ex)
        {
            ex.printStackTrace();
        }

        trophies = GetTrophies(true, ID);
        selectScreen.bgCharImg = AnimatorResources_Images.GetCharacterPortrait(ID);
        Locked = unlockLevel > currentLevel;
        if (Locked)
        {
            lockedDescription =
                    AnimatorCharacterSelect.uiText[2] + unlockLevel +
                            AnimatorCharacterSelect.uiText[3] + currentLevel +
                            AnimatorCharacterSelect.uiText[4];
        }
    }

    public String GetDescription()
    {
        if (Locked)
        {
            return lockedDescription;
        }
        else
        {
            return description;
        }
    }

    public ArrayList<String> GetStartingRelics()
    {
        if (!UnlockTracker.isRelicSeen(LivingPicture.ID))
        {
            UnlockTracker.markRelicAsSeen(LivingPicture.ID);
        }
        if (!UnlockTracker.isRelicSeen(PurgingStone_Cards.ID))
        {
            UnlockTracker.markRelicAsSeen(PurgingStone_Cards.ID);
        }
        if (!UnlockTracker.isRelicSeen(TheMissingPiece.ID))
        {
            UnlockTracker.markRelicAsSeen(TheMissingPiece.ID);
        }

        ArrayList<String> res = new ArrayList<>();
        res.add(LivingPicture.ID);
        res.add(PurgingStone_Cards.ID);
        res.add(TheMissingPiece.ID);

        return res;
    }

    public abstract ArrayList<String> GetStartingDeck();

    public CharSelectInfo GetLoadout(String name, String description, AnimatorCharacter animatorCharacter)
    {
        return new CharSelectInfo(name + "-" + ID, description, MaxHP, MaxHP, OrbSlots, StartingGold, CardDraw, animatorCharacter,
                GetStartingRelics(), GetStartingDeck(), false);
    }

    protected AnimatorCustomLoadout()
    {
        this.MaxHP = 71;
        this.StartingGold = 99;
        this.OrbSlots = 3;
        this.CardDraw = 5;
    }

    protected String GetTrophyMessage(int trophy)
    {
        if (trophy == 1)
        {
            return trophyStrings[3];
        }
        else if (trophy == 2)
        {
            return trophyStrings[4];
        }
        else if (trophy == 3)
        {
            return trophyStrings[5];
        }

        return null;
    }

    public AnimatorTrophies GetTrophies(boolean flush)
    {
        return GetTrophies(flush, ID);
    }

    public static AnimatorTrophies GetTrophies(boolean flush, int id)
    {
        AnimatorTrophies selected = null;
        for (AnimatorTrophies trophyLevel : AnimatorMetrics.trophiesData)
        {
            if (trophyLevel.id == id)
            {
                selected = trophyLevel;
                break;
            }
        }

        if (selected == null)
        {
            JavaUtilities.Logger.info("Trophy not found");
            selected = new AnimatorTrophies(id);
            AnimatorMetrics.trophiesData.add(selected);
            AnimatorMetrics.SaveTrophies(flush);
        }

        return selected;
    }

    public static void UpdateSpecialTrophies(Hitbox trophySpecialHb)
    {
        trophySpecialHb.update();

        float offsetX = 60 * Settings.scale;
        float offsetY = 0 * Settings.scale;
        if (trophySpecialHb.hovered)
        {
            if (specialTrophies.trophy1 > 0)
            {
                TipHelper.renderGenericTip(trophySpecialHb.cX + offsetX, trophySpecialHb.cY + offsetY, trophyStrings[12], trophyStrings[11]);
            }
            else
            {
                TipHelper.renderGenericTip(trophySpecialHb.cX + offsetX, trophySpecialHb.cY + offsetY, trophyStrings[12], trophyStrings[10]);
            }
        }
    }

    public static void RenderSpecialTrophies(Hitbox trophySpecialHb, SpriteBatch sb)
    {
        //FontHelper.tipHeaderFont.getData().setScale(0.6f);

        String text = "";
        if (specialTrophies.trophy1 > 0)
        {
            text += " " + String.format("%.2f", CustomAbstractDungeon.GetUltraRareChance()) + "%";
        }

        RenderSpecialTrophy(trophySpecialHb, specialTrophies.trophy1, sb, text);

        //FontHelper.tipHeaderFont.getData().setScale(1);
    }

    public void RenderTrophies(Hitbox trophy1Hb, Hitbox trophy2Hb, Hitbox trophy3Hb, SpriteBatch sb)
    {
        FontHelper.tipHeaderFont.getData().setScale(0.6f);

        RenderTrophy(trophy1Hb, trophies.trophy1, bronze, sb);
        RenderTrophy(trophy2Hb, trophies.trophy2, silver, sb);
        RenderTrophy(trophy3Hb, trophies.trophy3, gold, sb);

        FontHelper.tipHeaderFont.getData().setScale(1);
    }

    public void UpdateTrophies(Hitbox trophy1Hb, Hitbox trophy2Hb, Hitbox trophy3Hb)
    {
        trophy1Hb.update();
        trophy2Hb.update();
        trophy3Hb.update();

        float offsetX = 60 * Settings.scale;
        float offsetY = 0 * Settings.scale;
        if (trophy1Hb.hovered)
        {
            TipHelper.renderGenericTip(trophy1Hb.cX + offsetX, trophy1Hb.cY + offsetY, trophyStrings[0], GetTrophyMessage(1));
        }
        else if (trophy2Hb.hovered)
        {
            TipHelper.renderGenericTip(trophy2Hb.cX + offsetX, trophy2Hb.cY + offsetY, trophyStrings[1], GetTrophyMessage(2));
        }
        else if (trophy3Hb.hovered)
        {
            TipHelper.renderGenericTip(trophy3Hb.cX + offsetX, trophy3Hb.cY + offsetY, trophyStrings[2], GetTrophyMessage(3));
        }
    }

    private static void RenderSpecialTrophy(Hitbox trophyHb, int trophyLevel, SpriteBatch sb, String trophyString)
    {
        float w = 64;
        float h = 64;
        float halfW = 32;
        float halfH = 32;

        if (!trophyHb.hovered)
        {
            sb.setColor(Color.LIGHT_GRAY);
        }
        else
        {
            sb.setColor(Color.WHITE);
        }
        sb.draw(slot3, trophyHb.x, trophyHb.y, halfW, halfH, w, h, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);

        Texture texture;
        if (trophyLevel <= 0)
        {
            texture = locked;
        }
        else
        {
            texture = platinum;
        }

        sb.setColor(Color.WHITE);
        sb.draw(texture, trophyHb.x, trophyHb.y, halfW, halfH, w, h, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);

        if (trophyLevel > 0)
        {
            FontHelper.renderFontCentered(sb, FontHelper.tipHeaderFont, trophyString, trophyHb.cX + (trophyHb.width * 1.3f * Settings.scale), trophyHb.cY, Settings.GOLD_COLOR);
        }
    }

    private static void RenderTrophy(Hitbox trophyHb, int trophyLevel, Texture texture, SpriteBatch sb)
    {
        Texture slotTexture = trophyLevel > 0 ? slot2 : slot;

        float w = 48;
        float h = 48;
        float halfW = 24;
        float halfH = 24;

        if (!trophyHb.hovered)
        {
            sb.setColor(Color.LIGHT_GRAY);
        }
        else
        {
            sb.setColor(Color.WHITE);
        }
        sb.draw(slotTexture, trophyHb.x, trophyHb.y, halfW, halfH, w, h, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);

        if (trophyLevel < 0)
        {
            texture = locked;
        }
        sb.setColor(Color.WHITE);
        sb.draw(texture, trophyHb.x, trophyHb.y, halfW, halfH, w, h, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);

        if (trophyLevel > 0)
        {
            FontHelper.renderFontCentered(sb, FontHelper.tipHeaderFont, trophyLevel + "/20", trophyHb.cX + (5 * Settings.scale), trophyHb.y, Settings.GOLD_COLOR);
        }
    }

    public void OnVictory(AnimatorCustomLoadout currentLoadout, int ascensionLevel)
    {
        if (trophies == null)
        {
            trophies = GetTrophies(false, ID);
        }

        if (AnimatorMetrics.lastLoadout == ID)
        {
            trophies.trophy1 = Math.max(trophies.trophy1, ascensionLevel);
        }

        ArrayList<String> cardsWithSynergy = new ArrayList<>();
        int synergyCount = 0;
        int uniqueCards = 0;

        ArrayList<AbstractCard> cards = AbstractDungeon.player.masterDeck.group;
        for (AbstractCard c : cards)
        {
            AnimatorCard card = JavaUtilities.SafeCast(c, AnimatorCard.class);
            if (card != null)
            {
                Synergy synergy = card.synergy;
                if (synergy != null && synergy.ID == ID)
                {
                    synergyCount += 1;
                    if (!cardsWithSynergy.contains(card.cardID) && card.rarity != AbstractCard.CardRarity.BASIC)
                    {
                        uniqueCards += 1;
                        cardsWithSynergy.add(card.cardID);
                    }
                }
            }
        }

        if (synergyCount >= cards.size() / 2)
        {
            trophies.trophy2 = Math.max(trophies.trophy2, ascensionLevel);
        }

        if (uniqueCards >= 8)
        {
            trophies.trophy3 = Math.max(trophies.trophy3, ascensionLevel);
        }
    }

    private static Texture LoadTexture(String name)
    {
        return new Texture(AnimatorResources.GetRewardImage(AnimatorResources.CreateID(name)));
    }
}