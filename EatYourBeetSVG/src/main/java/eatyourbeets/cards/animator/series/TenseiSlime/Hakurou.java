package eatyourbeets.cards.animator.series.TenseiSlime;

import eatyourbeets.cards.animator.tokens.AffinityToken;
import eatyourbeets.cards.base.Affinity;
import eatyourbeets.effects.AttackEffects;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;

public class Hakurou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Hakurou.class)
            .SetAttack(2, CardRarity.COMMON)
            .SetSeriesFromClassPackage();
    static
    {
        DATA.AddPreview(AffinityToken.GetCard(Affinity.Green), true);
    }

    public Hakurou()
    {
        super(DATA);

        Initialize(1, 0, 3);
        SetUpgrade(0, 0, 1);

        SetAffinity_Red(1, 0, 0);
        SetAffinity_Green(2, 0, 2);

        SetAffinityRequirement(Affinity.Red, 4);
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

        GameActions.Bottom.GainAgility(1);
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        if (ForceStance.IsActive() || CheckAffinity(Affinity.Red))
        {
            GameActions.Bottom.MakeCardInHand(AffinityToken.GetCopy(Affinity.Green, upgraded));
        }

        GameActions.Bottom.VFX(new DieDieDieEffect());
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE);
        }
    }
}