package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class HidekiHinata extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HidekiHinata.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Ranged).SetSeriesFromClassPackage();

    public HidekiHinata()
    {
        super(DATA);

        Initialize(6, 0, 0, 0);
        SetUpgrade(2, 0, 0, 0);

        SetAffinity_Green(1, 1, 0);
        SetExhaust(true);
        AfterLifeMod.Add(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.FIRE);

        if (GameUtilities.IsAttacking(m.intent))
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.FIRE);
        }
    }
}