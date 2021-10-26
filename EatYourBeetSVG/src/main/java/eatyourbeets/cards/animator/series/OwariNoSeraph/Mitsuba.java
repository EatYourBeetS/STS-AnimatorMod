package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class Mitsuba extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Mitsuba.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Mitsuba()
    {
        super(DATA);

        Initialize(6, 8);
        SetUpgrade(2, 2);

        SetAffinity_Fire(1);
        SetAffinity_Air(1);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.Draw(magicNumber);
    }

    @Override
    protected float ModifyBlock(AbstractMonster enemy, float amount)
    {
        if (enemy != null && enemy.currentHealth > player.currentHealth)
        {
            return super.ModifyBlock(enemy, amount);
        }

        return super.ModifyBlock(enemy, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);
    }
}