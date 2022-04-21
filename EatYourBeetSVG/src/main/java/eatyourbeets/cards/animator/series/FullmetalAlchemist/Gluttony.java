package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.MixedHPAttribute;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Gluttony extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Gluttony.class)
            .SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetSeriesFromClassPackage()
            .ObtainableAsReward((data, deck) -> deck.size() >= (18 + (4 * data.GetTotalCopies(deck))));
    public static final int EXHAUST_AMOUNT = 4;

    public Gluttony()
    {
        super(DATA);

        Initialize(0, 0, 8, 2);
        SetUpgrade(0, 0, 0, 2);

        SetAffinity_Red(1, 1, 0);
        SetAffinity_Dark(2);

        SetHealing(true);
        SetExhaust(true);
    }

    @Override
    public ColoredString GetSpecialVariableString()
    {
        return super.GetSpecialVariableString(EXHAUST_AMOUNT);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return (inBattle && CheckSpecialCondition(false)) ? MixedHPAttribute.Instance.SetCard(this, true) : null;
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
        if (CheckSpecialCondition(true))
        {
            GameActions.Top.MoveCards(p.drawPile, p.exhaustPile, EXHAUST_AMOUNT)
            .ShowEffect(true, true)
            .SetOrigin(CardSelection.Top)
            .AddCallback(cards ->
            {
                if (cards.size() >= EXHAUST_AMOUNT)
                {
                    GameActions.Bottom.HealPlayerLimited(this, magicNumber).Overheal(true);
                    GameActions.Bottom.GainForce(secondaryValue, false);
                    GameActions.Bottom.GainCorruption(secondaryValue, false);
                }
            });
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return player.drawPile.size() >= EXHAUST_AMOUNT;
    }
}