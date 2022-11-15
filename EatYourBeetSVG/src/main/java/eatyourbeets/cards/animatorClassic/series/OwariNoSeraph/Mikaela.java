package eatyourbeets.cards.animatorClassic.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Mikaela extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Mikaela.class).SetSeriesFromClassPackage().SetAttack(1, CardRarity.COMMON);

    public Mikaela()
    {
        super(DATA);

        Initialize(6, 0, 2, 2);
        SetUpgrade(2, 0, 1, 0);
        SetScaling(0, 0, 1);


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
            }
        });
    }
}