package eatyourbeets.cards.animator.series.TenseiSlime;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
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

        SetAffinityRequirement(Affinity.Red, 3);
        SetAffinityRequirement(Affinity.Orange, 3);
    }

    @Override
    public AbstractAttribute GetBlockInfo() {
        if (GameUtilities.InStance(NeutralStance.STANCE_ID)) {
            return null;
        }
        return super.GetBlockInfo().AddMultiplier(2);
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

        GameActions.Bottom.RetainPower(Affinity.Green);
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.VFX(new DieDieDieEffect());
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE);
        }

        GameActions.Bottom.ChangeStance(AgilityStance.STANCE_ID)
        .RequireNeutralStance(true)
        .AddCallback(stance ->
        {
            if (stance != null)
            {
                GameActions.Bottom.Flash(this);
            }
            else {
                for (int i = 0; i < magicNumber; i++)
                {
                    GameActions.Bottom.GainBlock(block);
                }
            }
        });

        if (CheckAffinity(Affinity.Red) || CheckAffinity(Affinity.Orange))
        {
            GameActions.Bottom.MakeCardInHand(AffinityToken.GetCopy(Affinity.Green, upgraded));
        }
    }
}