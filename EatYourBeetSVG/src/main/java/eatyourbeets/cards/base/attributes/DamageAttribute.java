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
                largeIcon = ICONS.Elemental_L.Texture();
                break;

            case Piercing:
                icon = ICONS.Piercing.Texture();
                largeIcon = ICONS.Piercing_L.Texture();
                break;

            case Ranged:
                icon = ICONS.Ranged.Texture();
                largeIcon = ICONS.Ranged_L.Texture();
                break;

            case Normal:
            default:
                icon = ICONS.Damage.Texture();
                largeIcon = ICONS.Damage_L.Texture();
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
