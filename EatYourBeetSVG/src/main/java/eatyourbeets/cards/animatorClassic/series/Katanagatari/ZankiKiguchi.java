package eatyourbeets.cards.animatorClassic.series.Katanagatari;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class ZankiKiguchi extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(ZankiKiguchi.class).SetSeriesFromClassPackage().SetAttack(0, CardRarity.COMMON);

    public ZankiKiguchi()
    {
        super(DATA);

        Initialize(2, 0, 2);
        SetUpgrade(3, 0, 0);
        SetScaling(0, 1, 1);

        
        SetMartialArtist();
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.BLUNT_HEAVY);

        if (!player.stance.ID.equals(NeutralStance.STANCE_ID) && CombatStats.TryActivateSemiLimited(cardID))
        {
            player.stance.onEnterStance();
        }
    }
}