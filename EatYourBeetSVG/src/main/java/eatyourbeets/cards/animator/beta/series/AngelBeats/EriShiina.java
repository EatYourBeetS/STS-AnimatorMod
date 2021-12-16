package eatyourbeets.cards.animator.beta.series.AngelBeats;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.animator.special.ThrowingKnife;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnAfterlifeSubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.VelocityStance;
import eatyourbeets.utilities.GameActions;

public class EriShiina extends AnimatorCard implements OnAfterlifeSubscriber
{
    public static final EYBCardData DATA = Register(EriShiina.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Normal).SetSeriesFromClassPackage().PostInitialize(data ->
    {
        for (ThrowingKnife knife : ThrowingKnife.GetAllCards())
        {
            data.AddPreview(knife, true);
        }
    });

    public EriShiina()
    {
        super(DATA);

        Initialize(6, 1, 2, 1);
        SetUpgrade(2, 0, 0, 0);

        SetAffinity_Green(1, 0, 2);

        SetAffinityRequirement(Affinity.Blue, 4);

        SetExhaust(true);
        SetAfterlife(true);
        SetHitCount(2);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);
        CombatStats.onAfterlife.Subscribe(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_LIGHT);
        GameActions.Bottom.GainBlock(block);

        if (VelocityStance.IsActive())
        {
            GameActions.Bottom.CreateThrowingKnives(magicNumber);
        }

        if (TrySpendAffinity(Affinity.Blue))
        {
            GameActions.Bottom.GainBlur(1);
        }
    }

    @Override
    public void OnAfterlife(AbstractCard playedCard, AbstractCard fuelCard) {
        if (playedCard == this) {
            GameActions.Bottom.CreateThrowingKnives(magicNumber);
        }
    }
}