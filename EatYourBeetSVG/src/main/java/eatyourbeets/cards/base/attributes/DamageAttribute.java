package eatyourbeets.cards.base.attributes;

import eatyourbeets.cards.base.EYBCard;

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

        if (card.attackTarget != null)
        {
            iconTag = card.attackTarget.tag;
        }

        mainText = card.GetDamageString();

        return this;
    }
}
