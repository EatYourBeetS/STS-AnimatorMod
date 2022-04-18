package eatyourbeets.cards.unnamed.common;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.Colors;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class DrainingTouch extends UnnamedCard
{
    public static final EYBCardData DATA = Register(DrainingTouch.class)
            .SetMaxCopies(2)
            .SetAttack(1, CardRarity.COMMON);

    public DrainingTouch()
    {
        super(DATA);

        Initialize(7, 0, 7);
        SetUpgrade(2, 0, 2);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null)
        {
            GameUtilities.GetIntent(m).AddWithering(magicNumber);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.POISON).SetVFXColor(Colors.Violet(1));
        GameActions.Bottom.StackWithering(p, m, magicNumber);
    }
}