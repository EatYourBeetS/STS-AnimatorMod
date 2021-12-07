package eatyourbeets.cards.animator.series.OnePunchMan;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.ListSelection;

import java.util.Comparator;

public class Genos extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Genos.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public Genos()
    {
        super(DATA);

        Initialize(14, 0, 2, 2);
        SetUpgrade(4, 0, 0, 0);

        SetAffinity_Red(1);
        SetAffinity_Light(1, 0, 1);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (!player.hasPower(ArtifactPower.POWER_ID))
        {
            for (EnemyIntent intent : GameUtilities.GetIntents())
            {
                intent.AddPlayerBurning();
            }
        }
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.ReduceDebuffs(player, secondaryValue)
        .SetSelection(ListSelection.First(0), 1)
        .Sort(Comparator.comparingInt(a -> -a.amount));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.FIRE);
        GameActions.Bottom.ApplyBurning(p, p, magicNumber);
        GameActions.Bottom.ApplyBurning(p, m, magicNumber);
    }
}