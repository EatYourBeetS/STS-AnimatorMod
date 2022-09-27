package eatyourbeets.resources.common;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.StanceStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.console.CommandsManager;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.events.base.EYBEvent;
import eatyourbeets.monsters.EYBMonster;
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

//        AddEnergyTooltip("[R]", AbstractCard.orb_red);
//        AddEnergyTooltip("[G]", AbstractCard.orb_green);
//        AddEnergyTooltip("[B]", AbstractCard.orb_blue);
//        AddEnergyTooltip("[P]", AbstractCard.orb_purple);
        CardTooltips.RegisterEnergyTooltip("[E]", null); // TODO: generalize this

        for (Field field : GameDictionary.class.getDeclaredFields())
        {
            if (field.getType() == Keyword.class)
            {
                try
                {
                    final Keyword k = (Keyword) field.get(null);
                    if (CardTooltips.FindByName(null, k.NAMES[0]) == null)
                    {
                        final String id = JUtils.Capitalize(field.getName());
                        final EYBCardTooltip tooltip = new EYBCardTooltip(JUtils.Capitalize(k.NAMES[0]), k.DESCRIPTION);
                        JUtils.LogInfo(this, "Registering keyword:" + id + " Title:" + tooltip.title);
                        CardTooltips.RegisterID(null, id, tooltip);

                        for (String name : k.NAMES)
                        {
                            CardTooltips.RegisterName(null, name, tooltip);
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
}
