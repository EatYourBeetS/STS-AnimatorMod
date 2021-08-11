package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.WillpowerStance;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class SougenEsaka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SougenEsaka.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.ALL).SetSeriesFromClassPackage();

    public SougenEsaka()
    {
        super(DATA);

        Initialize(2, 3, 1);
        SetUpgrade(1, 1, 1);
        SetAffinity_Orange(1, 1, 1);

        SetAffinityRequirement(Affinity.Orange, 3);
    }

    @Override
    protected float ModifyBlock(AbstractMonster enemy, float amount)
    {
        return super.ModifyBlock(enemy, amount + MathUtils.ceil(CombatStats.Affinities.GetPowerAmount(Affinity.Orange) * 0.5f));
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.BLUNT_LIGHT);
        GameActions.Bottom.GainBlock(block);

        if (CheckAffinity(Affinity.Orange))
        {
            GameActions.Bottom.MoveCards(p.drawPile, p.discardPile, 1)
                    .ShowEffect(true, true)
                    .SetOrigin(CardSelection.Top).AddCallback(() -> {
                        GameActions.Bottom.ChangeStance(WillpowerStance.STANCE_ID);
                        GameActions.Bottom.GainWillpower(magicNumber);
                    });

        }
    }
}