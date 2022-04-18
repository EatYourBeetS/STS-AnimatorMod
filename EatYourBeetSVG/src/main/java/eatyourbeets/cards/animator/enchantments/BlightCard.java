package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.blights.AbstractBlight;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.AdvancedTexture;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class BlightCard extends AnimatorCard implements Hidden
{
    public static final EYBCardData DATA = Register(BlightCard.class)
            .SetCurse(-2, EYBCardTarget.None, true)
            .SetImagePath(GR.GetCardImage(Enchantment.ID));

    public AbstractBlight blight;
    public EYBCardTooltip tooltip;

    public BlightCard(AbstractBlight blight)
    {
        super(DATA);

        this.cropPortrait = false;
        this.blight = blight;
        this.portraitForeground = new AdvancedTexture(blight.img, null);
        this.portraitForeground.pos.scale = 2;
        this.tooltip = new EYBCardTooltip(blight.name, blight.description);
        this.tooltip.SetIcon(blight);
        GameUtilities.ChangeCardName(this, blight.name);
    }

    @Override
    protected String GetTypeText()
    {
        return "Blight";
    }

    @Override
    public void GenerateDynamicTooltips(ArrayList<EYBCardTooltip> dynamicTooltips)
    {
        super.GenerateDynamicTooltips(dynamicTooltips);

        dynamicTooltips.add(tooltip);
    }
}