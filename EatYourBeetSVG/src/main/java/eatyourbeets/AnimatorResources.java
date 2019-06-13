package eatyourbeets;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.interfaces.Hidden;
import eatyourbeets.monsters.Bosses.KrulTepes;
import eatyourbeets.monsters.UnnamedReign.UnnamedEnemyGroup;
import eatyourbeets.potions.FalseLifePotion;
import eatyourbeets.rewards.SpecialGoldReward;
import eatyourbeets.rewards.SynergyCardsReward;
import eatyourbeets.variables.SecondaryValueVariable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patches.AbstractEnums;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AnimatorResources
{
    public enum UIStringType
    {
        CharacterSelect,
        Synergies,
        Rewards,
        Actions,
        SpecialEffects,
        CardSelect,
        Trophies
    }

    private static final Logger logger = LogManager.getLogger(AnimatorResources.class.getName());
    private static final String languagePath;

    public static void LoadGameStrings()
    {
        BaseMod.loadCustomStringsFile(OrbStrings.class, languagePath + "OrbStrings.json");
        BaseMod.loadCustomStringsFile(CharacterStrings.class, languagePath + "CharacterStrings.json");
        BaseMod.loadCustomStringsFile(CardStrings.class, languagePath + "CardStrings.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, languagePath + "RelicStrings.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, languagePath + "PowerStrings.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, languagePath + "UIStrings.json");
        BaseMod.loadCustomStringsFile(EventStrings.class, languagePath + "EventStrings.json");
        BaseMod.loadCustomStringsFile(PotionStrings.class, languagePath + "PotionStrings.json");
        BaseMod.loadCustomStringsFile(MonsterStrings.class, languagePath + "MonsterStrings.json");
    }

    public static CharacterStrings GetCharacterStrings()
    {
        return CardCrawlGame.languagePack.getCharacterString("Animator");
    }

    public static MonsterStrings GetMonsterStrings(String monsterID)
    {
        return CardCrawlGame.languagePack.getMonsterStrings(monsterID);
    }

    public static CardStrings GetCardStrings(String cardID)
    {
        return CardCrawlGame.languagePack.getCardStrings(cardID);
    }

    public static EventStrings GetEventStrings(String eventID)
    {
        return CardCrawlGame.languagePack.getEventString(eventID);
    }

    public static OrbStrings GetOrbStrings(String orbID)
    {
        return CardCrawlGame.languagePack.getOrbString(orbID);
    }

    public static UIStrings GetUIStrings(UIStringType type) { return CardCrawlGame.languagePack.getUIString("animator:" + type.name()); }

    public static String GetCardImage(String cardID)
    {
        return "images/cards/" + cardID.replace(":", "/") + ".png";
    }

    public static String GetRelicImage(String relicID)
    {
        return "images/relics/" + relicID.replace(":", "/") + ".png";
    }

    public static String GetPowerImage(String powerID)
    {
        return "images/powers/" + powerID.replace(":", "/") + ".png";
    }

    public static String GetRewardImage(String rewardID)
    {
        return "images/ui/rewards/" + rewardID + ".png";
    }

    public static String GetMonsterImage(String monsterID)
    {
        return "images/monsters/" + monsterID + ".png";
    }

    public static String GetMonsterImage(String monsterID, String secondaryID)
    {
        return "images/monsters/" + monsterID + "/" + secondaryID + ".png";
    }

    public static void LoadCustomKeywords()
    {
        final String json = Gdx.files.internal(languagePath + "KeywordStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));

        final com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = new Gson().fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);
        if (keywords != null)
        {
            for (final com.evacipated.cardcrawl.mod.stslib.Keyword keyword : keywords)
            {
                BaseMod.addKeyword(keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    public static void LoadCustomRelics()
    {
        AnimatorResources_Relics.AddRelics();

        BaseMod.addPotion(FalseLifePotion.class, Color.GOLDENROD.cpy(), Color.WHITE.cpy(), Color.GOLDENROD.cpy(), FalseLifePotion.POTION_ID, AbstractEnums.Characters.THE_ANIMATOR);
    }

    public static void LoadAudio()
    {
        AnimatorResources_Audio.LoadAudio();
    }

    public static void LoadMonsters()
    {
        BaseMod.addMonster(KrulTepes.ID, KrulTepes::new);
        UnnamedEnemyGroup.RegisterMonsterGroups();
    }

    public static void LoadCustomEvents()
    {
        AnimatorResources_Events.AddEvents();
    }

    public static void LoadCustomCards() throws Exception
    {
        BaseMod.addDynamicVariable(new SecondaryValueVariable());

        // Do NOT localize this, it is used to load every card's ID
        String jsonString = Gdx.files.internal("localization/animator/eng/CardStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        Map<String, CardStrings> map = new Gson().fromJson(jsonString, new TypeToken<HashMap<String, CardStrings>>()
        {
        }.getType());

        for (String s : map.keySet())
        {
            Class cardClass;
            try
            {
                logger.info("Adding: " + s);
                AddAndUnlock(Class.forName("eatyourbeets.cards.animator." + s.replace("animator:", "")));
            }
            catch( ClassNotFoundException e )
            {
                logger.warn("Class not found : " + s);
            }
        }
    }

    public static void LoadCustomRewards()
    {
        SynergyCardsReward.Serializer synergySerializer = new SynergyCardsReward.Serializer();
        BaseMod.registerCustomReward(AbstractEnums.Rewards.SYNERGY_CARDS, synergySerializer, synergySerializer);

        SpecialGoldReward.Serializer goldSerializer = new SpecialGoldReward.Serializer();
        BaseMod.registerCustomReward(AbstractEnums.Rewards.SPECIAL_GOLD, goldSerializer, goldSerializer);
    }

    private static void AddAndUnlock(Class<?> cardClass) throws Exception
    {
        if (Hidden.class.isAssignableFrom(cardClass))
        {
            return;
        }

        AbstractCard card = (AbstractCard) cardClass.getConstructor().newInstance();
        String id = card.cardID;

        if (UnlockTracker.isCardLocked(id))
        {
            UnlockTracker.unlockCard(id);
            card.isLocked = false;
        }

        // animator_ -> animator:
        String oldID = id.replace(":", "_");
        if (UnlockTracker.seenPref.data.containsKey(oldID))
        {
            int res = UnlockTracker.seenPref.getInteger(oldID);

            UnlockTracker.seenPref.data.remove(oldID);

            if (res > 0)
            {
                UnlockTracker.seenPref.putInteger(id, 1);
                UnlockTracker.seenPref.flush();
            }
        }

        BaseMod.addCard(card);
    }

    static
    {
        final String filePath = "c:/temp/animator-localization/";
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
        else if (Settings.language == Settings.GameLanguage.FRA)
        {
            languagePath = "localization/animator/fra/";
        }
        else
        {
            languagePath = "localization/animator/eng/";
        }
    }
}