package eatyourbeets.cards.unnamed.uncommon;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Unburden extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Unburden.class)
            .SetMaxCopies(2)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Unburden()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 1);

        SetExhaust(true);
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        if (CheckSpecialCondition(false))
        {
            SetPlayable(HasHindrance(player.hand) || HasHindrance(player.drawPile) || HasHindrance(player.discardPile));
        }
        else
        {
            SetPlayable(HasHindrance(player.hand));
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        if (IsSolo())
        {
            GameActions.Bottom.ExhaustFromPile(name, 1, p.drawPile, p.hand, p.discardPile)
            .SetFilter(GameUtilities::IsHindrance)
            .AddCallback(cards ->
            {
                if (cards.size() > 0)
                {
                    GameActions.Bottom.GainStrength(magicNumber);
                }
            });
        }
        else
        {
            GameActions.Bottom.ExhaustFromHand(name, 1, false)
            .SetFilter(GameUtilities::IsHindrance)
            .AddCallback(cards ->
            {
                if (cards.size() > 0)
                {
                    for (AbstractCreature ally : GetAllies())
                    {
                        GameActions.Bottom.GainStrength(player, ally, magicNumber);
                    }
                }
            });
        }
    }

    @Override
    public boolean CheckSpecialCondition(boolean tryUse)
    {
        return IsSolo();
    }

    private boolean HasHindrance(CardGroup group)
    {
        for (AbstractCard c : group.group)
        {
            if (GameUtilities.IsHindrance(c))
            {
                return true;
            }
        }

        return false;
    }
}