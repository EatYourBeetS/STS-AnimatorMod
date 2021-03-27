package eatyourbeets.resources.common;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.console.CommandsManager;
import eatyourbeets.events.base.EYBEvent;
import eatyourbeets.monsters.EYBMonster;
import eatyourbeets.powers.common.AgilityPower;
import eatyourbeets.powers.common.ForcePower;
import eatyourbeets.powers.common.GenericFadingPower;
import eatyourbeets.powers.common.IntellectPower;
import eatyourbeets.resources.AbstractResources;
import eatyourbeets.resources.CardTooltips;
import eatyourbeets.resources.GR;
import eatyourbeets.stances.EYBStance;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.JUtils;

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
        EYBEvent.RegisterEvents();
    }

    @Override
    protected void InitializeAudio()
    {
        BaseMod.addAudio("ANIMATOR_REAPER", "audio/sound/STS_SFX_Reaper_v1.ogg");
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
        EYBMonster.RegisterMonsters();
    }

    @Override
    protected void InitializeCards()
    {
        JUtils.LogInfo(this, "InitializeCards();");

        Strings.Initialize();
        GR.Tooltips = new CardTooltips();
        EYBStance.Initialize();
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
        JUtils.LogInfo(this, "InitializeStrings();");

        LoadCustomStrings(PowerStrings.class);
        LoadCustomStrings(StanceStrings.class);
        LoadCustomStrings(UIStrings.class);

        EYBFontHelper.Initialize();
    }

    @Override
    protected void InitializeKeywords()
    {
        JUtils.LogInfo(this, "InitializeKeywords();");

        LoadKeywords();

        AddPowerTooltip("[F]", "Force", new ForcePower(null, 0));
        AddPowerTooltip("[A]", "Agility", new AgilityPower(null, 0));
        AddPowerTooltip("[I]", "Intellect", new IntellectPower(null, 0));
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
                    EYBCardTooltip tooltip = new EYBCardTooltip(TipHelper.capitalize(k.NAMES[0]), k.DESCRIPTION);

                    CardTooltips.RegisterID(TipHelper.capitalize(field.getName()), tooltip);

                    for (String name : k.NAMES)
                    {
                        CardTooltips.RegisterName(name, tooltip);
                    }
                }
                catch (IllegalAccessException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
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
        CardTooltips.RegisterName(symbol, tooltip);
    }

    private static void AddPowerTooltip(String symbol, String id, AbstractPower power)
    {
        int size = power.img.getWidth(); // width should always be equal to height

        EYBCardTooltip tooltip = CardTooltips.FindByID(id);
        if (tooltip == null)
        {
            JUtils.LogError(CommonResources.class, "Could not find tooltip: Symbol: {0}, ID: {1}, Power: {2} ",
                    symbol, id, power.name);
            return;
        }

        tooltip.icon = new TextureAtlas.AtlasRegion(power.img, 2, 4, size-4, size-4);

        EYBCardTooltip stance = CardTooltips.FindByID(id + " Stance");
        if (stance != null)
        {
            stance.icon = tooltip.icon;
        }

        CardTooltips.RegisterName(symbol, tooltip);
    }
}
