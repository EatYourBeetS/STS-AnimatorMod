package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.HPAttribute;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Gluttony extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Gluttony.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .ObtainableAsReward((data, deck) -> deck.size() >= (18 + (6 * data.GetTotalCopies(deck))));

    public Gluttony()
    {
        super(DATA);

        Initialize(0, 0, 6, 4);
        SetUpgrade(0, 0, 2);

        SetAffinity_Red(1, 1, 0);
        SetAffinity_Dark(2);

        SetHealing(true);
        SetExhaust(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return HPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        super.triggerOnOtherCardPlayed(c);

        if (GameUtilities.HasDarkAffinity(c) && GameUtilities.Retain(this))
        {
            flash();
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Top.HealPlayerLimited(this, magicNumber);

        if (CheckSpecialCondition(true))
        {
            GameActions.Top.MoveCards(p.drawPile, p.exhaustPile, secondaryValue)
            .ShowEffect(true, true)
            .SetOrigin(CardSelection.Top)
            .AddCallback(cards ->
            {
                for (AbstractCard c : cards)
                {
                    GameActions.Top.SealAffinities(c, false);
                }
            });
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return player.drawPile.size() >= secondaryValue;
    }
}