package eatyourbeets.resources.common;

import basemod.BaseMod;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.dungeons.TheUnnamedReign;
import eatyourbeets.events.UnnamedReign.TheAbandonedCabin;
import eatyourbeets.events.UnnamedReign.TheHaunt;
import eatyourbeets.events.UnnamedReign.TheMaskedTraveler3;
import eatyourbeets.events.UnnamedReign.TheUnnamedMerchant;
import eatyourbeets.events.animator.TheMaskedTraveler1;
import eatyourbeets.interfaces.csharp.ActionT1;
import eatyourbeets.monsters.Bosses.KrulTepes;
import eatyourbeets.monsters.UnnamedReign.UnnamedEnemyGroup;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.powers.common.GenericFadingPower;
import eatyourbeets.powers.common.IntellectPower;
import eatyourbeets.resources.AbstractResources;
import eatyourbeets.resources.GR;

import java.lang.reflect.Field;

public class CommonResources extends AbstractResources
{
    public final static String ID = "EYB";
    public final CommonDungeonData Dungeon = CommonDungeonData.Register(CreateID("Data"));
    public final CommonStrings Strings = new CommonStrings();
    public final CommonImages Images = new CommonImages();

    public final String Audio_TheUltimateCrystal = "ANIMATOR_THE_ULTIMATE_CRYSTAL";
    public final String Audio_TheCreature = "ANIMATOR_THE_CREATURE.ogg";
    public final String Audio_TheUnnamed = "ANIMATOR_THE_UNNAMED.ogg";
    public final String Audio_TheHaunt = "ANIMATOR_THE_HAUNT.ogg";

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
        Strings.Initialize();
    }

    @Override
    protected void InitializePowers()
    {
        BaseMod.addPower(GenericFadingPower.class, GenericFadingPower.POWER_ID);
    }

    @Override
    protected void PostInitialize()
    {
        GR.UI.Initialize();
        GR.IsLoaded = true;

        //CTContext.Test();
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

        for (Field field : GameDictionary.class.getDeclaredFields())
        {
            if (field.getType() == Keyword.class)
            {
                try
                {
                    Keyword k = (Keyword) field.get(null);
                    EYBCardTooltip tooltip = new EYBCardTooltip(k.NAMES[0], k.DESCRIPTION);

                    tooltipIDs.put(TipHelper.capitalize(field.getName()), tooltip);

                    for (String name : k.NAMES)
                    {
                        tooltips.put(name, tooltip);
                    }

//                keywords.put("[R]", GameDictionary.TEXT[0]);
//                keywords.put("[G]", GameDictionary.TEXT[0]);
//                keywords.put("[B]", GameDictionary.TEXT[0]);
//                keywords.put("[W]", GameDictionary.TEXT[0]);
//                keywords.put("[E]", GameDictionary.TEXT[0]);
//                TextureAtlas.AtlasRegion currentOrb = AbstractDungeon.player != null ? AbstractDungeon.player.getOrb() : AbstractCard.orb_red;
                }
                catch (IllegalAccessException ex)
                {
                    ex.printStackTrace();
                }
            }
        }

        ActionT1<AbstractPower> addIcon = (power) -> GR.GetTooltip(power.name.toLowerCase()).icon = power.img;
        addIcon.Invoke(new ForcePower(null, 0));
        addIcon.Invoke(new AgilityPower(null, 0));
        addIcon.Invoke(new IntellectPower(null, 0));
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
}
