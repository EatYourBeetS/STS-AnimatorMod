package eatyourbeets.cards.animator.series.TenseiSlime;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.vfx.FireballEffect;
import eatyourbeets.orbs.animator.Fire;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.utilities.GameActions;

public class Benimaru extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Benimaru.class).SetAttack(1, CardRarity.COMMON, EYBAttackType.Elemental, EYBCardTarget.Normal);

    public Benimaru()
    {
        super(DATA);

        Initialize(3, 0, 2);
        SetUpgrade(4, 0, 0);

        SetSynergy(Synergies.TenSura);
        SetAffinity(1, 0, 2, 0, 0);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        if (CombatStats.TryActivateSemiLimited(cardID))
        {
            GameActions.Bottom.ChannelOrb(new Fire());
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.VFX(new FireballEffect(player.hb.cX, player.hb.cY, m.hb.cX, m.hb.cY)
        .SetColor(Color.RED, Color.ORANGE).SetRealtime(true), true);

        GameActions.Bottom.DealDamage(this, m, AbstractGameAction.AttackEffect.NONE)
        .SetDamageEffect(e -> GameActions.Top.StackPower(player, new BurningPower(e, player, magicNumber)));
    }
}