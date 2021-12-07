package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.stances.MightStance;
import eatyourbeets.utilities.GameActions;

public class Fujimaki extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Fujimaki.class).SetAttack(1, CardRarity.COMMON).SetSeriesFromClassPackage();

    public Fujimaki()
    {
        super(DATA);

        Initialize(7, 0, 1);
        SetUpgrade(3, 0, 1);

        SetCooldown(1, 0, this::OnCooldownCompleted);
        SetAffinity_Red(1, 1, 2);

        SetAffinityRequirement(Affinity.Green, 4);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);

        if (TrySpendAffinity(Affinity.Green))
        {
            GameActions.Bottom.ChangeStance(MightStance.STANCE_ID);
        }

        cooldown.ProgressCooldownAndTrigger(m);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        GameActions.Bottom.GainMight(magicNumber);
        GameActions.Bottom.MakeCardInHand(new Wound());
    }
}