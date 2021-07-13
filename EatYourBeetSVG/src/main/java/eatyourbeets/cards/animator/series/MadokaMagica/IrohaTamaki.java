package eatyourbeets.cards.animator.series.MadokaMagica;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class IrohaTamaki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(IrohaTamaki.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental);

    public IrohaTamaki()
    {
        super(DATA);

        Initialize(3, 5, 2);
        SetUpgrade(0, 2, 1);

        SetSeries(CardSeries.MadokaMagica);
        SetAffinity(0, 0, 1, 1, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_VERTICAL);

        if (p.drawPile.size() > 0)
        {
            AbstractCard topCard = p.drawPile.getTopCard();
            if (GameUtilities.IsCurseOrStatus(topCard))
            {
                GameActions.Bottom.Exhaust(topCard);
            }
        }

        if (isSynergizing)
        {
            GameActions.Bottom.Scry(magicNumber);
        }
    }
}