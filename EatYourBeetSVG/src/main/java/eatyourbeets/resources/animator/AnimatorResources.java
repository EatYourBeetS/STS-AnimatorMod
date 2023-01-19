package eatyourbeets.resources.animator;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.*;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardMetadata;
import eatyourbeets.characters.AnimatorCharacter;
import eatyourbeets.potions.FalseLifePotion;
import eatyourbeets.potions.GrowthPotion;
import eatyourbeets.powers.affinity.animator.*;
import eatyourbeets.resources.AbstractResources;
import eatyourbeets.resources.CardTooltips;
import eatyourbeets.resources.GR;
import eatyourbeets.rewards.animator.MissingPieceReward;
import eatyourbeets.rewards.animator.SpecialCardReward;
import eatyourbeets.rewards.animator.SpecialGoldReward;
import eatyourbeets.ui.animator.seriesSelection.AnimatorLoadoutsContainer;
import eatyourbeets.utilities.JUtils;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class AnimatorResources extends AbstractResources
{
    public static final String ID = "animator";

    public final String OfficialName = "Animator";
    public final AnimatorDungeonData Dungeon = AnimatorDungeonData.Register(CreateID("Data"));
    public final AnimatorPlayerData Data = new AnimatorPlayerData();
    public final AnimatorStrings Strings = new AnimatorStrings();
    public final AnimatorImages Images = new AnimatorImages();
    public final AnimatorConfig Config = new AnimatorConfig();
    public final Color RenderColor = CardHelper.getColor(210, 147, 106);
    public Map<String, EYBCardMetadata> CardData;

    public AnimatorResources()
    {
        super(ID, Enums.Cards.THE_ANIMATOR, Enums.Characters.THE_ANIMATOR, new AnimatorCardLibrary());
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

        LoadCardStrings(CARD_FILE);

        String jsonString = new String(Gdx.files.internal("Animator-CardMetadata.json").readBytes());
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

        BaseMod.addCharacter(new AnimatorCharacter(), Images.CHAR_BUTTON_PNG, Images.CHAR_PORTRAIT_EMPTY_PNG, PlayerClass);
    }

    @Override
    protected void InitializeCards()
    {
        if (isLoaded)
        {
            return;
        }

        JUtils.LogInfo(this, "InitializeCards();");

        CardTooltips.RegisterPowerTooltip(GR.Animator.PlayerClass, "[Force]", "Force", new ForcePower());
        CardTooltips.RegisterPowerTooltip(GR.Animator.PlayerClass, "[Agility]", "Agility", new AgilityPower());
        CardTooltips.RegisterPowerTooltip(GR.Animator.PlayerClass, "[Intellect]", "Intellect", new IntellectPower());
        CardTooltips.RegisterPowerTooltip(GR.Animator.PlayerClass, "[Blessing]", "Blessing", new BlessingPower());
        CardTooltips.RegisterPowerTooltip(GR.Animator.PlayerClass, "[Corruption]", "Corruption", new CorruptionPower());

        CardTooltips.RegisterName(GR.Animator.PlayerClass, "[R]", GR.Tooltips.Affinity_Red);
        CardTooltips.RegisterName(GR.Animator.PlayerClass, "[G]", GR.Tooltips.Affinity_Green);
        CardTooltips.RegisterName(GR.Animator.PlayerClass, "[B]", GR.Tooltips.Affinity_Blue);
        CardTooltips.RegisterName(GR.Animator.PlayerClass, "[L]", GR.Tooltips.Affinity_Light);
        CardTooltips.RegisterName(GR.Animator.PlayerClass, "[D]", GR.Tooltips.Affinity_Dark);
        CardTooltips.RegisterName(GR.Animator.PlayerClass, "[M]", GR.Tooltips.Affinity_Star);
        CardTooltips.RegisterName(GR.Animator.PlayerClass, "[W]", GR.Tooltips.Affinity_General);
        CardTooltips.RegisterName(GR.Animator.PlayerClass, "[S]", GR.Tooltips.Affinity_Sealed);

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

//        CardTooltips.RegisterPowerTooltip(GR.Animator.PlayerClass, "[F]", "Force", new ForcePower());
//        CardTooltips.RegisterPowerTooltip(GR.Animator.PlayerClass, "[A]", "Agility", new AgilityPower());
//        CardTooltips.RegisterPowerTooltip(GR.Animator.PlayerClass, "[I]", "Intellect", new IntellectPower());
//        CardTooltips.RegisterPowerTooltip(GR.Animator.PlayerClass, "[B]", "Blessing", new BlessingPower());
//        CardTooltips.RegisterPowerTooltip(GR.Animator.PlayerClass, "[C]", "Corruption", new CorruptionPower());
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
        Config.Load(CardCrawlGame.saveSlot);
        Data.Initialize();
        Config.InitializeOptions();
        AnimatorAscensionManager.Initialize();
        AnimatorLoadoutsContainer.PreloadResources();
        AnimatorImages.PreloadResources();
        isLoaded = true;
    }
}