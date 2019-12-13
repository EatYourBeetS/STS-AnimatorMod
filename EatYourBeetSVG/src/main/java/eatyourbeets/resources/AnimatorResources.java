package eatyourbeets.resources;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.*;
import eatyourbeets.characters.AnimatorCharacter;
import eatyourbeets.relics.UnnamedReign.AncientMedallion;
import eatyourbeets.relics.UnnamedReign.TheEgnaroPiece;
import eatyourbeets.relics.UnnamedReign.TheEruzaStone;
import eatyourbeets.relics.UnnamedReign.TheWolleyCore;
import eatyourbeets.relics.animator.*;
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
        else if (Settings.language == Settings.GameLanguage.ZHT)
        {
            languagePath = "localization/animator/zht/";
        }
        else if (Settings.language == Settings.GameLanguage.ZHS)
        {
            languagePath = "localization/animator/zhs/";
        }
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
    protected void InitializeCards()
    {
        LoadCustomCards("animator");
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
    protected void InitializeRelics()
    {
        BaseMod.addRelicToCustomPool(new LivingPicture(), AbstractEnums.Cards.THE_ANIMATOR);
        BaseMod.addRelicToCustomPool(new Rinne(), AbstractEnums.Cards.THE_ANIMATOR);
        BaseMod.addRelicToCustomPool(new ExquisiteBloodVial(), AbstractEnums.Cards.THE_ANIMATOR);
        BaseMod.addRelicToCustomPool(new TheMissingPiece(), AbstractEnums.Cards.THE_ANIMATOR);
        BaseMod.addRelicToCustomPool(new PurgingStone_Cards(), AbstractEnums.Cards.THE_ANIMATOR);
        BaseMod.addRelicToCustomPool(new WizardHat(), AbstractEnums.Cards.THE_ANIMATOR);
        BaseMod.addRelicToCustomPool(new Buoy(), AbstractEnums.Cards.THE_ANIMATOR);
        BaseMod.addRelicToCustomPool(new RacePiece(), AbstractEnums.Cards.THE_ANIMATOR);
        BaseMod.addRelicToCustomPool(new BattleDrones(), AbstractEnums.Cards.THE_ANIMATOR);
        BaseMod.addRelicToCustomPool(new CursedBlade(), AbstractEnums.Cards.THE_ANIMATOR);
        BaseMod.addRelicToCustomPool(new CursedGlyph(), AbstractEnums.Cards.THE_ANIMATOR);
        BaseMod.addRelicToCustomPool(new VividPicture(), AbstractEnums.Cards.THE_ANIMATOR);
        BaseMod.addRelicToCustomPool(new AlchemistGlove(), AbstractEnums.Cards.THE_ANIMATOR);
        BaseMod.addRelicToCustomPool(new OldCoffin(), AbstractEnums.Cards.THE_ANIMATOR);
        BaseMod.addRelicToCustomPool(new WornHelmet(), AbstractEnums.Cards.THE_ANIMATOR);
        BaseMod.addRelicToCustomPool(new HeavyHalberd(), AbstractEnums.Cards.THE_ANIMATOR);
        BaseMod.addRelicToCustomPool(new HallowedScabbard(), AbstractEnums.Cards.THE_ANIMATOR);
        BaseMod.addRelicToCustomPool(new EngravedStaff(), AbstractEnums.Cards.THE_ANIMATOR);
        BaseMod.addRelicToCustomPool(new CerealBox(), AbstractEnums.Cards.THE_ANIMATOR);
        BaseMod.addRelicToCustomPool(new ColorlessFragment(), AbstractEnums.Cards.THE_ANIMATOR);
        BaseMod.addRelicToCustomPool(new Hoodie(), AbstractEnums.Cards.THE_ANIMATOR);
        BaseMod.addRelicToCustomPool(new ShionDessert(), AbstractEnums.Cards.THE_ANIMATOR);
        BaseMod.addRelicToCustomPool(new TheEgnaroPiece(), AbstractEnums.Cards.THE_ANIMATOR);
        BaseMod.addRelicToCustomPool(new TheEruzaStone(), AbstractEnums.Cards.THE_ANIMATOR);
        BaseMod.addRelicToCustomPool(new TheWolleyCore(), AbstractEnums.Cards.THE_ANIMATOR);
        BaseMod.addRelicToCustomPool(new AncientMedallion(), AbstractEnums.Cards.THE_ANIMATOR);
        BaseMod.addRelicToCustomPool(new Readme(), AbstractEnums.Cards.THE_ANIMATOR);
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
        super.InitializePowers();

        //LoadCustomPowers("animator");
    }
}