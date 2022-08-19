package eatyourbeets.cards.animatorClassic.series.TenseiSlime;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.utilities.GameActions;

public class Hakurou extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Hakurou.class).SetAttack(2, CardRarity.COMMON);

    public Hakurou()
    {
        super(DATA);

        Initialize(1, 0, 3, 4);
        SetUpgrade(0, 0, 1, 0);
        SetScaling(0, 1, 1);

        SetSeries(CardSeries.TenseiSlime);
        SetMartialArtist();
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (ForceStance.IsActive())
        {
            GameActions.Bottom.GainBlock(secondaryValue)
            .SetVFX(true, true);
        }

        GameActions.Bottom.VFX(new DieDieDieEffect());

        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);
        }

        if (info.IsSynergizing)
        {
            GameActions.Bottom.GainAgility(1);
        }
    }
}