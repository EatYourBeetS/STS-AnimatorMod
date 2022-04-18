package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.basic.ImprovedStrike;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.RandomizedList;

public class Sora_BattlePlan1 extends Sora_BattlePlan
{
    public static final EYBCardData DATA = Register(Sora_BattlePlan1.class)
            .SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.Normal)
            .SetImagePath(IMAGE_PATH)
            .SetSeries(SERIES);

    public Sora_BattlePlan1()
    {
        super(DATA);

        Initialize(0, 0, 2, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.ApplyVulnerable(p, m, magicNumber);

        final RandomizedList<EYBCardData> cards = new RandomizedList<>(ImprovedStrike.GetCards());
        for (int i = 0; i < secondaryValue; i++)
        {
            final AbstractCard c = cards.Retrieve(rng).MakeCopy(false);
            GameUtilities.ModifyCostForCombat(c, 0, false);
            GameActions.Bottom.MakeCardInHand(c);
        }
    }
}