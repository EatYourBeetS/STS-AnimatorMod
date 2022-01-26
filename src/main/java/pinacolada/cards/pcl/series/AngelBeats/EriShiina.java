package pinacolada.cards.pcl.series.AngelBeats;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.special.ThrowingKnife;
import pinacolada.effects.AttackEffects;
import pinacolada.interfaces.subscribers.OnAfterlifeSubscriber;
import pinacolada.powers.PCLCombatStats;
import pinacolada.stances.pcl.VelocityStance;
import pinacolada.utilities.PCLActions;

public class EriShiina extends PCLCard implements OnAfterlifeSubscriber
{
    public static final PCLCardData DATA = Register(EriShiina.class).SetAttack(2, CardRarity.UNCOMMON, PCLAttackType.Normal).SetSeriesFromClassPackage().PostInitialize(data ->
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

        SetAffinityRequirement(PCLAffinity.Blue, 4);

        SetExhaust(true);
        SetAfterlife(true);
        SetHitCount(2);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);
        PCLCombatStats.onAfterlife.Subscribe(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_LIGHT);
        PCLActions.Bottom.GainBlock(block);

        if (VelocityStance.IsActive())
        {
            PCLActions.Bottom.CreateThrowingKnives(magicNumber);
        }

        if (TrySpendAffinity(PCLAffinity.Blue))
        {
            PCLActions.Bottom.GainBlur(1);
        }
    }

    @Override
    public void OnAfterlife(AbstractCard playedCard, AbstractCard fuelCard) {
        if (playedCard == this) {
            PCLActions.Bottom.CreateThrowingKnives(magicNumber);
        }
    }
}