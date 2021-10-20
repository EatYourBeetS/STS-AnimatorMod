package eatyourbeets.cards.animator.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.vfx.combat.ClawEffect;
import eatyourbeets.cards.base.*;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Lu extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Lu.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Piercing, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();

    public Lu()
    {
        super(DATA);

        Initialize(5, 0, 1);
        SetUpgrade(4,0);

        SetEvokeOrbCount(2);

        SetAffinity_Water(1);
        SetAffinity_Dark(1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.NONE)
        .SetDamageEffect((enemy, __) -> GameEffects.List.Add(new ClawEffect(enemy.hb.cX, enemy.hb.cY, Color.VIOLET, Color.WHITE)));

        GameActions.Bottom.ChannelOrb(new Frost());
        GameActions.Bottom.ChannelOrb(new Dark());
    }
}