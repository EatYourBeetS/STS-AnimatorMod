package eatyourbeets.cards.base.attributes;

import eatyourbeets.cards.base.EYBCard;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.RenderHelpers;

public class DamageAttribute extends AbstractAttribute
{
    public static final DamageAttribute Instance = new DamageAttribute();

    @Override
    public AbstractAttribute SetCard(EYBCard card)
    {
        suffix = null;

        switch (card.attackType)
        {
            case Elemental:
                icon = GR.Common.Images.ElementalDamage.Texture();
                break;

            case Piercing:
                icon = GR.Common.Images.PiercingDamage.Texture();
                break;

            case Ranged:
                icon = GR.Common.Images.RangedDamage.Texture();
                break;

            case Normal:
            default:
                icon = GR.Common.Images.NormalDamage.Texture();
                break;
        }

        if (card.IsAoE())
        {
            iconTag = "AoE";
        }
        else
        {
            iconTag = null;
        }

        mainText = RenderHelpers.GetDamageString(card);

        return this;
    }
}
