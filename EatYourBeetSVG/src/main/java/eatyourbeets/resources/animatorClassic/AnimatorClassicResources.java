package eatyourbeets.resources.animatorClassic;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.*;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardMetadata;
import eatyourbeets.characters.AnimatorClassicCharacter;
import eatyourbeets.potions.FalseLifePotion;
import eatyourbeets.potions.GrowthPotion;
import eatyourbeets.powers.affinity.animatorClassic.*;
import eatyourbeets.resources.AbstractResources;
import eatyourbeets.resources.CardTooltips;
import eatyourbeets.resources.GR;
import eatyourbeets.resources.animator.AnimatorImages;
import eatyourbeets.rewards.animator.MissingPieceReward;
import eatyourbeets.rewards.animator.SpecialCardReward;
import eatyourbeets.rewards.animator.SpecialGoldReward;
import eatyourbeets.utilities.JUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class AnimatorClassicResources extends AbstractResources
{
    public static final String ID = "animatorClassic";

    public final String OfficialName = "Animator (classic)";
//    public final AnimatorDungeonData Dungeon = AnimatorDungeonData.Register(CreateID("Data"));
//    public final AnimatorPlayerData Data = new AnimatorPlayerData();
//    public final AnimatorConfig Config = new AnimatorConfig();
    public final AnimatorClassicStrings Strings = new AnimatorClassicStrings();
    public final AnimatorImages Images = new AnimatorImages();
    public final Color RenderColor = CardHelper.getColor(210, 147, 106);
    public Map<String, EYBCardMetadata> CardData;

    public AnimatorClassicResources()
    {
        super(ID, Enums.Cards.THE_ANIMATOR_CLASSIC, Enums.Characters.THE_ANIMATOR_CLASSIC, new AnimatorClassicCardLibrary());
    }

    @Override
    protected void InitializeStrings()
    {
        if (isLoaded)
        {
            return;
        }

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

        String jsonString = new String(Gdx.files.internal("AnimatorClassic-CardMetadata.json").readBytes());
        CardData = new Gson().fromJson(jsonString, new TypeToken<Map<String, EYBCardMetadata>>(){}.getType());

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
        if (isLoaded)
        {
            return;
        }

        final Color color = RenderColor.cpy();
        BaseMod.addColor(CardColor, color, color, color, color, color, color, color,
        Images.ATTACK_PNG, Images.SKILL_PNG, Images.POWER_PNG,
        Images.ORB_A_PNG, Images.ATTACK_PNG, Images.SKILL_PNG,
        Images.POWER_PNG, Images.ORB_B_PNG, Images.ORB_C_PNG);
    }

    @Override
    protected void InitializeCharacter()
    {
        if (isLoaded)
        {
            return;
        }

        BaseMod.addCharacter(new AnimatorClassicCharacter(), Images.CHAR_BUTTON_CLASSIC_PNG, Images.CHAR_PORTRAIT_EMPTY_PNG, PlayerClass);
    }

    @Override
    protected void InitializeCards()
    {
        if (isLoaded)
        {
            return;
        }

        JUtils.LogInfo(this, "InitializeCards();");

        Strings.Initialize();
        CardSeries.InitializeStrings();
        LoadCustomCards();
        CardLibrary.Initialize(this);
        EYBCardData.PostInitialize();
    }

    @Override
    protected void InitializeRelics()
    {
        if (isLoaded)
        {
            return;
        }

        JUtils.LogInfo(this, "InitializeRelics();");

        LoadCustomRelics();
    }

    @Override
    protected void InitializeKeywords()
    {
        if (isLoaded)
        {
            return;
        }

        JUtils.LogInfo(this, "InitializeKeywords();");

        LoadKeywords(PlayerClass);

        CardTooltips.RegisterPowerTooltip(GR.AnimatorClassic.PlayerClass, "[F]", "Force", new ForcePower());
        CardTooltips.RegisterPowerTooltip(GR.AnimatorClassic.PlayerClass, "[A]", "Agility", new AgilityPower());
        CardTooltips.RegisterPowerTooltip(GR.AnimatorClassic.PlayerClass, "[I]", "Intellect", new IntellectPower());
        CardTooltips.RegisterPowerTooltip(GR.AnimatorClassic.PlayerClass, "[B]", "Blessing", new BlessingPower());
        CardTooltips.RegisterPowerTooltip(GR.AnimatorClassic.PlayerClass, "[C]", "Corruption", new CorruptionPower());
    }

    @Override
    protected void InitializePowers()
    {
        // LoadCustomPowers();
    }

    @Override
    protected void InitializePotions()
    {
        if (isLoaded)
        {
            return;
        }

        BaseMod.addPotion(FalseLifePotion.class, Color.GOLDENROD.cpy(), Color.WHITE.cpy(), Color.GOLDENROD.cpy(),
        FalseLifePotion.POTION_ID, Enums.Characters.THE_ANIMATOR);

        BaseMod.addPotion(GrowthPotion.class, Color.NAVY.cpy(), Color.FOREST.cpy(), Color.YELLOW.cpy(),
        GrowthPotion.POTION_ID, Enums.Characters.THE_ANIMATOR);
    }

    @Override
    protected void InitializeRewards()
    {
        if (isLoaded)
        {
            return;
        }

        MissingPieceReward.Serializer synergySerializer = new MissingPieceReward.Serializer();
        BaseMod.registerCustomReward(Enums.Rewards.SYNERGY_CARDS, synergySerializer, synergySerializer);

        SpecialGoldReward.Serializer goldSerializer = new SpecialGoldReward.Serializer();
        BaseMod.registerCustomReward(Enums.Rewards.SPECIAL_GOLD, goldSerializer, goldSerializer);

        SpecialCardReward.Serializer cardSerializer = new SpecialCardReward.Serializer();
        BaseMod.registerCustomReward(Enums.Rewards.SPECIAL_CARDS, cardSerializer, cardSerializer);
    }

    @Override
    protected void PostInitialize()
    {
        isLoaded = true;
    }
}