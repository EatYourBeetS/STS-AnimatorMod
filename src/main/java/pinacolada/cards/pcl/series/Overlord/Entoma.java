package pinacolada.cards.pcl.series.Overlord;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Entoma extends PCLCard
{
    public static final PCLCardData DATA = Register(Entoma.class)
            .SetAttack(1, CardRarity.COMMON)
            .SetSeriesFromClassPackage();

    public Entoma()
    {
        super(DATA);

        Initialize(3, 0);
        SetUpgrade(1, 0);

        SetAffinity_Dark(1, 0, 1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.POISON).forEach(d -> d
        .SetDamageEffect(e -> PCLGameEffects.List.Add(VFX.Bite(e.hb, Color.GREEN)).duration)
        .AddCallback(e -> PCLActions.Top.ApplyPoison(player, e, damage).ShowEffect(false, true)));

        if (info.TryActivateSemiLimited())
        {
            PCLActions.Bottom.StackPower(new EntomaPower(p, 2));
        }
    }

    public static class EntomaPower extends PCLPower
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

            if (source == owner && PoisonPower.POWER_ID.equals(power.ID) && PCLGameUtilities.CanApplyPower(source, target, power, null))
            {
                PCLActions.Bottom.GainTemporaryHP(1);
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