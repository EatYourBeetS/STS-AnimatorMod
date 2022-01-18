package pinacolada.cards.pcl.series.Fate;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.cards.base.EYBCardTarget;
import pinacolada.cards.base.*;
import pinacolada.cards.base.cardeffects.GenericEffects.GenericEffect_ChannelOrb;
import pinacolada.orbs.pcl.Fire;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLJUtils;

import java.util.Arrays;

public class TokiomiTohsaka extends PCLCard
{
    public static final PCLCardData DATA = Register(TokiomiTohsaka.class)
            .SetAttack(1, CardRarity.UNCOMMON, PCLAttackType.Fire, EYBCardTarget.ALL)
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
            choices.AddEffect(new GenericEffect_ChannelOrb(new Fire()));
            choices.AddEffect(new GenericEffect_ChannelOrb(new Frost()));
            choices.AddEffect(new GenericEffect_ChannelOrb(new Lightning()));
            choices.AddEffect(new GenericEffect_ChannelOrb(new Dark()));
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