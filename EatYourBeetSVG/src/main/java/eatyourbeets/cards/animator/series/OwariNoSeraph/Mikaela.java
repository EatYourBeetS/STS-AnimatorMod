package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Mikaela extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Mikaela.class).SetAttack(1, CardRarity.COMMON);

    public Mikaela()
    {
        super(DATA);

        Initialize(6, 0, 2, 2);
        SetUpgrade(2, 0, 1, 0);
        SetScaling(0, 0, 1);

        SetSynergy(Synergies.OwariNoSeraph);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        GameActions.Bottom.ExhaustFromPile(name, 1, p.discardPile)
        .SetOptions(false, false)
        .AddCallback(cards ->
        {
            if (cards.size() > 0 && GameUtilities.IsCurseOrStatus(cards.get(0)))
            {
                GameActions.Bottom.GainForce(secondaryValue);
            }
        });
    }
}