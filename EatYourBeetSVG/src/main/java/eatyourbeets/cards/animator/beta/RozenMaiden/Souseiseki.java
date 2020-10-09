package eatyourbeets.cards.animator.beta.RozenMaiden;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class Souseiseki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Souseiseki.class)
    		.SetAttack(1, CardRarity.COMMON);

    public Souseiseki()
    {
        super(DATA);

        Initialize(10, 0, 0, 0);
        SetUpgrade(2, 0, 0, 0);
        
        SetScaling(0, 1, 0);
        SetSynergy(Synergies.RozenMaiden);
    }

    @Override
    protected void OnUpgrade()
    {
    	SetScaling(intellectScaling, agilityScaling + 1, forceScaling);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
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
        if (cards.size() > 0 && GameUtilities.IsCurseOrStatus(cards.get(0)))
            GameActions.Bottom.ChangeStance(AgilityStance.STANCE_ID);
    }
}
