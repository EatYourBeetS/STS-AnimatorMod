package eatyourbeets.cards.animator.beta.series.RozenMaiden;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.JUtils;
import eatyourbeets.utilities.TargetHelper;

public class Barasuishou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Barasuishou.class)
    		.SetAttack(1, CardRarity.UNCOMMON, EYBAttackType.Elemental, EYBCardTarget.ALL).SetSeriesFromClassPackage();

    public Barasuishou()
    {
        super(DATA);

        Initialize(8, 0, 3, 1);
        SetUpgrade(3, 0, 1);
        SetAffinity_Blue(1, 0, 2);
        SetAffinity_Dark(1, 0, 2);

        SetEthereal(true);
    }

    @Override
    protected float ModifyDamage(AbstractMonster enemy, float amount)
    {
        return super.ModifyDamage(enemy, amount + magicNumber *
                (JUtils.Count(player.drawPile.group, c -> c.type == CardType.CURSE) +
                JUtils.Count(player.discardPile.group, c -> c.type == CardType.CURSE) +
                JUtils.Count(player.hand.group, c -> c.type == CardType.CURSE)));
    }


    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamageToAll(this, AttackEffects.DARK);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActions.Bottom.ApplyBlinded(TargetHelper.Enemies(), secondaryValue);
    }


    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        super.triggerOnOtherCardPlayed(c);

        if (player.hand.contains(this)) {
            GameActions.Last.Exhaust(this);
        }
    }
}


