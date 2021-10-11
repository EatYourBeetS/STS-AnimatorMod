package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.stances.WillpowerStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Hakurou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Hakurou.class)
            .SetAttack(2, CardRarity.COMMON, EYBAttackType.Normal, EYBCardTarget.Normal, true, true)
            .SetSeriesFromClassPackage()
            .PostInitialize(data -> data.AddPreview(AffinityToken.GetCard(Affinity.Air), true));

    public Hakurou()
    {
        super(DATA);

        Initialize(1, 1, 3);
        SetUpgrade(0, 0, 1);

        SetAffinity_Fire(1, 0, 0);
        SetAffinity_Air(2, 0, 2);

        SetAffinityRequirement(Affinity.Fire, 3);
        SetAffinityRequirement(Affinity.Earth, 3);
    }

    @Override
    protected void OnUpgrade()
    {
        upgradedDamage = true;
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (ForceStance.IsActive() || WillpowerStance.IsActive()) {
            GameUtilities.MaintainPower(Affinity.Air);
            GameActions.Bottom.Flash(this);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.VFX(new DieDieDieEffect());
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE);
        }
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.GainBlock(block);
        }

        GameActions.Bottom.ChangeStance(AgilityStance.STANCE_ID)
        .RequireNeutralStance(true)
        .AddCallback(stance ->
        {
            if (stance != null)
            {
                GameActions.Bottom.Flash(this);
            }
        });

        if (CheckAffinity(Affinity.Fire) || CheckAffinity(Affinity.Earth))
        {
            GameActions.Bottom.MakeCardInHand(AffinityToken.GetCopy(Affinity.Air, upgraded));
        }
    }
}