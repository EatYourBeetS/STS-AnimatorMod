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
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.events.base.EYBEvent;
import eatyourbeets.monsters.EYBMonster;
import eatyourbeets.powers.affinity.*;
import eatyourbeets.powers.replacement.GenericFadingPower;
import eatyourbeets.resources.AbstractResources;
import eatyourbeets.resources.CardTooltips;
import eatyourbeets.resources.GR;
import eatyourbeets.stances.EYBStance;
import eatyourbeets.utilities.EYBFontHelper;
import eatyourbeets.utilities.JUtils;

import java.lang.reflect.Field;

public class CommonResources extends AbstractResources
{
    public final static String ID = "eyb";
    public final CommonDungeonData Dungeon = CommonDungeonData.Register(CreateID("Data"));
    public final CommonStrings Strings = new CommonStrings();
    public final CommonImages Images = new CommonImages();

    public CommonResources()
    {
        super(ID, AbstractCard.CardColor.COLORLESS, null, null);
    }

    @Override
    protected void InitializeEvents()
    {
        if (isLoaded)
        {
            return;
        }

        EYBEvent.RegisterEvents();
    }

    @Override
    protected void InitializeAudio()
    {
        if (isLoaded)
        {
            return;
        }

        SFX.Initialize();
    }

    @Override
    protected void InitializeMonsters()
    {
        if (isLoaded)
        {
            return;
        }

        EYBMonster.RegisterMonsters();
    }

    @Override
    protected void InitializeCards()
    {
        if (isLoaded)
        {
            return;
        }

        JUtils.LogInfo(this, "InitializeCards();");

        Strings.Initialize();
        GR.Tooltips = new CardTooltips();
        EYBStance.Initialize();
    }

    @Override
    protected void InitializePowers()
    {
        if (isLoaded)
        {
            return;
        }

        BaseMod.addPower(GenericFadingPower.class, GenericFadingPower.POWER_ID);
    }

    @Override
    protected void PostInitialize()
    {
        if (isLoaded)
        {
            return;
        }

        AttackEffects.Initialize();
        CommandsManager.RegisterCommands();
        GR.Tooltips.InitializeIcons();
        GR.UI.Initialize();
        isLoaded = true;
    }

    @Override
    protected void InitializeStrings()
    {
        if (isLoaded)
        {
            return;
        }

        JUtils.LogInfo(this, "InitializeStrings();");

        LoadCustomStrings(PowerStrings.class);
        LoadCustomStrings(StanceStrings.class);
        LoadCustomStrings(UIStrings.class);

        EYBFontHelper.Initialize();
    }

    @Override
    protected void InitializeKeywords()
    {
        if (isLoaded)
        {
            return;
        }

        JUtils.LogInfo(this, "InitializeKeywords();");

        LoadKeywords(null);

        AddPowerTooltip("[F]", "Force", new ForcePower());
        AddPowerTooltip("[A]", "Agility", new AgilityPower());
        AddPowerTooltip("[I]", "Intellect", new IntellectPower());
        AddPowerTooltip("[B]", "Blessing", new BlessingPower());
        AddPowerTooltip("[C]", "Corruption", new CorruptionPower());
//        AddEnergyTooltip("[R]", AbstractCard.orb_red);
//        AddEnergyTooltip("[G]", AbstractCard.orb_green);
//        AddEnergyTooltip("[B]", AbstractCard.orb_blue);
//        AddEnergyTooltip("[P]", AbstractCard.orb_purple);
        AddEnergyTooltip("[E]", null); // TODO: generalize this

        for (Field field : GameDictionary.class.getDeclaredFields())
        {
            if (field.getType() == Keyword.class)
            {
                try
                {
                    final Keyword k = (Keyword) field.get(null);
                    if (CardTooltips.FindByName(k.NAMES[0]) == null)
                    {
                        final EYBCardTooltip tooltip = new EYBCardTooltip(JUtils.Capitalize(k.NAMES[0]), k.DESCRIPTION);
                        CardTooltips.RegisterID(JUtils.Capitalize(field.getName()), tooltip);

                        for (String name : k.NAMES)
                        {
                            CardTooltips.RegisterName(name, tooltip);
                        }
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

        tooltip.icon = new TextureAtlas.AtlasRegion(power.img, 3, 5, size-6, size-6);
        //tooltip.icon = new TextureAtlas.AtlasRegion(power.img, 2, 4, size-4, size-4);

        EYBCardTooltip stance = CardTooltips.FindByID(id + " Stance");
        if (stance != null)
        {
            stance.icon = tooltip.icon;
        }

        CardTooltips.RegisterName(symbol, tooltip);
    }
}
