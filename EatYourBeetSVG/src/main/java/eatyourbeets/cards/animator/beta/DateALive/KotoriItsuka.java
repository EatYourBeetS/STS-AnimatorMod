package eatyourbeets.cards.animator.beta.DateALive;

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
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

public class KotoriItsuka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KotoriItsuka.class).SetAttack(2, CardRarity.RARE, EYBAttackType.Normal);

    public KotoriItsuka()
    {
        super(DATA);

        Initialize(5, 0, 3, 5);
        SetScaling(0, 0, 0);

        SetSynergy(Synergies.DateALive);
    }

    @Override
    protected void OnUpgrade()
    {
        SetScaling(0, 0, 2);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.FIRE);
        }

        GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));

        if (EffectHistory.TryActivateLimited(this.cardID))
        {
            GameActions.Bottom.ExhaustFromHand(name, secondaryValue, false)
            .SetOptions(true, true, true)
            .AddCallback(cards ->
            {
                for (AbstractCard card : cards)
                {
                    GameActions.Bottom.GainAgility(1, true);
                    GameActions.Bottom.GainForce(1, true);
                    GameActions.Bottom.GainIntellect(1, true);
                    GameActions.Bottom.MakeCardInHand(new Burn());
                }
            });
        }
    }
}