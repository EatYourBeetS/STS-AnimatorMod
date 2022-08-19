package eatyourbeets.cards.animatorClassic.series.TenseiSlime;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawCardNextTurnPower;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;

public class Shion extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Shion.class).SetAttack(2, CardRarity.COMMON);

    public Shion()
    {
        super(DATA);

        Initialize(16, 0, 2);
        SetUpgrade(5, 0, 0);
        SetScaling(0, 0, 1);

        SetSeries(CardSeries.TenseiSlime);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);

        if (info.IsSynergizing && CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID);
        }
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DiscardFromHand(name, 1, false)
        .SetOptions(false, false, false);
        GameActions.Bottom.StackPower(new DrawCardNextTurnPower(p, 1));
    }
}