package eatyourbeets.resources;

import basemod.BaseMod;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.resources.animator.AnimatorResources;
import eatyourbeets.resources.common.EYBResources;
import eatyourbeets.resources.unnamed.UnnamedResources;
import eatyourbeets.utilities.JavaUtilities;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patches.AbstractEnums;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractResources
implements EditCharactersSubscriber, EditCardsSubscriber, EditKeywordsSubscriber,
           EditRelicsSubscriber, EditStringsSubscriber, PostInitializeSubscriber,
           AddAudioSubscriber
{
    protected static final ArrayList<String> cardClassNames = JavaUtilities.GetClassNamesFromJarFile("eatyourbeets.cards.");
    protected static final ArrayList<String> relicClassNames = JavaUtilities.GetClassNamesFromJarFile("eatyourbeets.relics.");
    protected static final ArrayList<String> powerClassNames = JavaUtilities.GetClassNamesFromJarFile("eatyourbeets.powers.");
    protected static final HashMap<String, Map<String, String>> dynamicKeywords = new HashMap<>();
    protected static final HashMap<String, Keyword> keywords = new HashMap<>();
    protected static final HashMap<String, Texture> textures = new HashMap<>();
    protected static final Logger logger = LogManager.getLogger(AnimatorResources.class.getName());

    protected static EYBResources commonResources;
    protected static UnnamedResources unnamedResources;
    protected static AnimatorResources animatorResources;

    public static String CreateID(String prefix, String suffix)
    {
        return prefix + ":" + suffix;
    }

    public static Texture GetTexture(String path)
    {
        Texture texture = textures.get(path);
        if (texture == null)
        {
            texture = new Texture(path);
            texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            textures.put(path, texture);
        }

        return texture;
    }

    public static Map<String, String> GetDynamicKeyword(String keywordID)
    {
        return dynamicKeywords.get(keywordID);
    }

    public static Keyword GetKeyword(String keywordID)
    {
        return keywords.get(keywordID);
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

    public static void Initialize()
    {
        commonResources = Initialize(commonResources, new EYBResources());
        animatorResources = Initialize(animatorResources, new AnimatorResources());
        //unnamedResources = Initialize(unnamedResources, new UnnamedResources());
    }

    private static <T extends AbstractResources> T Initialize(AbstractResources existing, T resources)
    {
        if (existing != null)
        {
            BaseMod.unsubscribe(existing);
        }

        resources.InitializeInternal();
        resources.InitializeColor();

        BaseMod.subscribe(resources);

        return resources;
    }

    @Override
    public final void receiveEditCards()
    {
        InitializeCards();
    }

    @Override
    public final void receiveEditCharacters()
    {
        InitializeCharacter();
    }

    @Override
    public final void receiveEditKeywords()
    {
        InitializeKeywords();
    }

    @Override
    public final void receiveEditRelics()
    {
        InitializeRelics();
    }

    @Override
    public final void receiveEditStrings()
    {
        InitializeStrings();
    }

    @Override
    public void receiveAddAudio()
    {
        InitializeAudio();
    }

    @Override
    public final void receivePostInitialize()
    {
        InitializeEvents();
        InitializeMonsters();
        InitializePotions();
        InitializeRewards();
        InitializePowers();
        PostInitialize();
    }

    protected static void LoadCustomRelics(String character)
    {
        final String prefix = "eatyourbeets.relics." + character;

        for (String s : relicClassNames)
        {
            if (s.startsWith(prefix))
            {
                try
                {
                    logger.info("Adding: " + s);

                    LoadRelic(Class.forName(s));
                }
                catch (ClassNotFoundException e)
                {
                    logger.warn("Class not found : " + s);
                }
            }
        }
    }

    protected static void LoadCustomCards(String character)
    {
        final String prefix = "eatyourbeets.cards." + character;

        for (String s : cardClassNames)
        {
            if (s.startsWith(prefix))
            {
                try
                {
                    logger.info("Adding: " + s);

                    LoadCard(Class.forName(s));
                }
                catch (ClassNotFoundException e)
                {
                    logger.warn("Class not found : " + s);
                }
            }
        }
    }

    protected static void LoadCard(Class<?> type)
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

    protected static void LoadRelic(Class<?> type)
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

        BaseMod.addRelicToCustomPool(relic, AbstractEnums.Cards.THE_ANIMATOR);
    }

    @SuppressWarnings("unchecked") // I miss C# ...
    protected static void LoadCustomPowers(String character)
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

    protected static boolean CanInstantiate(Class<?> type)
    {
        return (!Hidden.class.isAssignableFrom(type) && !Modifier.isAbstract(type.getModifiers()));
    }

    protected void LoadKeywords(String path)
    {
        String json = Gdx.files.internal(path).readString(String.valueOf(StandardCharsets.UTF_8));

        Gson gson = new Gson();
        Type typeToken = new TypeToken<Map<String, Keyword>>()
        {
        }.getType();
        Map<String, Keyword> keywords = gson.fromJson(json, typeToken);

        for (Map.Entry<String, Keyword> pair : keywords.entrySet())
        {
            String id = pair.getKey();
            Keyword keyword = pair.getValue();

            if (!id.startsWith("~"))
            {
                BaseMod.addKeyword(keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }

            AbstractResources.keywords.put(id, keyword);
        }
    }

    protected void LoadDynamicKeywords(String path)
    {
        String json = Gdx.files.internal(path).readString(String.valueOf(StandardCharsets.UTF_8));

        Gson gson = new Gson();
        Type typeToken = new TypeToken<Map<String, Map<String, String>>>()
        {
        }.getType();
        Map<String, Map<String, String>> keywords = gson.fromJson(json, typeToken);

        for (Map.Entry<String, Map<String, String>> pair : keywords.entrySet())
        {
            String id = pair.getKey();
            Map<String, String> keyword = pair.getValue();

            AbstractResources.dynamicKeywords.put(id, keyword);
        }
    }

    protected void InitializeInternal()
    {

    }

    protected void InitializeColor()
    {
    }

    protected void InitializeMonsters()
    {
    }

    protected void InitializeEvents()
    {
    }

    protected void InitializeRewards()
    {
    }

    protected void InitializeAudio()
    {
    }

    protected void InitializeCards()
    {
    }

    protected void InitializePowers()
    {
    }

    protected void InitializeStrings()
    {
    }

    protected void InitializeRelics()
    {
    }

    protected void InitializePotions()
    {
    }

    protected void InitializeCharacter()
    {
    }

    protected void InitializeKeywords()
    {
    }

    protected void PostInitialize()
    {
    }
}
