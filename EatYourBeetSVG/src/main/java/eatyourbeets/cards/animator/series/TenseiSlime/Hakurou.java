package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Hakurou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Hakurou.class)
            .SetAttack(2, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.Normal, true)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(AffinityToken.GetCard(Affinity.Green), true));

    public Hakurou()
    {
        super(DATA);

        Initialize(1, 1, 3);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(2, 0, 2);

        SetAffinityRequirement(Affinity.Red, 4);
        SetAffinityRequirement(Affinity.Orange, 4);

        SetHitCount(3,1);
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

        GameUtilities.MaintainPower(Affinity.Green);
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.VFX(new DieDieDieEffect());
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE);
        GameActions.Bottom.GainBlock(block);

        GameActions.Bottom.ChangeStance(AgilityStance.STANCE_ID)
        .RequireNeutralStance(true)
        .AddCallback(stance ->
        {
            if (stance != null)
            {
                GameActions.Bottom.Flash(this);
            }
        });

        GameActions.Bottom.TryChooseSpendAffinity(this, Affinity.Red,Affinity.Orange).AddConditionalCallback(() -> {
            GameActions.Bottom.MakeCardInHand(AffinityToken.GetCopy(Affinity.Green, upgraded));
        });
    }
}