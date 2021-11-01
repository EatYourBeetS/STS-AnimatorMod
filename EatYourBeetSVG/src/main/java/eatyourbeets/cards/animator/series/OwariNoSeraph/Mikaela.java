package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Mikaela extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Mikaela.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Mikaela()
    {
        super(DATA);

        Initialize(7, 0, 2, 4);
        SetUpgrade(2, 0, 1, 0);

        SetAffinity_Red(1, 1, 1);
        SetAffinity_Dark(1,0,1);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);
        GameActions.Bottom.ExhaustFromPile(name, 1, p.discardPile)
        .SetOptions(false, false)
        .AddCallback(cards ->
        {
            if (cards.size() > 0 && GameUtilities.IsHindrance(cards.get(0)))
            {
                GameActions.Bottom.GainForce(secondaryValue);
                GameActions.Bottom.DealDamageAtEndOfTurn(player, player, secondaryValue, AttackEffects.SLASH_VERTICAL);
            }
        });
    }
}