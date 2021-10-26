package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.animator.GurenAction;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;

public class Guren extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Guren.class)
            .SetSkill(3, CardRarity.RARE)
            .SetSeriesFromClassPackage();

    private boolean alreadyPlayed = false;

    public Guren()
    {
        super(DATA);

        Initialize(0, 0,3);
        SetUpgrade(0,0,1);

        SetAffinity_Light(2);
        SetAffinity_Dark(2);

        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetExhaust(false);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.Add(new GurenAction(m));
        }
    }
}