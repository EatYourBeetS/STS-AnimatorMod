package eatyourbeets.cards.animator.series.GATE;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.stances.CalmStance;
import com.megacrit.cardcrawl.stances.NeutralStance;
import com.megacrit.cardcrawl.stances.WrathStance;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.stances.GuardStance;
import eatyourbeets.stances.OrbStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Kuribayashi extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Kuribayashi.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Ranged)
            .SetSeriesFromClassPackage();

    public Kuribayashi()
    {
        super(DATA);

        Initialize(13, 0, 7, 9);
        SetUpgrade(6, 0, 0);

        SetAffinity_Earth(2);
        SetAffinity_Steel();
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        super.OnDrag(m);

        if (m != null && GameUtilities.InStance(WrathStance.STANCE_ID))
        {
            GameUtilities.GetIntent(m).AddStrength(-magicNumber);
        }
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return GameUtilities.InStance(GuardStance.STANCE_ID) ? super.GetDamageInfo().AddMultiplier(3) : super.GetDamageInfo();
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT).SetSoundPitch(0.6f, 0.8f);

        if (GameUtilities.InStance(WrathStance.STANCE_ID))
        {
            GameActions.Bottom.ReduceStrength(m, magicNumber, true);
        }
        else if (GameUtilities.InStance(GuardStance.STANCE_ID))
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT);
            GameActions.Bottom.DealDamage(this, m, AttackEffects.GUNSHOT);
        }
        else if (GameUtilities.InStance(CalmStance.STANCE_ID))
        {
            GameActions.Bottom.ChangeStance(NeutralStance.STANCE_ID);
            GameActions.Bottom.GainSupportDamage(secondaryValue);
        }
        else if (GameUtilities.InStance(OrbStance.STANCE_ID))
        {
            GameActions.Bottom.ChannelOrb(new Plasma());
        }
    }
}