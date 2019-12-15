package eatyourbeets.resources;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.*;
import eatyourbeets.characters.UnnamedCharacter;
import eatyourbeets.relics.unnamed.InfinitePower;
import patches.AbstractEnums;

import java.io.File;

public class UnnamedResources extends AbstractResources
{
    private static String languagePath = null;

    public static String CreateID(String suffix)
    {
        return "unnamed:" + suffix;
    }

    private static void LoadLanguagePath()
    {
        if (languagePath != null)
        {
            return;
        }

        String filePath = "c:/temp/unnamed-localization/";
        File f = new File(filePath);
        if(f.exists() && f.isDirectory())
        {
            languagePath = filePath;
        }
        else
        {
            languagePath = "localization/unnamed/eng/";
        }
    }

    @Override
    protected void InitializeColor()
    {
        Color color = CardHelper.getColor(60, 77, 106);

        BaseMod.addColor(AbstractEnums.Cards.THE_UNNAMED, color, color, color, color, color, color, color,
                UnnamedResources_Images.ATTACK_PNG,  UnnamedResources_Images.SKILL_PNG ,    UnnamedResources_Images.POWER_PNG ,
                UnnamedResources_Images.ORB_1A_PNG,  UnnamedResources_Images.ATTACK_P_PNG , UnnamedResources_Images.SKILL_P_PNG ,
                UnnamedResources_Images.POWER_P_PNG, UnnamedResources_Images.ORB_1B_PNG,    UnnamedResources_Images.ORB_1C_PNG);
    }

    @Override
    protected void InitializeCharacter()
    {
        UnnamedCharacter unnamedCharacter = new UnnamedCharacter(UnnamedCharacter.NAME, AbstractEnums.Characters.THE_UNNAMED);
        BaseMod.addCharacter(unnamedCharacter, UnnamedResources_Images.CHAR_BUTTON_PNG, UnnamedResources_Images.CHAR_PORTRAIT_JPG, AbstractEnums.Characters.THE_UNNAMED);
    }

    @Override
    protected void InitializeCards()
    {
        LoadCustomCards("unnamed");
    }

    @Override
    protected void InitializeStrings()
    {
        LoadLanguagePath();

        BaseMod.loadCustomStringsFile(CharacterStrings.class, languagePath + "CharacterStrings.json");
        BaseMod.loadCustomStringsFile(CardStrings.class, languagePath + "CardStrings.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, languagePath + "RelicStrings.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, languagePath + "PowerStrings.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, languagePath + "UIStrings.json");
    }

    @Override
    protected void InitializeRelics()
    {
        BaseMod.addRelicToCustomPool(new InfinitePower(), AbstractEnums.Cards.THE_UNNAMED);
    }

    @Override
    protected void InitializeKeywords()
    {
        LoadKeywords(languagePath + "KeywordStrings.json");
    }

    @Override
    protected void PostInitialize()
    {
        super.PostInitialize();

        //TODO: Remove this
//        for (AbstractCard card : CardLibrary.getAllCards())
//        {
//            UnlockTracker.markCardAsSeen(card.cardID);
//        }
        //
    }
}