package eatyourbeets.resources.unnamed;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.*;
import eatyourbeets.characters.UnnamedCharacter;
import eatyourbeets.relics.unnamed.InfinitePower;
import eatyourbeets.resources.AbstractResources;

import java.io.File;

public class UnnamedResources extends AbstractResources
{
    public final static String ID = "unnamed";
    private static String languagePath = null;

    public UnnamedResources()
    {
        super(ID);
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

        BaseMod.addColor(Enums.Cards.THE_UNNAMED, color, color, color, color, color, color, color,
                UnnamedImages.ATTACK_PNG,  UnnamedImages.SKILL_PNG ,    UnnamedImages.POWER_PNG ,
                UnnamedImages.ORB_1A_PNG,  UnnamedImages.ATTACK_P_PNG , UnnamedImages.SKILL_P_PNG ,
                UnnamedImages.POWER_P_PNG, UnnamedImages.ORB_1B_PNG,    UnnamedImages.ORB_1C_PNG);
    }

    @Override
    protected void InitializeCharacter()
    {
        UnnamedCharacter unnamedCharacter = new UnnamedCharacter(UnnamedCharacter.NAME, Enums.Characters.THE_UNNAMED);
        BaseMod.addCharacter(unnamedCharacter, UnnamedImages.CHAR_BUTTON_PNG, UnnamedImages.CHAR_PORTRAIT_JPG, Enums.Characters.THE_UNNAMED);
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
        BaseMod.addRelicToCustomPool(new InfinitePower(), Enums.Cards.THE_UNNAMED);
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