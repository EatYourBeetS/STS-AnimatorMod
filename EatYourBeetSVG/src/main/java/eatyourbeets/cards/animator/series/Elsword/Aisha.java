package eatyourbeets.cards.animator.series.Elsword;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.*;
import eatyourbeets.cards.base.attributes.AbstractAttribute;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;

public class Aisha extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Aisha.class)
            .SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Elemental)
            .SetMaxCopies(2)
            .SetSeries(CardSeries.Elsword);

    public Aisha()
    {
        super(DATA);

        Initialize(1, 0, 2, 2);
        SetUpgrade(2, 0, 0, 0);

        SetAffinity_Blue(1, 0, 1);
        SetAffinity_Dark(1, 0, 1);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return super.GetDamageInfo().AddMultiplier(magicNumber);
    }

    @Override
    protected float GetInitialDamage()
    {
        return super.GetInitialDamage() + (player.filledOrbCount() * secondaryValue);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        for (int i = 0; i < magicNumber; i++)
        {
            GameActions.Bottom.DealDamage(this, m, AttackEffects.FIRE).SetVFX(true, false)
            .SetDamageEffect(enemy ->
            {
                GameEffects.List.Add(VFX.SmallLaser(player.hb, enemy.hb, Color.PURPLE));
                return GameEffects.List.Add(VFX.SmallLaser(player.hb, enemy.hb, Color.VIOLET)).duration * 0.1f;
            });
        }

        GameActions.Bottom.TriggerOrbPassive(1)
        .SetFilter(o -> Dark.ORB_ID.equals(o.ID))
        .AddCallback(orbs ->
        {
            if (orbs.size() > 0)
            {
                GameActions.Bottom.GainIntellect(1, true);
                GameActions.Bottom.GainCorruption(1, true);
            }
        });
    }
}