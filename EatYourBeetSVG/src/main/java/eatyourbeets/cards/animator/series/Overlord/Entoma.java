package eatyourbeets.cards.animator.series.Overlord;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.VFX;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameEffects;
import eatyourbeets.utilities.GameUtilities;

public class Entoma extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Entoma.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Entoma()
    {
        super(DATA);

        Initialize(3, 0);
        SetUpgrade(1, 0);

        SetAffinity_Dark(1, 1, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.DealCardDamage(this, m, AttackEffects.POISON).forEach(d -> d
        .SetDamageEffect(e -> GameEffects.List.Add(VFX.Bite(e.hb, Color.GREEN)).duration)
        .AddCallback(e -> GameActions.Top.ApplyPoison(player, e, damage).ShowEffect(false, true)));

        if (info.TryActivateSemiLimited())
        {
            GameActions.Bottom.StackPower(new EntomaPower(p, 2));
        }
    }

    public static class EntomaPower extends AnimatorPower
    {
        public EntomaPower(AbstractCreature owner, int amount)
        {
            super(owner, Entoma.DATA);

            Initialize(amount, PowerType.BUFF, true);
        }

        @Override
        public void onApplyPower(AbstractPower power, AbstractCreature target, AbstractCreature source)
        {
            super.onApplyPower(power, target, source);

            if (source == owner && PoisonPower.POWER_ID.equals(power.ID) && GameUtilities.CanApplyPower(source, target, power, null))
            {
                GameActions.Bottom.GainTemporaryHP(1);
            }
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            ReducePower(1);
        }
    }
}