package eatyourbeets.resources;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
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
import eatyourbeets.resources.unnamed.UnnamedResources;
import eatyourbeets.utilities.JavaUtilities;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GR
{
    // TODO: Set to false
    public static final boolean TEST_MODE = false;

    protected static final Logger logger = JavaUtilities.GetLogger(GR.class);
    protected static final ArrayList<String> cardClassNames = JavaUtilities.GetClassNamesFromJarFile("eatyourbeets.cards.");
    protected static final ArrayList<String> relicClassNames = JavaUtilities.GetClassNamesFromJarFile("eatyourbeets.relics.");
    protected static final ArrayList<String> powerClassNames = JavaUtilities.GetClassNamesFromJarFile("eatyourbeets.powers.");
    protected static final HashMap<String, Texture> textures = new HashMap<>();

    public static CardTooltips Tooltips = new CardTooltips();
    public static UIManager UI = new UIManager();
    public static AnimatorResources Animator;
    public static UnnamedResources Unnamed;
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
        Unnamed = new UnnamedResources();

        Initialize(Common);
        Initialize(Animator);
        //Initialize(Unnamed);
    }

    protected static void Initialize(AbstractResources resources)
    {
        resources.InitializeInternal();
        resources.InitializeColor();

        BaseMod.subscribe(resources);
    }

    public static CharacterStrings GetCharacterStrings(String characterID)
    {
        return CardCrawlGame.languagePack.getCharacterString(characterID);
    }

    public static MonsterStrings GetMonsterStrings(String monsterID)
    {
        return CardCrawlGame.languagePack.getMonsterStrings(monsterID);
    }

    public static PowerStrings GetPowerStrings(String powerID)
    {
        return CardCrawlGame.languagePack.getPowerStrings(powerID);
    }

    public static CardStrings GetCardStrings(String cardID)
    {
        return CardCrawlGame.languagePack.getCardStrings(cardID);
    }

    public static EventStrings GetEventStrings(String eventID)
    {
        return CardCrawlGame.languagePack.getEventString(eventID);
    }

    public static BlightStrings GetBlightStrings(String blightID)
    {
        return CardCrawlGame.languagePack.getBlightString(blightID);
    }

    public static UIStrings GetUIStrings(String stringID)
    {
        return CardCrawlGame.languagePack.getUIString(stringID);
    }

    public static OrbStrings GetOrbStrings(String orbID)
    {
        return CardCrawlGame.languagePack.getOrbString(orbID);
    }

    public static String GetCardImage(String cardID)
    {
        return "images/cards/" + cardID.replace(":", "/") + ".png";
    }

    public static String GetRelicImage(String relicID)
    {
        return "images/relics/" + relicID.replace(":", "/") + ".png";
    }

    public static String GetBlightImageName(String blightID)
    {
        return blightID.replace(":", "/") + ".png";
    }

    public static String GetPowerImage(String powerID)
    {
        return "images/powers/" + powerID.replace(":", "/") + ".png";
    }

    public static String GetMonsterImage(String monsterID)
    {
        return "images/monsters/" + monsterID.replace(":", "/") + ".png";
    }

    public static String GetRewardImage(String rewardID)
    {
        return "images/ui/rewards/" + rewardID.replace(":", "/") + ".png";
    }

    public static Texture GetTextureMipMap(String path)
    {
        Texture texture = textures.get(path);
        if (texture == null)
        {
            texture = new Texture(Gdx.files.internal(path), true);
            texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
            textures.put(path, texture);
        }

        return texture;
    }

    public static Texture GetTexture(String path)
    {
        Texture texture = textures.get(path);
        if (texture == null)
        {
            texture = new Texture(Gdx.files.internal(path), false);
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
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

    protected void LoadKeywords(String path)
    {
        FileHandle file = Gdx.files.internal(path);
        if (!file.exists())
        {
            JavaUtilities.GetLogger(this).warn("File not found: " + path);
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

            Tooltips.RegisterID(id, tooltip);

            for (String name : keyword.NAMES)
            {
                Tooltips.RegisterName(name, tooltip);
            }
        }
    }

    protected void LoadCustomStrings(Class<?> type, String path)
    {
        if (Gdx.files.internal(path).exists())
        {
            BaseMod.loadCustomStringsFile(type, path);
        }
        else
        {
            JavaUtilities.GetLogger(this).warn("File not found: " + path);
        }
    }

    public static boolean CanInstantiate(Class<?> type)
    {
        return (!Hidden.class.isAssignableFrom(type) && !Modifier.isAbstract(type.getModifiers()));
    }

    public static class Enums
    {
        public static class Characters
        {
            @SpireEnum
            public static AbstractPlayer.PlayerClass THE_ANIMATOR;

            @SpireEnum
            public static AbstractPlayer.PlayerClass THE_UNNAMED;
        }

        public static class Cards
        {
            @SpireEnum
            public static AbstractCard.CardColor THE_ANIMATOR;

            @SpireEnum
            public static AbstractCard.CardColor THE_UNNAMED;
        }

        public static class Library
        {
            @SpireEnum
            public static CardLibrary.LibraryType THE_ANIMATOR;

            @SpireEnum
            public static CardLibrary.LibraryType THE_UNNAMED;
        }

        public static class Screens
        {
            @SpireEnum
            public static AbstractDungeon.CurrentScreen EYB_SCREEN;
        }

        public static class Rewards
        {
            @SpireEnum
            public static RewardItem.RewardType SYNERGY_CARDS;

            @SpireEnum
            public static RewardItem.RewardType SPECIAL_GOLD;
        }

        public static class CardTags
        {
            @SpireEnum
            public static AbstractCard.CardTags TEMPORARY;

            //@SpireEnum
            //public static AbstractCard.CardTags SHAPESHIFTER;

            //@SpireEnum
            //public static AbstractCard.CardTags UNOBTAINABLE;

            @SpireEnum
            public static AbstractCard.CardTags PIERCING;

            @SpireEnum
            public static AbstractCard.CardTags UNIQUE;

            @SpireEnum
            public static AbstractCard.CardTags VOIDBOUND;

            @SpireEnum
            public static AbstractCard.CardTags IGNORE_PEN_NIB;

            @SpireEnum
            public static AbstractCard.CardTags ECHO;

            @SpireEnum
            public static AbstractCard.CardTags PURGE;

            @SpireEnum
            public static AbstractCard.CardTags PURGING;

            @SpireEnum
            public static AbstractCard.CardTags LOYAL;

            @SpireEnum
            public static AbstractCard.CardTags IMPROVED_STRIKE;

            @SpireEnum
            public static AbstractCard.CardTags IMPROVED_DEFEND;
        }
    }
}
