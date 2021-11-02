package eatyourbeets.cards.animator.series.GoblinSlayer;

import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.orbs.animator.Earth;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class DwarfShaman extends AnimatorCard
{
    public static final EYBCardData DATA = Register(DwarfShaman.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental)
            .SetSeriesFromClassPackage();

    public DwarfShaman()
    {
        super(DATA);

        Initialize(6, 0, 3);
        SetUpgrade(4, 0, 0);

        SetAffinity_Earth();

        SetEvokeOrbCount(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE).SetVFX(true, false)
        .SetDamageEffect(c -> GameEffects.List.Add(VFX.ThrowRock(player.hb, c.hb, 0.4f)).duration).SetRealtime(true);
        GameActions.Bottom.ChannelOrb(new Earth());
        GameActions.Bottom.MakeCardInDrawPile(new Dazed())
        .SetOriginalOrder(GameActions.ActionOrder.Top);
    }
}