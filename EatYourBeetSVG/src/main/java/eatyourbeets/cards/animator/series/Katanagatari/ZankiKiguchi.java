package eatyourbeets.cards.animator.series.Katanagatari;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class ZankiKiguchi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(ZankiKiguchi.class).SetAttack(0, CardRarity.COMMON);

    public ZankiKiguchi()
    {
        super(DATA);

        Initialize(2, 0, 2);
        SetUpgrade(3, 0, 0);
        SetScaling(0, 1, 1);

        SetSynergy(Synergies.Katanagatari);
        SetAffinity(1, 2, 0, 0, 0);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.MoveCard(this, player.hand)
        .ShowEffect(true, true);
        SetPurge(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);

        if (!player.stance.ID.equals(NeutralStance.STANCE_ID) && CombatStats.TryActivateSemiLimited(cardID))
        {
            player.stance.onEnterStance();
        }
    }
}