package eatyourbeets;

import basemod.BaseMod;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergy;
import eatyourbeets.relics.LivingPicture;
import eatyourbeets.relics.PurgingStone;
import eatyourbeets.relics.TheMissingPiece;
import eatyourbeets.rewards.SpecialGoldReward;
import eatyourbeets.rewards.SynergyCardsReward;
import eatyourbeets.variables.SecondaryValueVariable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import patches.AbstractEnums;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnimatorResources
{
    public enum UIStringType
    {
        CharacterSelect,
        Synergies,
        Rewards,
        Actions,
        SoraEffects,
        CardSelect,
        TetAction
    }

    private static final Logger logger = LogManager.getLogger(AnimatorResources.class.getName());
    private static final HashMap<Integer, Texture> characterPortraits = new HashMap<>();
    private static final String languagePath;

    public static final String ATTACK_PNG = "images/cardui/512/bg_attack_canvas.png";
    public static final String SKILL_PNG = "images/cardui/512/bg_skill_canvas.png";
    public static final String POWER_PNG = "images/cardui/512/bg_power_canvas.png";
    public static final String ORB_A_PNG = "images/cardui/512/card_a_orb_canvas.png";
    public static final String ORB_C_PNG = "images/cardui/512/card_c_orb_canvas.png";
    public static final String ORB_B_PNG = "images/cardui/512/card_b_orb_canvas.png";
    public static final String ATTACK_P_PNG = "images/cardui/1024/bg_attack_canvas.png";
    public static final String SKILL_P_PNG = "images/cardui/1024/bg_skill_canvas.png";
    public static final String POWER_P_PNG = "images/cardui/1024/bg_power_canvas.png";
    public static final String CHAR_BUTTON_PNG = "images/ui/charselect/animator_button.png";
    public static final String CHAR_PORTRAIT_JPG = "images/ui/charselect/animator_portrait.jpg";

    public static final String ORB_VFX_PNG = "images/ui/topPanel/animator_canvas/orbVfx.png";
    public static final String[] ORB_TEXTURES =
            {
                    "images/ui/topPanel/animator_canvas/layer1.png", "images/ui/topPanel/animator_canvas/layer2.png", "images/ui/topPanel/animator_canvas/layer3.png",
                    "images/ui/topPanel/animator_canvas/layer4.png", "images/ui/topPanel/animator_canvas/layer5.png", "images/ui/topPanel/animator_canvas/layer6.png",
                    "images/ui/topPanel/animator_canvas/layer1d.png", "images/ui/topPanel/animator_canvas/layer2d.png", "images/ui/topPanel/animator_canvas/layer3d.png",
                    "images/ui/topPanel/animator_canvas/layer4d.png", "images/ui/topPanel/animator_canvas/layer5d.png"
            };

    public static final String SKELETON_ATLAS = "images/characters/animator/idle/skeleton.atlas";
    public static final String SKELETON_JSON = "images/characters/animator/idle/skeleton.json";
    public static final String SHOULDER1_PNG = "images/characters/animator/shoulder.png";
    public static final String SHOULDER2_PNG = "images/characters/animator/shoulder2.png";
    public static final String CORPSE_PNG = "images/characters/animator/corpse.png";

    public static Texture GetCharacterPortrait(int id)
    {
        Texture result;
        if (!characterPortraits.containsKey(id))
        {
            result = new Texture("images/ui/charselect/animator_portrait_" + id + ".png");
            characterPortraits.put(id, result);
        }
        else
        {
            result = characterPortraits.get(id);
        }

        return result;
    }

    static
    {
        if (Settings.language == Settings.GameLanguage.ZHT)
        {
            languagePath = "localization/zht/";
        }
        else if (Settings.language == Settings.GameLanguage.ZHS)
        {
            languagePath = "localization/zhs/";
        }
        else
        {
            languagePath = "localization/eng/";
        }
    }

    public static void LoadGameStrings()
    {
        BaseMod.loadCustomStringsFile(CharacterStrings.class, languagePath + "Animator_CharacterStrings.json");
        BaseMod.loadCustomStringsFile(CardStrings.class, languagePath + "Animator_CardStrings.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, languagePath + "Animator_RelicStrings.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, languagePath + "Animator_PowerStrings.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, languagePath + "Animator_UIStrings.json");
    }

    public static CharacterStrings GetCharacterStrings()
    {
        return CardCrawlGame.languagePack.getCharacterString("Animator");
    }

    public static CardStrings GetCardStrings(String cardID)
    {
        return CardCrawlGame.languagePack.getCardStrings(cardID);
    }

    public static UIStrings GetUIStrings(UIStringType type) { return CardCrawlGame.languagePack.getUIString("Animator_" + type.name()); }

    public static String GetCardImage(String cardID)
    {
        return "images/cards/" + cardID + ".png";
    }

    public static String GetRelicImage(String relicID)
    {
        return "images/relics/" + relicID + ".png";
    }

    public static String GetPowerImage(String powerID)
    {
        return "images/powers/" + powerID + ".png";
    }

    public static String GetRewardImage(String rewardID)
    {
        return "images/ui/rewards/" + rewardID + ".png";
    }

    public static void LoadCustomKeywords()
    {
        final String json = Gdx.files.internal(languagePath + "Animator_KeywordStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));

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
        BaseMod.addRelicToCustomPool(new LivingPicture(), AbstractEnums.Cards.THE_ANIMATOR);
        BaseMod.addRelicToCustomPool(new TheMissingPiece(), AbstractEnums.Cards.THE_ANIMATOR);
        BaseMod.addRelicToCustomPool(new PurgingStone(), AbstractEnums.Cards.THE_ANIMATOR);
    }

    public static void LoadCustomCards() throws Exception
    {
        BaseMod.addDynamicVariable(new SecondaryValueVariable());

        // Do NOT localize this, it is used to load every card's ID
        String jsonString = Gdx.files.internal("localization/eng/Animator_CardStrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        Map<String, CardStrings> map = new Gson().fromJson(jsonString, new TypeToken<HashMap<String, CardStrings>>()
        {
        }.getType());
        for (String s : map.keySet())
        {
            logger.info("Adding: " + s);

            AddAndUnlock(Class.forName("eatyourbeets.cards.animator." + s.replace("animator_", "")));
        }
    }

    public static void LoadCustomRewards()
    {
        SynergyCardsReward.Serializer synergySerializer = new SynergyCardsReward.Serializer();
        BaseMod.registerCustomReward(AbstractEnums.Rewards.SYNERGY_CARDS, synergySerializer, synergySerializer);

        SpecialGoldReward.Serializer goldSerializer = new SpecialGoldReward.Serializer();
        BaseMod.registerCustomReward(AbstractEnums.Rewards.SPECIAL_GOLD, goldSerializer, goldSerializer);
    }

    private static void AddAndUnlock(Class cardClass) throws Exception
    {
        AbstractCard card = (AbstractCard) cardClass.newInstance();
        String id = card.cardID;

        if (UnlockTracker.isCardLocked(id))
        {
            UnlockTracker.unlockCard(id);
        }

        BaseMod.addCard(card);
    }

    public static boolean CreateDebugFile()
    {
        final String filePath = "c:/temp/synergies.txt";
        File f = new File(filePath);
        if(!f.exists() || f.isDirectory())
        {
            return false;
        }

        ArrayList<AbstractCard> cards = BaseMod.getCustomCardsToAdd();
        ArrayList<AnimatorCard> animatorCards = new ArrayList<>();
        for (AbstractCard c : cards)
        {
            AnimatorCard c2 = Utilities.SafeCast(c, AnimatorCard.class);
            if (c2 != null && c2.GetSynergy() != null)
            {
                animatorCards.add(c2);
            }
        }

        ArrayList<AbstractCard.CardRarity> rarities = new ArrayList<>();
        rarities.add(AbstractCard.CardRarity.COMMON);
        rarities.add(AbstractCard.CardRarity.UNCOMMON);
        rarities.add(AbstractCard.CardRarity.RARE);
        rarities.add(AbstractCard.CardRarity.SPECIAL);

        StringBuilder sb = new StringBuilder();
        Map<Synergy, List<AnimatorCard>> cardGroup = animatorCards.stream().collect(Collectors.groupingBy(AnimatorCard::GetSynergy));
        for (Synergy s : cardGroup.keySet())
        {
            if (s != null)
            {
                sb.append("[").append(s.NAME).append("] (").append(cardGroup.get(s).size()).append(")\n");
                Map<AbstractCard.CardRarity, List<AnimatorCard>> cardRarityGroup = cardGroup.get(s).stream().collect(Collectors.groupingBy(c -> c.rarity));
                for (AbstractCard.CardRarity r : rarities)
                {
                    if (cardRarityGroup.containsKey(r))
                    {
                        sb.append(r.name()).append(":\n");
                        List<AnimatorCard> rarityGroup = cardRarityGroup.get(r);
                        for (AnimatorCard a : rarityGroup)
                        {
                            sb.append(a.name).append("\n");
                        }
                    }
                }
                sb.append("\n");
            }
            sb.append("\n");
        }

        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(sb.toString());
            writer.close();

            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        return false;
    }
}