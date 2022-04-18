package eatyourbeets.cards_beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards_beta.LogHorizon.Soujiro;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class Soujiro_Isami extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Soujiro_Isami.class)
            .SetAttack(0, CardRarity.SPECIAL)
            .SetSeries(Soujiro.DATA.Series);

    public Soujiro_Isami()
    {
        super(DATA);

        Initialize(6, 0, 0);
        SetUpgrade(2, 0, 0);

        SetAffinity_Green(2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_DIAGONAL);

        if (info.TryActivateStarter())
        {
            GameActions.Bottom.GainAgility(1, true);
        }
    }
}