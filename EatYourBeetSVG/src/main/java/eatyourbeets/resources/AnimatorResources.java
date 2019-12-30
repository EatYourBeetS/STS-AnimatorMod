package eatyourbeets.resources;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.*;
import eatyourbeets.characters.AnimatorCharacter;
import patches.AbstractEnums;

import java.io.File;

public class AnimatorResources extends AbstractResources
{
    private static String languagePath = null;

    public static String CreateID(String suffix)
    {
        return "animator:" + suffix;
    }

    private static void LoadLanguagePath()
    {
        if (languagePath != null)
        {
            return;
        }

        String filePath = "c:/temp/animator-localization/";
        File f = new File(filePath);
        if(f.exists() && f.isDirectory())
        {
            languagePath = filePath;
        }
//        else if (Settings.language == Settings.GameLanguage.ZHT)
//        {
//            languagePath = "localization/animator/zht/";
//        }
//        else if (Settings.language == Settings.GameLanguage.ZHS)
//        {
//            languagePath = "localization/animator/zhs/";
//        }
//        else if (Settings.language == Settings.GameLanguage.KOR)
//        {
//            languagePath = "localization/animator/kor/";
//        }
//        else if (Settings.language == Settings.GameLanguage.FRA)
//        {
//            languagePath = "localization/animator/fra/";
//        }
        else
        {
            languagePath = "localization/animator/eng/";
        }
    }

    @Override
    protected void InitializeColor()
    {
        Color color = CardHelper.getColor(210, 147, 106);
        BaseMod.addColor(AbstractEnums.Cards.THE_ANIMATOR, color, color, color, color, color, color, color,
        AnimatorResources_Images.ATTACK_PNG,  AnimatorResources_Images.SKILL_PNG ,    AnimatorResources_Images.POWER_PNG ,
        AnimatorResources_Images.ORB_A_PNG ,  AnimatorResources_Images.ATTACK_P_PNG , AnimatorResources_Images.SKILL_P_PNG ,
        AnimatorResources_Images.POWER_P_PNG, AnimatorResources_Images.ORB_B_PNG ,    AnimatorResources_Images.ORB_C_PNG);
    }

    @Override
    protected void InitializeCharacter()
    {
        AnimatorCharacter animatorCharacter = new AnimatorCharacter(AnimatorCharacter.NAME, AbstractEnums.Characters.THE_ANIMATOR);
        BaseMod.addCharacter(animatorCharacter, AnimatorResources_Images.CHAR_BUTTON_PNG, AnimatorResources_Images.CHAR_PORTRAIT_JPG, AbstractEnums.Characters.THE_ANIMATOR);
    }

    @Override
    protected void InitializeStrings()
    {
        LoadLanguagePath();

        BaseMod.loadCustomStringsFile(OrbStrings.class, languagePath + "OrbStrings.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, languagePath + "CharacterStrings.json");
        BaseMod.loadCustomStringsFile(CardStrings.class, languagePath + "CardStrings.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, languagePath + "RelicStrings.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, languagePath + "PowerStrings.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, languagePath + "UIStrings.json");
        BaseMod.loadCustomStringsFile(EventStrings.class, languagePath + "EventStrings.json");
        BaseMod.loadCustomStringsFile(PotionStrings.class, languagePath + "PotionStrings.json");
        BaseMod.loadCustomStringsFile(MonsterStrings.class, languagePath + "MonsterStrings.json");
        BaseMod.loadCustomStringsFile(BlightStrings.class, languagePath + "BlightStrings.json");
    }

    @Override
    protected void InitializeCards()
    {
        LoadCustomCards("animator");
    }

    @Override
    protected void InitializeRelics()
    {
        LoadCustomRelics("animator");
    }

    @Override
    protected void InitializeKeywords()
    {
        LoadKeywords(languagePath + "KeywordStrings.json");
        LoadDynamicKeywords(languagePath + "DynamicKeywordStrings.json");
    }

    @Override
    protected void InitializePowers()
    {
        //LoadCustomPowers("animator");
    }
}