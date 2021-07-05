package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Konayuki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Konayuki.class).SetSkill(2, CardRarity.COMMON, EYBCardTarget.None);

    public Konayuki()
    {
        super(DATA);

        Initialize(0, 5, 3, 1);
        SetUpgrade(0, 3, 0, 0);

        SetSynergy(Synergies.Katanagatari);
        SetAffinity(2, 0, 0, 0, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
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