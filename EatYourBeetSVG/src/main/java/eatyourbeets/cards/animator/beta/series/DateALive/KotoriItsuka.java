package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;

public class KotoriItsuka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KotoriItsuka.class).SetAttack(3, CardRarity.RARE, EYBAttackType.Normal);

    public KotoriItsuka()
    {
        super(DATA);

        Initialize(8, 0, 2, 5);
        SetUpgrade(4, 0, 0);
        SetExhaust(true);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (int i=0; i<magicNumber; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.FIRE);
        }

        GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));

        GameActions.Bottom.ExhaustFromHand(name, secondaryValue, false)
                .SetOptions(true, true, true)
        .AddCallback(cards ->
        {
            int amountExhausted = 0;

            for (AbstractCard card : cards)
            {
                amountExhausted++;

                GameActions.Bottom.GainAgility(1, true);
                GameActions.Bottom.GainForce(1, true);
                GameActions.Bottom.MakeCardInHand(new Burn());
            }

            if (amountExhausted == secondaryValue)
            {
                GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID);
            }
        });
    }
}