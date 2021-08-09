package eatyourbeets.cards.animator.beta.series.DateALive;

import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class KotoriItsuka extends AnimatorCard
{
    public static final EYBCardData DATA = Register(KotoriItsuka.class).SetAttack(1, CardRarity.RARE, EYBAttackType.Normal).SetMaxCopies(2).SetSeriesFromClassPackage();

    public KotoriItsuka()
    {
        super(DATA);

        Initialize(6, 0, 5, 5);
        SetUpgrade(0, 0, 0);
        SetAffinity_Red(2, 0, 1);
        SetAffinity_Orange(1, 0, 0);
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));

        for (int i=0; i<magicNumber; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.FIRE);
        }
    }

    @Override
    public boolean cardPlayable(AbstractMonster m)
    {
        return player.discardPile.size() > player.drawPile.size();
    }

    @Override
    public void OnLateUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        for (int i=0; i<secondaryValue; i++)
        {
            GameActions.Bottom.MakeCardInDrawPile(new Burn())
            .SetDuration(Settings.ACTION_DUR_XFAST, true);
        }
    }
}