package eatyourbeets.cards.animator.special;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.utilities.GameActions;

public class Soujiro_Isami extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Soujiro_Isami.class).SetAttack(0, CardRarity.SPECIAL);

    public Soujiro_Isami()
    {
        super(DATA);

        Initialize(6, 0, 0);
        SetUpgrade(2, 0, 0);

        SetSeries(CardSeries.LogHorizon);
        SetAffinity(0, 2, 0, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);

        if (IsStarter())
        {
            GameActions.Bottom.GainAgility(1, true);
        }
    }
}