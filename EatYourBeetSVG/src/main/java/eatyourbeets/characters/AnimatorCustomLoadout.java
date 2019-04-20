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
import eatyourbeets.AnimatorResources;
import eatyourbeets.Utilities;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergy;
import eatyourbeets.relics.LivingPicture;
import eatyourbeets.relics.PurgingStone;
import eatyourbeets.relics.TheMissingPiece;

import java.lang.reflect.Field;
import java.util.ArrayList;

public abstract class AnimatorCustomLoadout
{
    protected static final String[] trophyStrings = AnimatorResources.GetUIStrings(AnimatorResources.UIStringType.Trophies).TEXT;
    protected static final Texture bronze = new Texture(AnimatorResources.GetRewardImage("Animator_Bronze"));
    protected static final Texture silver = new Texture(AnimatorResources.GetRewardImage("Animator_Silver"));
    protected static final Texture gold = new Texture(AnimatorResources.GetRewardImage("Animator_Gold"));
    protected static final Texture locked = new Texture(AnimatorResources.GetRewardImage("Animator_Locked"));
    protected static final Texture slot = new Texture(AnimatorResources.GetRewardImage("Animator_Slot"));
    protected static final Texture slot2 = new Texture(AnimatorResources.GetRewardImage("Animator_Slot2"));

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
            //Utilities.Logger.info("Gold Field: " + (goldField != null) + ", " + this.Name + ", " + this.StartingGold + ", Option: " + (option != null));
            goldField.set(option, this.StartingGold);
            hpField.set(option, String.valueOf(this.MaxHP));
        }
        catch (NoSuchFieldException | IllegalAccessException ex)
        {
            ex.printStackTrace();
        }

        trophies = GetTrophies(true);
        selectScreen.bgCharImg = AnimatorResources.GetCharacterPortrait(ID);
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
        if (!UnlockTracker.isRelicSeen(PurgingStone.ID))
        {
            UnlockTracker.markRelicAsSeen(PurgingStone.ID);
        }
        if (!UnlockTracker.isRelicSeen(TheMissingPiece.ID))
        {
            UnlockTracker.markRelicAsSeen(TheMissingPiece.ID);
        }

        ArrayList<String> res = new ArrayList<>();
        res.add(LivingPicture.ID);
        res.add(PurgingStone.ID);
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
        this.MaxHP = 75;
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
        AnimatorTrophies selected = null;
        for (AnimatorTrophies trophyLevel : AnimatorMetrics.trophiesData)
        {
            if (trophyLevel.id == this.ID)
            {
                selected = trophyLevel;
                break;
            }
        }

        if (selected == null)
        {
            Utilities.Logger.info("Trophy not found");
            selected = new AnimatorTrophies(this.ID);
            AnimatorMetrics.trophiesData.add(selected);
            AnimatorMetrics.SaveTrophies(flush);
        }

        return selected;
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

    private void RenderTrophy(Hitbox trophyHb, int trophyLevel, Texture texture, SpriteBatch sb)
    {
        Texture slotTexture = trophyLevel > 0 ? slot2 : slot;

        if (!trophyHb.hovered)
        {
            sb.setColor(Color.LIGHT_GRAY);
        }
        else
        {
            sb.setColor(Color.WHITE);
        }
        sb.draw(slotTexture, trophyHb.x, trophyHb.y, 24.0F, 24.0F, 48.0F, 48.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);

        if (trophyLevel < 0)
        {
            texture = locked;
        }
        sb.setColor(Color.WHITE);
        sb.draw(texture, trophyHb.x, trophyHb.y, 24.0F, 24.0F, 48.0F, 48.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);

        if (trophyLevel > 0)
        {
            FontHelper.renderFontCentered(sb, FontHelper.tipHeaderFont, trophyLevel + "/20", trophyHb.cX + (5 * Settings.scale), trophyHb.y, Settings.GOLD_COLOR);
        }
    }

    public void OnTrueVictory(AnimatorCustomLoadout currentLoadout, int ascensionLevel)
    {
        if (trophies == null)
        {
            trophies = GetTrophies(false);
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
            AnimatorCard card = Utilities.SafeCast(c, AnimatorCard.class);
            if (card != null)
            {
                Synergy synergy = card.GetSynergy();
                if (synergy != null && synergy.ID == ID)
                {
                    synergyCount += 1;
                    if (!cardsWithSynergy.contains(card.cardID))
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
}