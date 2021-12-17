package pinacolada.cards.pcl.series.GATE;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.powers.PowerHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class MariKurokawa extends PCLCard
{
    public static final PCLCardData DATA = Register(MariKurokawa.class)
            .SetAttack(1, CardRarity.COMMON, PCLAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public MariKurokawa()
    {
        super(DATA);

        Initialize(5, 0, 2, 3);
        SetUpgrade(3, 0, 0);

        SetAffinity_Light(1, 0, 1);
        SetAffinity_Green(1, 0, 1);
        SetAffinity_Orange(1, 0, 1);

        SetAffinityRequirement(PCLAffinity.Red, 2);
        SetAffinityRequirement(PCLAffinity.Green, 2);

        SetDrawPileCardPreview(c -> c.type == CardType.ATTACK && PCLGameUtilities.HasGreenAffinity(c));
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        PCLActions.Bottom.StackPower(TargetHelper.RandomEnemy(), PowerHelper.LockOn, 1).IgnoreArtifact(true);
        PCLActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.GUNSHOT).forEach(d -> d.SetSoundPitch(0.9f, 1f));
        PCLActions.Bottom.Draw(1)
        .SetFilter(c -> c.type == CardType.ATTACK && PCLGameUtilities.HasGreenAffinity(c), false)
        .AddCallback(cards ->
        {
            for (AbstractCard c : cards)
            {
                PCLGameUtilities.Retain(c);
            }
        });
    }
}