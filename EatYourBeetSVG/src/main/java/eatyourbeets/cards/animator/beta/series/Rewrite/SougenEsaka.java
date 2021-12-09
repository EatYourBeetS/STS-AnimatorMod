package eatyourbeets.cards.animator.beta.series.Rewrite;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.stances.EnduranceStance;
import eatyourbeets.stances.MightStance;
import eatyourbeets.utilities.CardSelection;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class SougenEsaka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(SougenEsaka.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.ALL).SetSeriesFromClassPackage();

    public SougenEsaka()
    {
        super(DATA);

        Initialize(2, 4, 1, 2);
        SetUpgrade(2, 1, 1);
        SetAffinity_Orange(1, 0, 2);
        SetAffinity_Red(0, 0, 1);

        SetAffinityRequirement(Affinity.Orange, 5);
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamageToAll(this, AttackEffects.BLUNT_LIGHT);
        GameActions.Bottom.GainBlock(block);

        if (GameUtilities.InStance(MightStance.STANCE_ID)) {
            GameActions.Bottom.GainEndurance(secondaryValue);
        }


        if (CheckAffinity(Affinity.Orange))
        {
            GameActions.Bottom.MoveCards(p.drawPile, p.discardPile, 1)
                    .ShowEffect(true, true)
                    .SetOrigin(CardSelection.Top).AddCallback(() -> GameActions.Bottom.ChangeStance(EnduranceStance.STANCE_ID));

        }
    }
}