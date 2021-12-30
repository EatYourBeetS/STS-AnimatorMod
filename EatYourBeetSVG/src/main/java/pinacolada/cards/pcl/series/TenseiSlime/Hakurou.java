package pinacolada.cards.pcl.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.powers.CombatStats;
import pinacolada.cards.base.*;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.cardeffects.GenericEffects.GenericEffect_EnterStance;
import pinacolada.cards.base.cardeffects.GenericEffects.GenericEffect_GainAffinity;
import pinacolada.cards.pcl.tokens.AffinityToken;
import pinacolada.effects.AttackEffects;
import pinacolada.stances.VelocityStance;
import pinacolada.utilities.PCLActions;

public class Hakurou extends PCLCard
{
    public static final PCLCardData DATA = Register(Hakurou.class)
            .SetAttack(2, CardRarity.COMMON, PCLAttackType.Piercing, eatyourbeets.cards.base.EYBCardTarget.Normal)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(AffinityToken.GetCard(PCLAffinity.Green), true));
    private static final CardEffectChoice choices = new CardEffectChoice();

    public Hakurou()
    {
        super(DATA);

        Initialize(1, 1, 4, 2);
        SetUpgrade(1, 0, 1, 1);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(1, 0, 2);

        SetAffinityRequirement(PCLAffinity.Red, 4);
        SetAffinityRequirement(PCLAffinity.Orange, 4);

        SetHitCount(3,0);
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
            if (choices.TryInitialize(this))
            {
                choices.AddEffect(new GenericEffect_EnterStance(VelocityStance.STANCE_ID));
                choices.AddEffect(new GenericEffect_GainAffinity(PCLAffinity.Green, magicNumber));
            }
            choices.Select(1, m);
        });
    }
}