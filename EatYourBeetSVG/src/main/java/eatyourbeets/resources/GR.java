package eatyourbeets.resources;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
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
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.resources.animator.AnimatorResources;
import eatyourbeets.resources.common.CommonResources;
import eatyourbeets.utilities.JUtils;
import org.apache.logging.log4j.Logger;

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
    // TODO: Set to false
    public static final boolean TEST_MODE = false;

    protected static final Logger logger = JUtils.GetLogger(GR.class);
    protected static final ArrayList<String> cardClassNames = JUtils.GetClassNamesFromJarFile("eatyourbeets.cards.");
    protected static final ArrayList<String> relicClassNames = JUtils.GetClassNamesFromJarFile("eatyourbeets.relics.");
    protected static final ArrayList<String> powerClassNames = JUtils.GetClassNamesFromJarFile("eatyourbeets.powers.");
    protected static final HashMap<String, Texture> textures = new HashMap<>();

    public static CardTooltips Tooltips = null; // Created by CommonResources
    public static UIManager UI = new UIManager();
    public static AnimatorResources Animator;
    public static CommonResources Common;
    public static boolean IsLoaded;

    public static void Initialize()
    {
        if (Common != null)
        {
            throw new RuntimeException("Already Initialized");
        }

        Common = new CommonResources();
        Animator = new AnimatorResources();

        Initialize(Common);
        Initialize(Animator);
    }

    protected static void Initialize(AbstractResources resources)
    {
        resources.InitializeInternal();
        resources.InitializeColor();

        BaseMod.subscribe(resources);
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
                JUtils.GetLogger(GR.class).error("Texture does not exist: " + path);
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
        final String prefix = "eatyourbeets.relics." + character;

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

        BaseMod.addRelicToCustomPool(relic, Enums.Cards.THE_ANIMATOR);
    }

    protected void LoadCustomCards(String character)
    {
        final String prefix = "eatyourbeets.cards." + character;

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
        final String prefix = "eatyourbeets.cards." + character;

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
            JUtils.LogWarning(this, "File not found: " + file.path());
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
            EYBCardTooltip tooltip = new EYBCardTooltip(keyword);

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
            JUtils.LogWarning(this, "File not found: " + file.path());
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
            JUtils.GetLogger(AnimatorResources.class).error("Loading card strings failed. Using default method.");
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
            @SpireEnum public static AbstractPlayer.PlayerClass THE_ANIMATOR;
            @SpireEnum public static AbstractPlayer.PlayerClass THE_UNNAMED;
        }

        public static class Cards
        {
            @SpireEnum public static AbstractCard.CardColor THE_ANIMATOR;
            @SpireEnum public static AbstractCard.CardColor THE_UNNAMED;
        }

        public static class Library
        {
            @SpireEnum public static CardLibrary.LibraryType THE_ANIMATOR;
            @SpireEnum public static CardLibrary.LibraryType THE_UNNAMED;
        }

        public static class Screens
        {
            @SpireEnum public static AbstractDungeon.CurrentScreen EYB_SCREEN;
        }

        public static class Rewards
        {
            @SpireEnum public static RewardItem.RewardType SYNERGY_CARDS;
            @SpireEnum public static RewardItem.RewardType AURA_CARDS;
            @SpireEnum public static RewardItem.RewardType SPECIAL_GOLD;
        }

        public static class CardTags
        {
            @SpireEnum public static AbstractCard.CardTags TEMPORARY;
            @SpireEnum public static AbstractCard.CardTags UNIQUE;
            @SpireEnum public static AbstractCard.CardTags VOIDBOUND;
            @SpireEnum public static AbstractCard.CardTags IGNORE_PEN_NIB;
            @SpireEnum public static AbstractCard.CardTags ECHO;
            @SpireEnum public static AbstractCard.CardTags PURGE;
            @SpireEnum public static AbstractCard.CardTags DELAYED;
            @SpireEnum public static AbstractCard.CardTags HASTE;
            @SpireEnum public static AbstractCard.CardTags HASTE_INFINITE;
            @SpireEnum public static AbstractCard.CardTags PURGING;
            @SpireEnum public static AbstractCard.CardTags LOYAL;
            @SpireEnum public static AbstractCard.CardTags AUTOPLAY;
            @SpireEnum public static AbstractCard.CardTags IMPROVED_BASIC_CARD;
            @SpireEnum public static AbstractCard.CardTags HARMONIC;
        }

        public static class AttackEffect
        {
            @SpireEnum public static AbstractGameAction.AttackEffect FIRE_EXPLOSION;
            @SpireEnum public static AbstractGameAction.AttackEffect ICE;
            @SpireEnum public static AbstractGameAction.AttackEffect DARKNESS;
            @SpireEnum public static AbstractGameAction.AttackEffect PSYCHOKINESIS;
            @SpireEnum public static AbstractGameAction.AttackEffect GUNSHOT;
            @SpireEnum public static AbstractGameAction.AttackEffect SHIELD_FROST;
            @SpireEnum public static AbstractGameAction.AttackEffect DAGGER;
            @SpireEnum public static AbstractGameAction.AttackEffect SPEAR;
            @SpireEnum public static AbstractGameAction.AttackEffect PUNCH;
            @SpireEnum public static AbstractGameAction.AttackEffect DARK;
            @SpireEnum public static AbstractGameAction.AttackEffect WATER;
        }
    }
}
