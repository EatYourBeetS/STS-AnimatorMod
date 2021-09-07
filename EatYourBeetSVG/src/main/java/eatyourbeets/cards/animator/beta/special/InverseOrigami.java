package eatyourbeets.cards.animator.beta.special;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.RainbowCardEffect;
import eatyourbeets.cards.animator.special.OrbCore;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class InverseOrigami extends AnimatorCard
{
    public static final EYBCardData DATA = Register(InverseOrigami.class).SetSkill(2, CardRarity.SPECIAL, EYBCardTarget.None).SetSeries(CardSeries.DateALive);

    public InverseOrigami()
    {
        super(DATA);

        Initialize(0, 0);
        SetAffinity_Blue(1, 1, 0);
        SetAffinity_Dark(1, 0, 0);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        GameActions.Bottom.SpendEnergy(1, false)
        .AddCallback(() ->
        {
            GameActions.Bottom.MakeCardInHand(JUtils.Random(OrbCore.GetAllCores()).makeCopy());
            GameActions.Bottom.Flash(this);
        });
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        SupportDamagePower supportDamage = GameUtilities.GetPower(p, SupportDamagePower.class);
        if (supportDamage != null && supportDamage.amount > 0)
        {
            supportDamage.atEndOfTurn(true);
        }

        GameActions.Bottom.VFX(new RainbowCardEffect());
        GameActions.Bottom.TriggerOrbPassive(p.maxOrbs);
    }
}