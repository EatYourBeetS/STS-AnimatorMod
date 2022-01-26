package pinacolada.cards.pcl.series.Fate;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.*;
import pinacolada.cards.base.cardeffects.GenericEffects.GenericEffect_ChannelOrb;
import pinacolada.orbs.PCLOrbHelper;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

import java.util.Arrays;

public class TokiomiTohsaka extends PCLCard
{
    public static final PCLCardData DATA = Register(TokiomiTohsaka.class)
            .SetAttack(1, CardRarity.UNCOMMON, PCLAttackType.Fire, PCLCardTarget.AoE)
            .SetSeriesFromClassPackage(true);
    private static final CardEffectChoice choices = new CardEffectChoice();

    public TokiomiTohsaka()
    {
        super(DATA);

        Initialize(3, 5, 3, 0);
        SetUpgrade(0, 3, 0, 0);

        SetAffinity_Blue(1, 0, 2);
        SetAffinity_Orange(1, 0, 0);

        SetExhaust(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToAll(this, AbstractGameAction.AttackEffect.FIRE)
                        .forEach(d -> d.SetVFXColor(Color.ORANGE, Color.ORANGE));
        PCLActions.Bottom.GainBlock(block);

        if (choices.TryInitialize(this))
        {
            for (PCLOrbHelper orb : PCLOrbHelper.ALL.values()) {
                if (PCLGameUtilities.IsCommonOrb(orb.ID)) {
                    choices.AddEffect(new GenericEffect_ChannelOrb(orb, 1));
                }
            }
        }
        choices.Select(PCLActions.Bottom, 1, null)
                .CancellableFromPlayer(true);
        if (CheckPrimaryCondition(true)) {
            choices.Select(PCLActions.Bottom, 1, null)
                    .CancellableFromPlayer(true);
        }
        else {
            PCLActions.Bottom.AddAffinity(PCLAffinity.Light, magicNumber);
        }
    }

    @Override
    public boolean CheckPrimaryCondition(boolean tryUse){
        PCLAffinity highestAffinity = PCLJUtils.FindMax(Arrays.asList(PCLAffinity.Extended()), this::GetHandAffinity);
        return (highestAffinity.equals(PCLAffinity.Light));
    }
}