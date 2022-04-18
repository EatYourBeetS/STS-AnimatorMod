package eatyourbeets.cards.unnamed.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.HPAttribute;
import eatyourbeets.monsters.PlayerMinions.UnnamedDoll;
import eatyourbeets.utilities.GameActions;

public class AdvancedSummon extends UnnamedCard
{
    public static final EYBCardData DATA = Register(AdvancedSummon.class)
            .SetMaxCopies(2)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None);

    public AdvancedSummon()
    {
        super(DATA);

        Initialize(0, 0, 2, 1);

        SetSummon(true);
        SetExhaust(true);
        SetHealing(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return HPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.HealPlayerLimited(this, magicNumber);
        GameActions.Bottom.SummonDoll(1)
        .AddCallback(dolls ->
        {
            for (UnnamedDoll doll : dolls)
            {
                GameActions.Bottom.GainStrength(player, doll, secondaryValue);
                GameActions.Bottom.GainDexterity(player, doll, secondaryValue);

                if (upgraded)
                {
                    GameActions.Delayed.ActivateDoll(doll, 1);
                }
            }
        });
    }
}