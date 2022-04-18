package eatyourbeets.cards.animator.enchantments;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.EYBCardTooltip;
import eatyourbeets.interfaces.markers.Hidden;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.AdvancedTexture;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class RelicCard extends AnimatorCard implements Hidden
{
    public static final EYBCardData DATA = Register(RelicCard.class)
            .SetCurse(-2, EYBCardTarget.None, true)
            .SetImagePath(GR.GetCardImage(Enchantment.ID));

    public AbstractRelic relic;
    public EYBCardTooltip tooltip;

    public RelicCard(AbstractRelic relic)
    {
        super(DATA);

        this.cropPortrait = false;
        this.relic = relic;
        this.portraitForeground = new AdvancedTexture(relic.img, null);
        this.portraitForeground.pos.scale = 2;
        this.tooltip = new EYBCardTooltip(relic.name, relic.description);
        this.tooltip.SetIcon(relic);
        GameUtilities.ChangeCardName(this, relic.name);
    }

    @Override
    protected String GetTypeText()
    {
        return "Relic";
    }

    @Override
    public void GenerateDynamicTooltips(ArrayList<EYBCardTooltip> dynamicTooltips)
    {
        super.GenerateDynamicTooltips(dynamicTooltips);

        dynamicTooltips.add(tooltip);
    }
}