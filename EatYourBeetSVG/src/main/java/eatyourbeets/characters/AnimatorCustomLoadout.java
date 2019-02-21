package eatyourbeets.characters;

import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.AnimatorResources;
import eatyourbeets.Utilities;
import eatyourbeets.cards.animator.Defend;
import eatyourbeets.cards.animator.Strike;
import eatyourbeets.relics.LivingPicture;
import eatyourbeets.relics.PurgingStone;
import eatyourbeets.relics.TheMissingPiece;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class AnimatorCustomLoadout
{
    private static Field goldField;

    public int ID;
    public String Name;
    public int StartingGold;
    public final int CardDraw;
    public final int OrbSlots;
    public final int MaxHP;
    public boolean Locked;

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
            Utilities.Logger.info("Gold Field: " + (goldField != null) + ", " + this.Name + ", " + this.StartingGold + ", Option: " + (option != null));
            goldField.set(option, this.StartingGold);
        }
        catch (NoSuchFieldException | IllegalAccessException ex)
        {
            ex.printStackTrace();
        }

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

    public ArrayList<String> GetStartingDeck()
    {
        ArrayList<String> res = new ArrayList<>();
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Strike.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);
        res.add(Defend.ID);

        return res;
    }

    public CharSelectInfo GetLoadout(String name, String description, AnimatorCharacter animatorCharacter)
    {
        return new CharSelectInfo(name, description, MaxHP, MaxHP, OrbSlots, StartingGold, CardDraw, animatorCharacter,
                GetStartingRelics(), GetStartingDeck(), false);
    }

    protected AnimatorCustomLoadout()
    {
        this.MaxHP = 75;
        this.StartingGold = 99;
        this.OrbSlots = 3;
        this.CardDraw = 5;
    }
}
