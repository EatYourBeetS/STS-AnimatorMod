package eatyourbeets.cards.unnamed.status;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class Status_Burn extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Status_Burn.class)
            .SetStatus(UNPLAYABLE_COST, CardRarity.COMMON, EYBCardTarget.None);

    public Status_Burn()
    {
        this(false);
    }

    public Status_Burn(boolean upgrade)
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 2);

        SetEndOfTurnPlay(true);

        if (upgrade)
        {
            upgrade();
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        if (dontTriggerOnUseCard)
        {
            GameActions.Bottom.DealDamage(p, p, magicNumber, DamageInfo.DamageType.THORNS, AttackEffects.FIRE).CanKill(false);
        }
    }
}