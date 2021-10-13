package eatyourbeets.cards.animator.series.Konosuba;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.listeners.OnAddToDeckListener;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Chris extends AnimatorCard implements OnAddToDeckListener
{
    public static final EYBCardData DATA = Register(Chris.class)
            .SetAttack(0, CardRarity.RARE)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(new Eris(), false));

    public Chris()
    {
        super(DATA);

        Initialize(4, 0, 10, 8);
        SetUpgrade(2, 0, 0, -3);

        SetAffinity_Air();

        SetAffinityRequirement(Affinity.Air, secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (CheckAffinity(Affinity.Air))
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.DAGGER).StealGold(magicNumber);
        }
        else
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.DAGGER);
        }
    }

    @Override
    public boolean OnAddToDeck()
    {
        GameEffects.TopLevelList.ShowAndObtain(new Eris());

        return true;
    }
}