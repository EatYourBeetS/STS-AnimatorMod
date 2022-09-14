package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.HPAttribute;
import eatyourbeets.interfaces.subscribers.OnAffinityGainedSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Gluttony extends AnimatorCard implements OnAffinityGainedSubscriber
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
    public int OnAffinityGained(Affinity affinity, int amount)
    {
        if (amount > 0 && affinity == Affinity.Dark && player.hand.contains(this) && GameUtilities.Retain(this))
        {
            flash();
        }

        return 0;
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

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);

        CombatStats.onAffinityGained.Subscribe(this);
    }
}