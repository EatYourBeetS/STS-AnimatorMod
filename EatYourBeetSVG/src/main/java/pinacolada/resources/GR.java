package pinacolada.resources;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.interfaces.markers.Hidden;
import org.apache.logging.log4j.Logger;
import pinacolada.cards.base.PCLCardTooltip;
import pinacolada.resources.pcl.PCLResources;
import pinacolada.utilities.PCLJUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class GR
{
    // TODO: Merge with EYB GR
    // TODO: Set to false
    public static final boolean TEST_MODE = false;
    public static final String BASE_PREFIX = "pcl";
    public static final String PREFIX_CARDS = "pinacolada.cards.";
    public static final String PREFIX_POWERS = "pinacolada.powers.";
    public static final String PREFIX_RELIC = "pinacolada.relics.";

    protected static final Logger logger = PCLJUtils.GetLogger(GR.class);
    protected static final ArrayList<String> cardClassNames = PCLJUtils.GetClassNamesFromJarFile(PREFIX_CARDS);
    protected static final ArrayList<String> powerClassNames = PCLJUtils.GetClassNamesFromJarFile(PREFIX_POWERS);
    protected static final ArrayList<String> relicClassNames = PCLJUtils.GetClassNamesFromJarFile(PREFIX_RELIC);
    protected static final HashMap<String, Texture> textures = new HashMap<>();

    public static CardTooltips Tooltips = null;
    public static UIManager UI = new UIManager();
    public static PCLResources PCL;
    public static ShaderProgram GrayscaleShader;
    public static ShaderProgram SepiaShader;
    public static boolean IsLoaded;

    public static void Initialize()
    {
        if (PCL != null)
        {
            throw new RuntimeException("Already Initialized");
        }

        PCL = new PCLResources();

        Initialize(PCL);
    }

    protected static void Initialize(AbstractResources resources)
    {
        resources.InitializeInternal();
        resources.InitializeColor();

        BaseMod.subscribe(resources);
    }

    public static ShaderProgram GetGrayscaleShader() {
        if (GrayscaleShader == null) {
            FileHandle fShader = Gdx.files.internal("shaders/grayscaleFragment.glsl");
            FileHandle vShader = Gdx.files.internal("shaders/coloringVertex.glsl");
            String fShaderString = fShader.readString();
            String vShaderString = vShader.readString();
            GrayscaleShader = new ShaderProgram(vShaderString, fShaderString);
        }
        return GrayscaleShader;
    }

    public static ShaderProgram GetSepiaShader() {
        if (SepiaShader == null) {
            FileHandle fShader = Gdx.files.internal("shaders/sepiaFragment.glsl");
            FileHandle vShader = Gdx.files.internal("shaders/coloringVertex.glsl");
            String fShaderString = fShader.readString();
            String vShaderString = vShader.readString();
            SepiaShader = new ShaderProgram(vShaderString, fShaderString);
        }
        return SepiaShader;
    }

    public static CharacterStrings GetCharacterStrings(String characterID)
    {
        return GetLanguagePack().getCharacterString(characterID);
    }

    public static MonsterStrings GetMonsterStrings(String monsterID)
    {
        return GetLanguagePack().getMonsterStrings(monsterID);
    }

    public static PowerStrings GetPowerStrings(String powerID)
    {
        return GetLanguagePack().getPowerStrings(powerID);
    }

    public static RelicStrings GetRelicStrings(String relicID)
    {
        return GetLanguagePack().getRelicStrings(relicID);
    }

    public static CardStrings GetCardStrings(String cardID)
    {
        return GetLanguagePack().getCardStrings(cardID);
    }

    public static StanceStrings GetStanceString(String stanceID)
    {
        return GetLanguagePack().getStanceString(stanceID);
    }

    public static EventStrings GetEventStrings(String eventID)
    {
        return GetLanguagePack().getEventString(eventID);
    }

    public static BlightStrings GetBlightStrings(String blightID)
    {
        return GetLanguagePack().getBlightString(blightID);
    }

    public static UIStrings GetUIStrings(String stringID)
    {
        return GetLanguagePack().getUIString(stringID);
    }

    public static RunModStrings GetRunModStrings(String stringID)
    {
        return GetLanguagePack().getRunModString(stringID);
    }

    public static OrbStrings GetOrbStrings(String orbID)
    {
        return GetLanguagePack().getOrbString(orbID);
    }

    private static LocalizedStrings GetLanguagePack()
    {
        return CardCrawlGame.languagePack;
    }

    public static String GetPng(String id, String subFolder)
    {
        final String[] s = id.split(Pattern.quote(":"), 2);
        return "images/" + s[0] + "/" + subFolder + "/" + s[1].replace(":", "_") + ".png";
    }

    public static String GetCardImage(String id)
    {
        return GetPng(id, "cards");
    }

    public static String GetRelicImage(String id)
    {
        return GetPng(id, "relics");
    }

    public static String GetBlightImage(String id)
    {
        return GetPng(id, "blights");
    }

    public static String GetBlightOutlineImage(String id)
    {
        return GetPng(id, "blights/outline");
    }

    public static String GetPowerImage(String id)
    {
        return GetPng(id, "powers");
    }

    public static String GetMonsterImage(String id)
    {
        return GetPng(id, "monsters");
    }

    public static String GetRewardImage(String id)
    {
        return GetPng(id, "ui/rewards");
    }

    public static boolean IsTranslationSupported(Settings.GameLanguage language)
    {
        //This should be set only for beta branches.  Do not merge this into master.
        return false;//language == Settings.GameLanguage.RUS; // language == Settings.GameLanguage.ZHS || language == Settings.GameLanguage.ZHT;
    }

    public static Texture GetTexture(String path)
    {
        return GetTexture(path, false);
    }

    public static Texture GetTexture(String path, boolean useMipMap)
    {
        return GetTexture(path, true, false);
    }

    public static Texture GetTexture(String path, boolean useMipMap, boolean refresh)
    {
        Texture texture = textures.get(path);
        if (texture == null || refresh)
        {
            FileHandle file = Gdx.files.internal(path);
            if (file.exists())
            {
                texture = new Texture(file, useMipMap);
                if (useMipMap)
                {
                    texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
                }
                else
                {
                    texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                }
            }
            else
            {
                PCLJUtils.GetLogger(GR.class).error("Texture does not exist: " + path);
            }

            textures.put(path, texture);
        }

        return texture;
    }

    public static String CreateID(String prefix, String suffix)
    {
        return prefix + ":" + suffix;
    }

    protected void LoadCustomRelics(String character)
    {
        final String prefix = PREFIX_RELIC + character;

        for (String s : relicClassNames)
        {
            if (s.startsWith(prefix))
            {
                try
                {
                    logger.info("Adding: " + s);

                    LoadCustomRelic(Class.forName(s));
                }
                catch (ClassNotFoundException e)
                {
                    logger.warn("Class not found : " + s);
                }
            }
        }
    }

    protected void LoadCustomRelic(Class<?> type)
    {
        if (!CanInstantiate(type))
        {
            return;
        }

        AbstractRelic relic;
        try
        {
            relic = (AbstractRelic)type.getConstructor().newInstance();
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
        {
            e.printStackTrace();
            return;
        }

        BaseMod.addRelicToCustomPool(relic, Enums.Cards.THE_FOOL);
    }

    protected void LoadCustomCards(String character)
    {
        final String prefix = PREFIX_CARDS + character;
        for (String s : cardClassNames)
        {
            if (s.startsWith(prefix))
            {
                try
                {
                    logger.info("Adding: " + s);

                    LoadCustomCard(Class.forName(s));
                }
                catch (ClassNotFoundException e)
                {
                    logger.warn("Class not found : " + s);
                }
            }
        }
    }

    protected void LoadCustomCard(Class<?> type)
    {
        if (!CanInstantiate(type))
        {
            return;
        }

        AbstractCard card;
        String id;

        try
        {
            card = (AbstractCard)type.getConstructor().newInstance();
            id = card.cardID;
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
        {
            e.printStackTrace();
            return;
        }

        if (UnlockTracker.isCardLocked(id))
        {
            UnlockTracker.unlockCard(id);
            card.isLocked = false;
        }

        BaseMod.addCard(card);
    }

    protected void LoadCustomPowers(String character)
    {
        final String prefix = PREFIX_CARDS + character;

        for (String s : cardClassNames)
        {
            if (s.startsWith(prefix))
            {
                try
                {
                    logger.info("Adding: " + s);

                    Class<?> type = Class.forName(s);
                    if (CanInstantiate(type))
                    {
                        BaseMod.addPower((Class<AbstractPower>) type, CreateID(character, type.getSimpleName()));
                    }
                }
                catch (ClassNotFoundException e)
                {
                    logger.warn("Class not found : " + s);
                }
            }
        }
    }

    protected void LoadKeywords(FileHandle file)
    {
        if (!file.exists())
        {
            PCLJUtils.LogWarning(this, "File not found: " + file.path());
            return;
        }

        String json = file.readString(String.valueOf(StandardCharsets.UTF_8));
        Gson gson = new Gson();
        Type typeToken = new TypeToken<Map<String, Keyword>>(){}.getType();
        Map<String, Keyword> items = gson.fromJson(json, typeToken);

        for (Map.Entry<String, Keyword> pair : items.entrySet())
        {
            String id = pair.getKey();
            Keyword keyword = pair.getValue();
            PCLCardTooltip tooltip = new PCLCardTooltip(keyword);

            CardTooltips.RegisterID(id, tooltip);

            for (String name : keyword.NAMES)
            {
                CardTooltips.RegisterName(name, tooltip);
            }
        }
    }

    protected void LoadCustomStrings(Class<?> type, FileHandle file)
    {
        if (file.exists())
        {
            BaseMod.loadCustomStrings(type, file.readString(String.valueOf(StandardCharsets.UTF_8)));
        }
        else
        {
            PCLJUtils.LogWarning(this, "File not found: " + file.path());
        }
    }

    @SuppressWarnings("unchecked")
    public static void LoadGroupedCardStrings(String jsonString)
    {
        Map localizationStrings = ReflectionHacks.getPrivateStatic(LocalizedStrings.class, "cards");
        Map cardStrings = new HashMap<>();
        try
        {
            Type typeToken = new TypeToken<Map<String, Map<String, CardStrings>>>(){}.getType();
            Map map = new HashMap<>((Map)new Gson().fromJson(jsonString, typeToken));

            for (Object key1 : map.keySet())
            {
                Map map3 = ((Map<Object, CardStrings>)map.get(key1));
                for (Object key2 : map3.keySet())
                {
                    cardStrings.put(key2, map3.get(key2));
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            PCLJUtils.GetLogger(PCLResources.class).error("Loading card strings failed. Using default method.");
            BaseMod.loadCustomStrings(CardStrings.class, jsonString);
            return;
        }

        localizationStrings.putAll(cardStrings);
    }

    public static boolean CanInstantiate(Class<?> type)
    {
        return (!Hidden.class.isAssignableFrom(type) && !Modifier.isAbstract(type.getModifiers()));
    }

    public static class Enums
    {
        public static class Characters
        {
            @SpireEnum public static AbstractPlayer.PlayerClass THE_FOOL;
        }

        public static class Cards
        {
            @SpireEnum public static AbstractCard.CardColor THE_FOOL;
        }

        public static class Library
        {
            @SpireEnum public static CardLibrary.LibraryType THE_FOOL;
        }

        public static class Screens
        {
            public static AbstractDungeon.CurrentScreen EYB_SCREEN = eatyourbeets.resources.GR.Enums.Screens.EYB_SCREEN;
        }

        public static class Rewards
        {
            @SpireEnum public static RewardItem.RewardType SERIES_CARDS;
            @SpireEnum public static RewardItem.RewardType ENCHANTMENT;
            public static RewardItem.RewardType SPECIAL_GOLD = eatyourbeets.resources.GR.Enums.Rewards.SPECIAL_GOLD;
        }

        public static class CardTags
        {
            @SpireEnum public static AbstractCard.CardTags AFTERLIFE;
            @SpireEnum public static AbstractCard.CardTags AUTOPLAY;
            @SpireEnum public static AbstractCard.CardTags EXPANDED;
            @SpireEnum public static AbstractCard.CardTags HARMONIC;
            @SpireEnum public static AbstractCard.CardTags HASTE_INFINITE;
            @SpireEnum public static AbstractCard.CardTags PCL_ETHEREAL;
            @SpireEnum public static AbstractCard.CardTags PCL_EXHAUST;
            @SpireEnum public static AbstractCard.CardTags PCL_INNATE;
            @SpireEnum public static AbstractCard.CardTags PCL_RETAIN;
            @SpireEnum public static AbstractCard.CardTags PCL_RETAIN_ONCE;
            @SpireEnum public static AbstractCard.CardTags PCL_UNPLAYABLE;
            @SpireEnum public static AbstractCard.CardTags PROTAGONIST;
            public static AbstractCard.CardTags DELAYED = eatyourbeets.resources.GR.Enums.CardTags.DELAYED;
            public static AbstractCard.CardTags HASTE = eatyourbeets.resources.GR.Enums.CardTags.HASTE;
            public static AbstractCard.CardTags IGNORE_PEN_NIB = eatyourbeets.resources.GR.Enums.CardTags.IGNORE_PEN_NIB;
            public static AbstractCard.CardTags IMPROVED_BASIC_CARD = eatyourbeets.resources.GR.Enums.CardTags.IMPROVED_BASIC_CARD;
            public static AbstractCard.CardTags LOYAL = eatyourbeets.resources.GR.Enums.CardTags.LOYAL;
            public static AbstractCard.CardTags MARKED = eatyourbeets.resources.GR.Enums.CardTags.MARKED;
            public static AbstractCard.CardTags PURGE = eatyourbeets.resources.GR.Enums.CardTags.PURGE;
            public static AbstractCard.CardTags PURGING = eatyourbeets.resources.GR.Enums.CardTags.PURGING;
            public static AbstractCard.CardTags UNIQUE = eatyourbeets.resources.GR.Enums.CardTags.UNIQUE;
            public static AbstractCard.CardTags VOIDBOUND = eatyourbeets.resources.GR.Enums.CardTags.VOIDBOUND;
            public static AbstractCard.CardTags VOLATILE = eatyourbeets.resources.GR.Enums.CardTags.VOLATILE;
        }

        public static class AttackEffect
        {
            @SpireEnum public static AbstractGameAction.AttackEffect BLEED;
            @SpireEnum public static AbstractGameAction.AttackEffect CLASH;
            @SpireEnum public static AbstractGameAction.AttackEffect DARKNESS;
            @SpireEnum public static AbstractGameAction.AttackEffect FIRE_EXPLOSION;
            @SpireEnum public static AbstractGameAction.AttackEffect PSYCHOKINESIS;
            @SpireEnum public static AbstractGameAction.AttackEffect SMALL_EXPLOSION;
            @SpireEnum public static AbstractGameAction.AttackEffect SPARK;
            @SpireEnum public static AbstractGameAction.AttackEffect WATER;
            public static AbstractGameAction.AttackEffect BITE = eatyourbeets.resources.GR.Enums.AttackEffect.BITE;
            public static AbstractGameAction.AttackEffect CLAW = eatyourbeets.resources.GR.Enums.AttackEffect.CLAW;
            public static AbstractGameAction.AttackEffect DAGGER = eatyourbeets.resources.GR.Enums.AttackEffect.DAGGER;
            public static AbstractGameAction.AttackEffect DARK = eatyourbeets.resources.GR.Enums.AttackEffect.DARK;
            public static AbstractGameAction.AttackEffect GUNSHOT = eatyourbeets.resources.GR.Enums.AttackEffect.GUNSHOT;
            public static AbstractGameAction.AttackEffect ICE = eatyourbeets.resources.GR.Enums.AttackEffect.ICE;
            public static AbstractGameAction.AttackEffect PUNCH = eatyourbeets.resources.GR.Enums.AttackEffect.PUNCH;
            public static AbstractGameAction.AttackEffect SHIELD_FROST = eatyourbeets.resources.GR.Enums.AttackEffect.SHIELD_FROST;
            public static AbstractGameAction.AttackEffect SPEAR = eatyourbeets.resources.GR.Enums.AttackEffect.SPEAR;
        }
    }
}
