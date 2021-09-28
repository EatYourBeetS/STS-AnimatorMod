package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;

public class SougenEsaka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SougenEsaka.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.ALL).SetSeriesFromClassPackage();

    public SougenEsaka()
    {
        super(DATA);

        Initialize(2, 3, 1);
        SetUpgrade(2, 1, 1);
        SetAffinity_Red(1, 0, 2);

        SetAffinityRequirement(Affinity.Red, 3);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.BLUNT_LIGHT);
        GameActions.Bottom.GainBlock(block);


        if (CheckAffinity(Affinity.Red))
        {
            GameActions.Bottom.MoveCards(p.drawPile, p.discardPile, 1)
                    .ShowEffect(true, true)
                    .SetOrigin(CardSelection.Top).AddCallback(() -> {
                        GameActions.Bottom.ChangeStance(ForceStance.STANCE_ID);
                        GameActions.Bottom.GainAgility(magicNumber);
                    });

        }
    }
}