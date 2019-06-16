package eatyourbeets.resources;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import eatyourbeets.characters.UnnamedCharacter;
import eatyourbeets.relics.unnamed.InfinitePower;
import patches.AbstractEnums;

public class Resources_Unnamed extends AbstractResources
{
    @Override
    protected void InitializeColor()
    {
        Color color = CardHelper.getColor(60, 77, 106);

        BaseMod.addColor(AbstractEnums.Cards.THE_UNNAMED, color, color, color, color, color, color, color,
                Resources_Unnamed_Images.ATTACK_PNG,  Resources_Unnamed_Images.SKILL_PNG ,    Resources_Unnamed_Images.POWER_PNG ,
                Resources_Unnamed_Images.ORB_A_PNG ,  Resources_Unnamed_Images.ATTACK_P_PNG , Resources_Unnamed_Images.SKILL_P_PNG ,
                Resources_Unnamed_Images.POWER_P_PNG, Resources_Unnamed_Images.ORB_B_PNG ,    Resources_Unnamed_Images.ORB_C_PNG);
    }

    @Override
    protected void InitializeCharacter()
    {
        UnnamedCharacter unnamedCharacter = new UnnamedCharacter(UnnamedCharacter.NAME, AbstractEnums.Characters.THE_UNNAMED);
        BaseMod.addCharacter(unnamedCharacter, Resources_Unnamed_Images.CHAR_BUTTON_PNG, Resources_Unnamed_Images.CHAR_PORTRAIT_JPG, AbstractEnums.Characters.THE_UNNAMED);
    }

    @Override
    protected void InitializeCards()
    {
        LoadCustomCards("unnamed");
    }

    @Override
    protected void InitializeStrings()
    {
        BaseMod.loadCustomStringsFile(CharacterStrings.class, "localization/unnamed/eng/CharacterStrings.json");
        BaseMod.loadCustomStringsFile(CardStrings.class, "localization/unnamed/eng/CardStrings.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, "localization/unnamed/eng/RelicStrings.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, "localization/unnamed/eng/PowerStrings.json");
    }

    @Override
    protected void InitializeRelics()
    {
        BaseMod.addRelicToCustomPool(new InfinitePower(), AbstractEnums.Cards.THE_UNNAMED);
    }

    @Override
    protected void InitializeKeywords()
    {

    }
}