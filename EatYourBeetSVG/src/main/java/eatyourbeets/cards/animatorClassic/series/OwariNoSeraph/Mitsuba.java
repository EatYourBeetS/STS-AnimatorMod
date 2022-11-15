package eatyourbeets.cards.animatorClassic.series.OwariNoSeraph;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;

public class Mitsuba extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(Mitsuba.class).SetSeriesFromClassPackage().SetAttack(1, CardRarity.COMMON);

    public Mitsuba()
    {
        super(DATA);

        Initialize(7, 2, 2, 6);
        SetUpgrade(3, 0, 0, 0);

        
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
            return super.ModifyBlock(enemy, amount + secondaryValue);
        }

        return super.ModifyBlock(enemy, amount);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.SLASH_HEAVY);
    }
}