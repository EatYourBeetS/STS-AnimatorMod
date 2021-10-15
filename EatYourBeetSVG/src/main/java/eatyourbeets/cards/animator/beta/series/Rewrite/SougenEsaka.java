package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.stances.WillpowerStance;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SougenEsaka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SougenEsaka.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.ALL, true).SetSeriesFromClassPackage();

    public SougenEsaka()
    {
        super(DATA);

        Initialize(2, 3, 1);
        SetUpgrade(2, 1, 1);
        SetAffinity_Orange(2, 0, 2);
        SetAffinity_Red(0, 0, 1);

        SetAffinityRequirement(Affinity.Orange, 3);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.BLUNT_LIGHT);
        GameActions.Bottom.GainBlock(block);

        if (GameUtilities.InStance(ForceStance.STANCE_ID)) {
            GameActions.Bottom.GainWillpower(1, true);
        }


        if (TrySpendAffinity(Affinity.Orange))
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