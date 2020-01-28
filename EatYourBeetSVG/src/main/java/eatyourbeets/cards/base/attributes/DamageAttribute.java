package eatyourbeets.cards.base.attributes;

import eatyourbeets.cards.base.EYBCard;
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
                icon = ICONS.Elemental.Texture();
                break;

            case Piercing:
                icon = ICONS.Piercing.Texture();
                break;

            case Ranged:
                icon = ICONS.Ranged.Texture();
                break;

            case Normal:
            default:
                icon = ICONS.Damage.Texture();
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
