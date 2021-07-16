package eatyourbeets.cards.animator.series.TenseiSlime;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.effects.VFX;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Benimaru extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Benimaru.class)
            .SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental, EYBCardTarget.Normal)
            .SetSeriesFromClassPackage();

    public Benimaru()
    {
        super(DATA);

        Initialize(3, 0, 2);
        SetUpgrade(4, 0, 0);

        SetAffinity_Red(1);
        SetAffinity_Blue(1);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.Flash(this);
            GameActions.Bottom.ChannelOrb(new Fire());
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE)
        .SetDamageEffect(e -> GameEffects.List.Add(VFX.Fireball(player.hb, e.hb)).SetColor(Color.RED, Color.ORANGE).SetRealtime(true).duration)
        .AddCallback(m, (enemy, __) -> GameActions.Top.ApplyBurning(player, enemy, magicNumber))
        .SetRealtime(true);
    }
}