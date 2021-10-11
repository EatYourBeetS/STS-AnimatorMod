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
    public static final EYBCardData DATA = Register(SougenEsaka.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.ALL, true, true).SetSeriesFromClassPackage();

    public SougenEsaka()
    {
        super(DATA);

        Initialize(2, 3, 1);
        SetUpgrade(2, 1, 1);
        SetAffinity_Earth(2, 0, 2);

        SetAffinityRequirement(Affinity.Fire, 2);
        SetAffinityRequirement(Affinity.Earth, 3);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToAll(this, AttackEffects.BLUNT_LIGHT);
        GameActions.Bottom.GainBlock(block);

        if (CheckAffinity(Affinity.Fire) || GameUtilities.InStance(ForceStance.STANCE_ID)) {
            GameUtilities.MaintainPower(Affinity.Earth);
        }


        if (CheckAffinity(Affinity.Earth))
        {
            GameActions.Bottom.MoveCards(p.drawPile, p.discardPile, 1)
                    .ShowEffect(true, true)
                    .SetOrigin(CardSelection.Top).AddCallback(() -> {
                        GameActions.Bottom.ChangeStance(WillpowerStance.STANCE_ID);
                        GameActions.Bottom.RaiseEarthLevel(magicNumber);
                    });

        }
    }
}