package eatyourbeets.cards.animator.status;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Overheat extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Overheat.class)
            .SetStatus(0, CardRarity.COMMON, EYBCardTarget.None);

    public Overheat()
    {
        super(DATA);

        Initialize(0, 0, 4);

        SetAffinity_Red(1);

        SetEndOfTurnPlay(false);
        SetPurge(true);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.TakeDamageAtEndOfTurn(magicNumber, AttackEffects.FIRE);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (!this.dontTriggerOnUseCard)
        {
            GameActions.Bottom.Draw(1);
            GameActions.Bottom.MakeCardInDrawPile(new Status_Burn());
        }
    }
}