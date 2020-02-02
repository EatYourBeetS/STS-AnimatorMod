package eatyourbeets.resources.common;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.console.CommandsManager;
import eatyourbeets.dungeons.TheUnnamedReign;
import eatyourbeets.events.UnnamedReign.TheAbandonedCabin;
import eatyourbeets.events.UnnamedReign.TheHaunt;
import eatyourbeets.events.UnnamedReign.TheMaskedTraveler3;
import eatyourbeets.events.UnnamedReign.TheUnnamedMerchant;
import eatyourbeets.events.animator.TheMaskedTraveler1;
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
        GR.Tooltips.Initialize();
    }

    @Override
    protected void InitializePowers()
    {
        BaseMod.addPower(GenericFadingPower.class, GenericFadingPower.POWER_ID);
    }

    @Override
    protected void PostInitialize()
    {
        CommandsManager.RegisterCommands();
        GR.Tooltips.InitializeIcons();
        GR.UI.Initialize();
        GR.IsLoaded = true;
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

        AddPowerTooltip("[F]", new ForcePower(null, 0));
        AddPowerTooltip("[A]", new AgilityPower(null, 0));
        AddPowerTooltip("[I]", new IntellectPower(null, 0));
        AddEnergyTooltip("[R]", AbstractCard.orb_red);
        AddEnergyTooltip("[G]", AbstractCard.orb_green);
        AddEnergyTooltip("[B]", AbstractCard.orb_blue);
        AddEnergyTooltip("[P]", AbstractCard.orb_purple);
        AddEnergyTooltip("[E]", null); // TODO: generalize this

        for (Field field : GameDictionary.class.getDeclaredFields())
        {
            if (field.getType() == Keyword.class)
            {
                try
                {
                    Keyword k = (Keyword) field.get(null);
                    EYBCardTooltip tooltip = new EYBCardTooltip(k.NAMES[0], k.DESCRIPTION);

                    Tooltips.RegisterID(TipHelper.capitalize(field.getName()), tooltip);

                    for (String name : k.NAMES)
                    {
                        Tooltips.RegisterName(name, tooltip);
                    }
                }
                catch (IllegalAccessException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
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

    private static void AddEnergyTooltip(String symbol, TextureAtlas.AtlasRegion region)
    {
        if (region == null)
        {
            Texture texture = GR.GetTexture(GR.Animator.Images.ORB_C_PNG);
            region = new TextureAtlas.AtlasRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
            //region = new TextureAtlas.AtlasRegion(texture, 2, 2, texture.getWidth()-4, texture.getHeight()-4);
        }

        EYBCardTooltip tooltip = new EYBCardTooltip(TipHelper.TEXT[0], GameDictionary.TEXT[0]);
        tooltip.icon = region;
        Tooltips.RegisterName(symbol, tooltip);
    }

    private static void AddPowerTooltip(String symbol, AbstractPower power)
    {
        int size = power.img.getWidth(); // width should always be equal to height

        EYBCardTooltip tooltip = Tooltips.FindByName(power.name.toLowerCase());
        tooltip.icon = new TextureAtlas.AtlasRegion(power.img, 2, 2, size-4, size-4);
        Tooltips.RegisterName(symbol, tooltip);
    }
}
