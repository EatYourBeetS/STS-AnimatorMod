package eatyourbeets.resources.common;

import basemod.BaseMod;
import basemod.abstracts.CustomSavable;
import com.google.gson.annotations.Expose;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.dungeons.TheUnnamedReign;
import eatyourbeets.events.TheMaskedTraveler1;
import eatyourbeets.events.UnnamedReign.TheAbandonedCabin;
import eatyourbeets.events.UnnamedReign.TheHaunt;
import eatyourbeets.events.UnnamedReign.TheMaskedTraveler3;
import eatyourbeets.events.UnnamedReign.TheUnnamedMerchant;
import eatyourbeets.monsters.Bosses.KrulTepes;
import eatyourbeets.monsters.UnnamedReign.UnnamedEnemyGroup;
import eatyourbeets.powers.common.GenericFadingPower;
import eatyourbeets.resources.AbstractResources;
import eatyourbeets.variables.SecondaryValueVariable;

public class CommonResources extends AbstractResources implements CustomSavable<CommonResources.SaveData>
{
    public final static String ID = "EYB";
    public final CommonStrings Strings = new CommonStrings();
    public final CommonImages Images = new CommonImages();
    public final SaveData CurrentGameData = new SaveData();

    public final String Audio_TheHaunt = "ANIMATOR_THE_HAUNT.ogg";
    public final String Audio_TheUnnamed = "ANIMATOR_THE_UNNAMED.ogg";
    public final String Audio_TheCreature = "ANIMATOR_THE_CREATURE.ogg";
    public final String Audio_TheUltimateCrystal = "ANIMATOR_THE_ULTIMATE_CRYSTAL";
    public CommonImages.Textures Textures;

    public CommonResources()
    {
        super(ID);
    }

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
        Textures = Images.CreateTextures();
        Strings.Initialize();

        BaseMod.addDynamicVariable(new SecondaryValueVariable());
    }

    @Override
    protected void InitializePowers()
    {
        BaseMod.addPower(GenericFadingPower.class, GenericFadingPower.POWER_ID);
    }

    @Override
    protected void PostInitialize()
    {
        BaseMod.addSaveField(SaveData.ID, this);
    }

    @Override
    protected void InitializeStrings()
    {
        LoadCustomStrings(PowerStrings.class);
        LoadCustomStrings(UIStrings.class);
    }

    @Override
    protected void InitializeKeywords()
    {
        LoadKeywords();
    }

    @Override
    protected String GetLanguagePath(Settings.GameLanguage language)
    {
        if (language != Settings.GameLanguage.ZHT && language != Settings.GameLanguage.ZHS)
        {
            language = Settings.GameLanguage.ENG;
        }

        return super.GetLanguagePath(language);
    }

    @Override
    public SaveData onSave()
    {
        CurrentGameData.Validate();
        return CurrentGameData;
    }

    @Override
    public void onLoad(SaveData data)
    {
        CurrentGameData.Import(data);
        CurrentGameData.Validate();
    }

    public static class SaveData
    {
        public static final String ID = CreateID(CommonResources.ID, SaveData.class.getSimpleName());

        public Boolean EnteredUnnamedReign = false;
        public Integer RNGCounter = 0;

        @Expose(serialize = false, deserialize = false)
        private Random rng;

        public void Import(SaveData data)
        {
            if (data != null)
            {
                EnteredUnnamedReign = data.EnteredUnnamedReign;
                RNGCounter = data.RNGCounter;
                rng = data.rng;
            }
            else
            {
                EnteredUnnamedReign = false;
                RNGCounter = 0;
                rng = null;
            }
        }

        public void Validate()
        {
            if (rng != null)
            {
                RNGCounter = rng.counter;
            }
            else if (RNGCounter == null)
            {
                RNGCounter = 0;
            }

            if (EnteredUnnamedReign == null)
            {
                EnteredUnnamedReign = false;
            }
        }

        public Random GetRNG()
        {
            if (rng == null)
            {
                rng = new Random(Settings.seed);
                rng.setCounter(RNGCounter);
            }

            return rng;
        }
    }
}
