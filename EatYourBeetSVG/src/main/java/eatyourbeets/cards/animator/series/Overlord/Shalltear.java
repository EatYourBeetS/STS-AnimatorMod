package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.effects.vfx.HemokinesisEffect;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Shalltear extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shalltear.class).SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL);

    public Shalltear()
    {
        super(DATA);

        Initialize(3, 0, 3, 1);
        SetUpgrade(0, 0, 0);
        SetScaling(1, 1, 1);

        SetEthereal(true);
        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void OnDrag(AbstractMonster m)
    {
        boolean stealStrength = HasSynergy();
        for (EnemyIntent intent : GameUtilities.GetIntents())
        {
            intent.AddWeak();
            if (stealStrength)
            {
                intent.AddStrength(-secondaryValue);
            }
        }
    }

    @Override
    protected void OnUpgrade()
    {
        SetEthereal(false);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.NONE)
        .SetDamageEffect((enemy, aBoolean) ->
        {
            GameEffects.List.Add(new HemokinesisEffect(enemy.hb.cX, enemy.hb.cY, player.hb.cX, player.hb.cY));
            GameActions.Bottom.ApplyWeak(player, enemy, 1);

            if (HasSynergy())
            {
                GameActions.Bottom.ReduceStrength(enemy, secondaryValue, true).SetForceGain(true);
            }
        });
    }
}