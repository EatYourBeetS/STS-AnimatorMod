package eatyourbeets.cards.animatorClassic.series.Katanagatari;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Konayuki extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Konayuki.class).SetSeriesFromClassPackage().SetSkill(2, CardRarity.COMMON, EYBCardTarget.None);

    public Konayuki()
    {
        super(DATA);

        Initialize(0, 5, 3, 1);
        SetUpgrade(0, 3, 0, 0);


    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.GainForce(magicNumber)
        .AddCallback(force ->
        {
            if (force.amount >= 10 && CombatStats.TryActivateLimited(cardID))
            {
                GameEffects.Queue.ShowCardBriefly(this.makeStatEquivalentCopy());
                GameActions.Bottom.DealDamageToRandomEnemy(40, damageTypeForTurn, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .SetOptions(false, false, false)
                .SetPiercing(true, false);
            }
        });
    }
}