package eatyourbeets.cards.unnamed.common;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Dishearten extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Dishearten.class)
            .SetMaxCopies(2)
            .SetSkill(2, CardRarity.COMMON);

    public Dishearten()
    {
        super(DATA);

        Initialize(0, 0, 20);
        SetUpgrade(0, 0, 10);
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
        GameActions.Bottom.StackWithering(p, m, magicNumber);

        if (CheckSpecialCondition(true))
        {
            GameActions.Bottom.GainEnergy(1);
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return IsSolo() && ((tryUse && CombatStats.TryActivateSemiLimited(cardID)) || CombatStats.CanActivateSemiLimited(cardID));
    }
}