package eatyourbeets.resources;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.localization.UIStrings;
import eatyourbeets.characters.AnimatorMetrics;
import eatyourbeets.dungeons.TheUnnamedReign;
import eatyourbeets.events.TheMaskedTraveler1;
import eatyourbeets.events.UnnamedReign.TheAbandonedCabin;
import eatyourbeets.events.UnnamedReign.TheHaunt;
import eatyourbeets.events.UnnamedReign.TheMaskedTraveler3;
import eatyourbeets.events.UnnamedReign.TheUnnamedMerchant;
import eatyourbeets.monsters.Bosses.KrulTepes;
import eatyourbeets.monsters.UnnamedReign.UnnamedEnemyGroup;
import eatyourbeets.potions.FalseLifePotion;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.common.GenericFadingPower;
import eatyourbeets.rewards.SpecialGoldReward;
import eatyourbeets.rewards.SynergyCardsReward;
import eatyourbeets.variables.SecondaryValueVariable;
import patches.AbstractEnums;

import java.io.File;

public class Resources_Common extends AbstractResources
{
    private static String languagePath = null;

    private static void LoadLanguagePath()
    {
        if (languagePath != null)
        {
            return;
        }

        String filePath = "c:/temp/common-localization/";
        File f = new File(filePath);
        if(f.exists() && f.isDirectory())
        {
            languagePath = filePath;
        }
        else
        {
            languagePath = "localization/common/eng/";
        }
    }

    public static Texture Map_Act5Entrance;
    public static Texture Map_Act5EntranceOutline;

    public static final String Audio_TheHaunt = "ANIMATOR_THE_HAUNT.ogg";
    public static final String Audio_TheUnnamed = "ANIMATOR_THE_UNNAMED.ogg";
    public static final String Audio_TheCreature = "ANIMATOR_THE_CREATURE.ogg";
    public static final String Audio_TheUltimateCrystal = "ANIMATOR_THE_ULTIMATE_CRYSTAL";

    @Override
    protected void InitializeEvents()
    {
        BaseMod.addEvent(TheMaskedTraveler1.ID, TheMaskedTraveler1.class, Exordium.ID);
        //BaseMod.addEvent(TheMaskedTraveler2.ID, TheMaskedTraveler2.class, TheEnding.ID);
        BaseMod.addEvent(TheMaskedTraveler3.ID, TheMaskedTraveler3.class, TheUnnamedReign.ID);
        //BaseMod.addEvent(TheDomVedeloper1.ID, TheDomVedeloper1.class, Exordium.ID);
        BaseMod.addEvent(TheHaunt.ID, TheHaunt.class, TheUnnamedReign.ID);
        BaseMod.addEvent(TheUnnamedMerchant.ID, TheUnnamedMerchant.class, TheUnnamedReign.ID);
        BaseMod.addEvent(TheAbandonedCabin.ID, TheAbandonedCabin.class, TheUnnamedReign.ID);
    }

    @Override
    protected void InitializeAudio()
    {
        BaseMod.addAudio("ANIMATOR_ORB_EARTH_EVOKE", "audio/sound/ANIMATOR_ORB_EARTH_EVOKE.ogg");
        BaseMod.addAudio("ANIMATOR_ORB_EARTH_CHANNEL", "audio/sound/ANIMATOR_ORB_EARTH_CHANNEL.ogg");
        BaseMod.addAudio("ANIMATOR_KIRA_POWER", "audio/sound/ANIMATOR_KIRA_POWER.ogg");
        BaseMod.addAudio("ANIMATOR_MEGUMIN_CHARGE", "audio/sound/ANIMATOR_MEGUMIN_CHARGE.ogg");
        //BaseMod.addAudio("ANIMATOR_EMONZAEMON_ATTACK", "audio/sound/ANIMATOR_EMONZAEMON_ATTACK.ogg");
        BaseMod.addAudio(Audio_TheUltimateCrystal, "audio/sound/ANIMATOR_THE_ULTIMATE_CRYSTAL.ogg");

        BaseMod.addAudio(Audio_TheHaunt, "audio/music/ANIMATOR_THE_HAUNT.ogg");
        BaseMod.addAudio(Audio_TheUnnamed, "audio/music/ANIMATOR_THE_UNNAMED.ogg");
        BaseMod.addAudio(Audio_TheCreature, "audio/music/ANIMATOR_THE_CREATURE.ogg");
    }

    @Override
    protected void InitializeMonsters()
    {
        BaseMod.addMonster(KrulTepes.ID, KrulTepes::new);
        UnnamedEnemyGroup.RegisterMonsterGroups();
    }

    @Override
    protected void InitializeCards()
    {
        BaseMod.addDynamicVariable(new SecondaryValueVariable());
    }

    @Override
    protected void InitializeRewards()
    {
        SynergyCardsReward.Serializer synergySerializer = new SynergyCardsReward.Serializer();
        BaseMod.registerCustomReward(AbstractEnums.Rewards.SYNERGY_CARDS, synergySerializer, synergySerializer);

        SpecialGoldReward.Serializer goldSerializer = new SpecialGoldReward.Serializer();
        BaseMod.registerCustomReward(AbstractEnums.Rewards.SPECIAL_GOLD, goldSerializer, goldSerializer);
    }

    @Override
    protected void InitializePotions()
    {
        BaseMod.addPotion(FalseLifePotion.class, Color.GOLDENROD.cpy(), Color.WHITE.cpy(), Color.GOLDENROD.cpy(),
                            FalseLifePotion.POTION_ID, AbstractEnums.Characters.THE_ANIMATOR);
    }

    @Override
    protected void InitializePowers()
    {
        BaseMod.addPower(GenericFadingPower.class, GenericFadingPower.POWER_ID);
    }

    @Override
    protected void PostInitialize()
    {
        AnimatorMetrics.Initialize();
        BaseMod.addSaveField("animator_SaveData", PlayerStatistics.Instance);

        Map_Act5Entrance = new Texture("images/ui/map/act5Entrance.png");
        Map_Act5EntranceOutline = new Texture("images/ui/map/act5EntranceOutline.png");
    }

    @Override
    protected void InitializeStrings()
    {
        LoadLanguagePath();

        BaseMod.loadCustomStringsFile(UIStrings.class, languagePath + "UIStrings.json");

        super.InitializeStrings();
    }

    @Override
    protected void InitializeKeywords()
    {
        LoadKeywords(languagePath + "KeywordStrings.json");
    }
}
