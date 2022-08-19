package eatyourbeets.cards.animatorClassic.special;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;

public class Soujiro_Isami extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Soujiro_Isami.class).SetAttack(0, CardRarity.SPECIAL);

    public Soujiro_Isami()
    {
        super(DATA);

        Initialize(6, 0, 0);
        SetUpgrade(2, 0, 0);

        SetMartialArtist();
        SetSeries(CardSeries.LogHorizon);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);

        if (IsStarter())
        {
            GameActions.Bottom.GainAgility(1, true);
        }
    }
}