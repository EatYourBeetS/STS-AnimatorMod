package eatyourbeets.resources.animator;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardMetadata;
import eatyourbeets.characters.AnimatorCharacter;
import eatyourbeets.potions.FalseLifePotion;
import eatyourbeets.potions.GrowthPotion;
import eatyourbeets.resources.AbstractResources;
import eatyourbeets.rewards.animator.MissingPieceReward;
import eatyourbeets.rewards.animator.SpecialGoldReward;
import eatyourbeets.ui.animator.seriesSelection.AnimatorLoadoutsContainer;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class AnimatorResources extends AbstractResources
{
    public final static String ID = "animator";

    public final String OfficialName = "Animator (redesign)"; // Don't change this
    public final AbstractCard.CardColor CardColor = Enums.Cards.THE_ANIMATOR;
    public final AbstractPlayer.PlayerClass PlayerClass = Enums.Characters.THE_ANIMATOR;
    public final AnimatorDungeonData Dungeon = AnimatorDungeonData.Register(CreateID("Data"));
    public final AnimatorPlayerData Data = new AnimatorPlayerData();
    public final AnimatorStrings Strings = new AnimatorStrings();
    public final AnimatorImages Images = new AnimatorImages();
    public final AnimatorConfig Config = new AnimatorConfig();
    public Map<String, EYBCardMetadata> CardData;

    public AnimatorResources()
    {
        super(ID);
    }

    public int GetUnlockLevel()
    {
        return UnlockTracker.getUnlockLevel(PlayerClass);
    }

    public int GetUnlockCost()
    {
        return GetUnlockCost(0, true);
    }

    public int GetUnlockCost(int level, boolean relative)
    {
        if (relative)
        {
            level += GetUnlockLevel();
        }

        return level <= 4 ? 300 + (level * 500) : 1000 + (level * 300);
    }

    public boolean IsSelected()
    {
        return GameUtilities.IsPlayerClass(PlayerClass);
    }

    @Override
    protected void InitializeStrings()
    {
        JUtils.LogInfo(this, "InitializeStrings();");

        LoadCustomStrings(OrbStrings.class);
        LoadCustomStrings(CharacterStrings.class);

        String json = GetFallbackFile("CardStrings.json").readString(StandardCharsets.UTF_8.name());
        LoadGroupedCardStrings(ProcessJson(json, true));

        if (testFolder.isDirectory() || IsTranslationSupported(Settings.language))
        {
            FileHandle file = GetFile(Settings.language, "CardStrings.json");
            if (file.exists())
            {
                String json2 = file.readString(StandardCharsets.UTF_8.name());
                LoadGroupedCardStrings(ProcessJson(json2, false));
            }
        }

        String jsonString = new String(Gdx.files.internal("Animator-CardMetadata.json").readBytes());
        CardData = new Gson().fromJson(jsonString, new TypeToken<Map<String, EYBCardMetadata>>(){}.getType());

        LoadCustomStrings(RelicStrings.class);
        LoadCustomStrings(PowerStrings.class);
        LoadCustomStrings(UIStrings.class);
        LoadCustomStrings(EventStrings.class);
        LoadCustomStrings(PotionStrings.class);
        LoadCustomStrings(MonsterStrings.class);
        LoadCustomStrings(BlightStrings.class);
        LoadCustomStrings(RunModStrings.class);
    }

    @Override
    protected void InitializeColor()
    {
        Color color = CardHelper.getColor(210, 147, 106);
        BaseMod.addColor(CardColor, color, color, color, color, color, color, color,
        Images.ATTACK_PNG, Images.SKILL_PNG, Images.POWER_PNG,
        Images.ORB_A_PNG, Images.ATTACK_PNG_L, Images.SKILL_PNG_L,
        Images.POWER_PNG_L, Images.ORB_B_PNG, Images.ORB_C_PNG);
    }

    @Override
    protected void InitializeCharacter()
    {
        BaseMod.addCharacter(new AnimatorCharacter(), Images.CHAR_BUTTON_PNG, Images.CHAR_PORTRAIT_JPG, PlayerClass);
    }

    @Override
    protected void InitializeCards()
    {
        JUtils.LogInfo(this, "InitializeCards();");

        Strings.Initialize();
        CardSeries.InitializeStrings();
        LoadCustomCards();
        EYBCardData.PostInitialize();
    }

    @Override
    protected void InitializeRelics()
    {
        JUtils.LogInfo(this, "InitializeRelics();");

        LoadCustomRelics();
    }

    @Override
    protected void InitializeKeywords()
    {
        JUtils.LogInfo(this, "InitializeKeywords();");

        LoadKeywords();
    }

    @Override
    protected void InitializePowers()
    {
        // LoadCustomPowers();
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
        MissingPieceReward.Serializer synergySerializer = new MissingPieceReward.Serializer();
        BaseMod.registerCustomReward(Enums.Rewards.SYNERGY_CARDS, synergySerializer, synergySerializer);

        SpecialGoldReward.Serializer goldSerializer = new SpecialGoldReward.Serializer();
        BaseMod.registerCustomReward(Enums.Rewards.SPECIAL_GOLD, goldSerializer, goldSerializer);
    }

    @Override
    protected void PostInitialize()
    {
        Config.Load(CardCrawlGame.saveSlot);
        Data.Initialize();
        Config.InitializeOptions();
        AnimatorLoadoutsContainer.PreloadResources();
        AnimatorImages.PreloadResources();
    }

    public String ProcessJson(String originalString, boolean useFallback)
    {
        final String path = "CardStringsShortcuts.json";
        final FileHandle file = useFallback ? GetFallbackFile(path) : GetFile(Settings.language, path);

        if (!file.exists())
        {
            return originalString;
        }

        String shortcutsJson = file.readString(String.valueOf(StandardCharsets.UTF_8));
        Map<String, String> items = new Gson().fromJson(shortcutsJson, new TypeToken<Map<String, String>>(){}.getType());

        int size = items.size();
        String[] shortcuts = items.keySet().toArray(new String[size]);
        String[] replacement = items.values().toArray(new String[size]);

        return StringUtils.replaceEach(originalString, shortcuts, replacement);
    }
}