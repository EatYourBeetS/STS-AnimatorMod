package eatyourbeets.resources.animator;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.characters.AnimatorCharacter;
import eatyourbeets.potions.FalseLifePotion;
import eatyourbeets.potions.GrowthPotion;
import eatyourbeets.resources.AbstractResources;
import eatyourbeets.rewards.animator.SpecialGoldReward;
import eatyourbeets.rewards.animator.SynergyCardsReward;
import eatyourbeets.ui.animator.seriesSelection.AnimatorLoadoutsContainer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class AnimatorResources extends AbstractResources
{
    private SpireConfig config;

    public final static String ID = "animator";
    public final AbstractCard.CardColor CardColor = Enums.Cards.THE_ANIMATOR;
    public final AbstractPlayer.PlayerClass PlayerClass = Enums.Characters.THE_ANIMATOR;
    public final AnimatorDungeonData Dungeon = AnimatorDungeonData.Register(CreateID("Data"));
    public final AnimatorPlayerData Data = new AnimatorPlayerData();
    public final AnimatorStrings Strings = new AnimatorStrings();
    public final AnimatorImages Images = new AnimatorImages();

    public AnimatorResources()
    {
        super(ID);
    }

    public int GetUnlockLevel()
    {
        return UnlockTracker.getUnlockLevel(PlayerClass);
    }

    public SpireConfig GetConfig()
    {
        try
        {
            if (config == null)
            {
                config = new SpireConfig("TheAnimator", "TheAnimatorConfig");
            }

            return config;
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public boolean SaveConfig()
    {
        SpireConfig config = GetConfig();

        try
        {
            config.save();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    protected void InitializeStrings()
    {
        LoadCustomStrings(OrbStrings.class);
        LoadCustomStrings(CharacterStrings.class);

        BaseMod.loadCustomStrings(CardStrings.class, ProcessJson(Gdx.files.internal(GetLanguagePath() + "CardStrings.json").readString(StandardCharsets.UTF_8.name())));

        LoadCustomStrings(RelicStrings.class);
        LoadCustomStrings(PowerStrings.class);
        LoadCustomStrings(UIStrings.class);
        LoadCustomStrings(EventStrings.class);
        LoadCustomStrings(PotionStrings.class);
        LoadCustomStrings(MonsterStrings.class);
        LoadCustomStrings(BlightStrings.class);
    }

    @Override
    protected void InitializeColor()
    {
        Color color = CardHelper.getColor(210, 147, 106);
        BaseMod.addColor(CardColor, color, color, color, color, color, color, color,
            Images.ATTACK_PNG, Images.SKILL_PNG, Images.POWER_PNG,
            Images.ORB_A_PNG, Images.ATTACK_P_PNG, Images.SKILL_P_PNG,
            Images.POWER_P_PNG, Images.ORB_B_PNG, Images.ORB_C_PNG);
    }

    @Override
    protected void InitializeCharacter()
    {
        BaseMod.addCharacter(new AnimatorCharacter(), Images.CHAR_BUTTON_PNG, Images.CHAR_PORTRAIT_JPG, PlayerClass);
    }

    @Override
    protected void InitializeCards()
    {
        Strings.Initialize();
        LoadCustomCards();
    }

    @Override
    protected void InitializeRelics()
    {
        LoadCustomRelics();
    }

    @Override
    protected void InitializeKeywords()
    {
        LoadKeywords();
    }

    @Override
    protected void InitializePowers()
    {
        //LoadCustomPowers();
    }

    @Override
    protected void InitializePotions()
    {
        BaseMod.addPotion(FalseLifePotion.class, Color.GOLDENROD.cpy(), Color.WHITE.cpy(), Color.GOLDENROD.cpy(),
        FalseLifePotion.POTION_ID, Enums.Characters.THE_ANIMATOR);

        BaseMod.addPotion(GrowthPotion.class, Color.NAVY.cpy(), Color.FOREST.cpy(), Color.YELLOW.cpy(),
        GrowthPotion.POTION_ID, Enums.Characters.THE_ANIMATOR);
    }

    @Override
    protected void InitializeRewards()
    {
        SynergyCardsReward.Serializer synergySerializer = new SynergyCardsReward.Serializer();
        BaseMod.registerCustomReward(Enums.Rewards.SYNERGY_CARDS, synergySerializer, synergySerializer);

        SpecialGoldReward.Serializer goldSerializer = new SpecialGoldReward.Serializer();
        BaseMod.registerCustomReward(Enums.Rewards.SPECIAL_GOLD, goldSerializer, goldSerializer);
    }

    @Override
    protected void PostInitialize()
    {
        Data.Initialize();
        AnimatorLoadoutsContainer.PreloadResources();
    }

    @Override
    protected String GetLanguagePath(Settings.GameLanguage language)
    {
        if (language != Settings.GameLanguage.ZHT && language != Settings.GameLanguage.ZHS)
        {
            language = Settings.GameLanguage.ENG;
        }

        return super.GetLanguagePath(language);
    }

    public String ProcessJson(String originalString)
    {
        String shortcutsJson = Gdx.files.internal(GetLanguagePath() + "CardStringsShortcuts.json").readString(String.valueOf(StandardCharsets.UTF_8));

        Map<String, String> items = new Gson().fromJson(shortcutsJson, new TypeToken<Map<String, String>>(){}.getType());

        int size = items.size();
        String[] shortcuts = items.keySet().toArray(new String[size]);
        String[] replacement = items.values().toArray(new String[size]);

        return StringUtils.replaceEach(originalString, shortcuts, replacement);
    }
}