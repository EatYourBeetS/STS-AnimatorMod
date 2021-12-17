package pinacolada.cards.pcl.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.pcl.tokens.AffinityToken;
import pinacolada.effects.AttackEffects;
import pinacolada.stances.VelocityStance;
import pinacolada.utilities.PCLActions;

public class Hakurou extends PCLCard
{
    public static final PCLCardData DATA = Register(Hakurou.class)
            .SetAttack(2, CardRarity.COMMON, PCLAttackType.Normal, eatyourbeets.cards.base.EYBCardTarget.Normal)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(AffinityToken.GetCard(PCLAffinity.Green), true));

    public Hakurou()
    {
        super(DATA);

        Initialize(1, 1, 3, 2);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(1, 0, 2);

        SetAffinityRequirement(PCLAffinity.Red, 4);
        SetAffinityRequirement(PCLAffinity.Orange, 4);

        SetHitCount(3,1);
    }

    @Override
    public AbstractAttribute GetBlockInfo()
    {
        return super.GetBlockInfo().AddMultiplier(hitCount);
    }

    @Override
    protected void OnUpgrade()
    {
        upgradedDamage = true;
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (CombatStats.TryActivateSemiLimited(cardID)) {
            PCLActions.Bottom.GainBlock(secondaryValue);
            PCLActions.Bottom.Flash(this);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.VFX(new DieDieDieEffect());
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.NONE);
        for (int i = 0; i < hitCount; i++) {
            PCLActions.Bottom.GainBlock(block);
        }

        PCLActions.Bottom.TryChooseSpendAffinity(this, PCLAffinity.Red, PCLAffinity.Orange).AddConditionalCallback(() -> {
            if (VelocityStance.IsActive()) {
                PCLActions.Bottom.MakeCardInHand(AffinityToken.GetCopy(PCLAffinity.Green, upgraded));
            }
            else {
                PCLActions.Bottom.ChangeStance(VelocityStance.STANCE_ID);
            }
        });
    }
}