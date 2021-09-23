package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class Souseiseki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Souseiseki.class)
    		.SetAttack(1, CardRarity.COMMON).SetSeriesFromClassPackage();

    public Souseiseki()
    {
        super(DATA);

        Initialize(10, 0, 0, 0);
        SetUpgrade(2, 0, 0, 0);
        SetAffinity_Green(1, 0, 1);
    }

    @Override
    protected void OnUpgrade()
    {
        SetAffinity_Green(2, 0, 2);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (GameUtilities.IsAttacking(m.intent))
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        }

        GameActions.Bottom.ExhaustFromHand(name, 1, false)
                .SetOptions(false, false, false)
                .AddCallback(this::AfterExhaust);
    }

    public void AfterExhaust(ArrayList<AbstractCard> cards)
    {
        if (cards.size() > 0 && GameUtilities.IsHindrance(cards.get(0)))
            GameActions.Bottom.ChangeStance(AgilityStance.STANCE_ID);
    }
}
