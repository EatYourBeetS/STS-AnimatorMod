package eatyourbeets.cards.animator.colorless.rare;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Hibiki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Hibiki.class).SetAttack(1, CardRarity.RARE, EYBAttackType.Ranged, EYBCardTarget.Random).SetColor(CardColor.COLORLESS);

    public Hibiki()
    {
        super(DATA);

        Initialize(2, 0, 3, 1);
        SetUpgrade(0, 0, 0, 1);

        SetSeries(CardSeries.Kancolle);

        SetAffinity_Green(1);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamageToRandomEnemy(this, AbstractGameAction.AttackEffect.BLUNT_LIGHT)
            .SetOptions(true, false);
        }

        GameActions.Bottom.ModifyAllInstances(uuid, c -> GameUtilities.IncreaseMagicNumber(c, secondaryValue, false));
    }
}