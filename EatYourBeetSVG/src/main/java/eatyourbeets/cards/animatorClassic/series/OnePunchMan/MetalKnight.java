package eatyourbeets.cards.animatorClassic.series.OnePunchMan;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.vfx.combat.WeightyImpactEffect;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardSeries;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class MetalKnight extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(MetalKnight.class).SetAttack(3, CardRarity.UNCOMMON);

    public MetalKnight()
    {
        super(DATA);

        Initialize(11, 0, 3);
        SetUpgrade(2, 0, 0);

        SetEvokeOrbCount(1);
        SetSeries(CardSeries.OnePunchMan);
    }

    @Override
    protected void OnUpgrade()
    {
        SetInnate(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.VFX(new WeightyImpactEffect(m.hb.cX, m.hb.cY));
        GameActions.Bottom.Wait(0.8f);
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE);
        GameActions.Bottom.ChannelOrb(new Plasma());

        if (magicNumber > 0)
        {
            GameActions.Bottom.GainMetallicize(magicNumber);
            GameActions.Bottom.ModifyAllInstances(uuid, c -> GameUtilities.DecreaseMagicNumber(c, 1, false));
        }
    }
}