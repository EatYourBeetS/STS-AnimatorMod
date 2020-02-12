package eatyourbeets.cards.animator.series.FullmetalAlchemist;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.utilities.GameActions;

public class Scar extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Scar.class).SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Elemental);

    public Scar()
    {
        super(DATA);

        Initialize(12, 0, 2);
        SetUpgrade(4, 0);

        SetExhaust(true);
        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Top.ExhaustFromHand(name, 1, true)
        .ShowEffect(true, true)
        .AddCallback(m, (enemy, cards) ->
        {
            if (cards != null && cards.size() > 0)
            {
                GameActions.Bottom.ReduceStrength((AbstractMonster)enemy, cards.get(0).cost, false);
            }
        });

        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE)
        .SetDamageEffect(__ -> CardCrawlGame.sound.playA("ORB_DARK_EVOKE", -0.3F));
    }
}