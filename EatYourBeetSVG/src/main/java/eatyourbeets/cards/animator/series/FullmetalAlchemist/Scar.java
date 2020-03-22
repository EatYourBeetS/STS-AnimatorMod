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
    public static final EYBCardData DATA = Register(Scar.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental);

    public Scar()
    {
        super(DATA);

        Initialize(14, 0, 6, 0);
        SetUpgrade(2, 0, 2, 0);
        SetScaling(1, 0, 1);

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Top.ExhaustFromHand(name, 1, true)
        .ShowEffect(true, true);

        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE)
        .SetDamageEffect(enemy ->
        {
            CardCrawlGame.sound.playA("ORB_DARK_EVOKE", -0.3f);
            GameActions.Bottom.ReduceStrength(enemy, magicNumber, true);
        });
    }
}