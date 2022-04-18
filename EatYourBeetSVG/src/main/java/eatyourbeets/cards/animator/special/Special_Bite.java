package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class Special_Bite extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Special_Bite.class)
            .SetAttack(1, CardRarity.SPECIAL)
            .SetColor(CardColor.COLORLESS);

    public Special_Bite()
    {
        super(DATA);

        Initialize(9, 0, 3);
        SetUpgrade(3, 0, 0);

        SetAffinity_Dark(1);
        SetAffinity_Red(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BITE);
        GameActions.Bottom.RecoverHP(magicNumber);
    }
}