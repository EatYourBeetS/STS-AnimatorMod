package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.cards.base.attributes.TempHPAttribute;
import eatyourbeets.effects.VFX;
import eatyourbeets.monsters.EnemyIntent;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Shalltear extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shalltear.class)
            .SetAttack(2, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetSeries(CardSeries.Overlord);

    public Shalltear()
    {
        super(DATA);

        Initialize(3, 0, 3, 1);
        SetUpgrade(0, 0, 0);

        SetEthereal(true);

        SetAffinity_Blue(1, 1, 1);
        SetAffinity_Green(1, 0, 0);
        SetAffinity_Dark(2, 0, 2);
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
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainTemporaryHP(magicNumber);
        GameActions.Bottom.DealDamageToAll(this, AbstractGameAction.AttackEffect.NONE)
        .SetDamageEffect((enemy, __) ->
        {
            GameEffects.List.Add(VFX.Hemokinesis(player.hb, enemy.hb));
            GameActions.Bottom.ApplyWeak(player, enemy, 1);

            if (isSynergizing)
            {
                GameActions.Bottom.ReduceStrength(enemy, secondaryValue, true).SetForceGain(true);
            }
        });
    }
}