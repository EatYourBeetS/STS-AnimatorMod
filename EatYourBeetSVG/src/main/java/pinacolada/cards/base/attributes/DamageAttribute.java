package pinacolada.cards.base.attributes;

import pinacolada.cards.base.PCLCard;

public class DamageAttribute extends AbstractAttribute
{
    public static final DamageAttribute Instance = new DamageAttribute();

    @Override
    public AbstractAttribute SetCard(PCLCard card)
    {
        suffix = null;

        switch (card.attackType)
        {
            case Brutal:
                icon = ICONS.Brutal_L.Texture();
                largeIcon = ICONS.Brutal.Texture();
                break;

            case Dark:
                icon = ICONS.Dark.Texture();
                largeIcon = ICONS.Dark_L.Texture();
                break;

            case Electric:
                icon = ICONS.Electric.Texture();
                largeIcon = ICONS.Electric_L.Texture();
                break;

            case Fire:
                icon = ICONS.Fire.Texture();
                largeIcon = ICONS.Fire_L.Texture();
                break;

            case Ice:
                icon = ICONS.Ice.Texture();
                largeIcon = ICONS.Ice_L.Texture();
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
