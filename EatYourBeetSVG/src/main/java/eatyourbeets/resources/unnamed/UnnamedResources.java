package eatyourbeets.resources.unnamed;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.helpers.CardHelper;
import eatyourbeets.relics.unnamed.InfinitePower;
import eatyourbeets.resources.AbstractResources;

public class UnnamedResources extends AbstractResources
{
    public final static String ID = "unnamed";
    private static String languagePath = null;

    public UnnamedResources()
    {
        super(ID);
    }

    @Override
    protected void PostInitialize()
    {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    protected void InitializeColor()
    {
        Color color = CardHelper.getColor(60, 77, 106);

        BaseMod.addColor(Enums.Cards.THE_UNNAMED, color, color, color, color, color, color, color,
        UnnamedImages.ATTACK_PNG, UnnamedImages.SKILL_PNG, UnnamedImages.POWER_PNG,
        UnnamedImages.ORB_1A_PNG, UnnamedImages.ATTACK_PNG, UnnamedImages.SKILL_PNG,
        UnnamedImages.POWER_PNG, UnnamedImages.ORB_1B_PNG, UnnamedImages.ORB_1C_PNG);
    }

    @Override
    protected void InitializeRelics()
    {
        BaseMod.addRelicToCustomPool(new InfinitePower(), Enums.Cards.THE_UNNAMED);
    }

    @Override
    protected void InitializeKeywords()
    {
        LoadKeywords();
    }
}