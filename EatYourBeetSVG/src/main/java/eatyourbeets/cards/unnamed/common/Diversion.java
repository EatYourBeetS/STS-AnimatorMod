package eatyourbeets.cards.unnamed.common;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.UnnamedCard;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.SpecialAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class Diversion extends UnnamedCard
{
    public static final EYBCardData DATA = Register(Diversion.class)
            .SetAttack(0, CardRarity.COMMON);

    public Diversion()
    {
        super(DATA);

        Initialize(4, 0, 2);
        SetUpgrade(3, 0, 0);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return (inBattle && secondaryValue > 0) ? SpecialAttribute.Instance.SetCard(this, GR.Tooltips.Amplification, secondaryValue) : null;
    }

    @Override
    protected void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        secondaryValue = 0;
        for (AbstractCard c : player.hand.group)
        {
            if (CanReshuffle(c))
            {
                secondaryValue += magicNumber;
            }
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);
        GameActions.Bottom.MoveCards(player.hand, player.drawPile)
        .SetFilter(this::CanReshuffle)
        .SetDestination(CardSelection.Random)
        .AddCallback(m, (target, cards) ->
        {
            if (cards.size() > 0)
            {
                GameActions.Bottom.StackAmplification(player, target, cards.size() * magicNumber);
            }
        });
    }

    private boolean CanReshuffle(AbstractCard c)
    {
        return c.type == CardType.SKILL && !c.uuid.equals(uuid);
    }
}