package pinacolada.cards.pcl.special;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderLongFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import pinacolada.cards.base.*;
import pinacolada.cards.pcl.series.Fate.Saber;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameEffects;

public class Saber_X extends PCLCard
{
    public static final PCLCardData DATA = Register(Saber_X.class)
            .SetAttack(1, CardRarity.SPECIAL, PCLAttackType.Piercing, PCLCardTarget.AoE)
            .SetSeries(Saber.DATA.Series);

    public Saber_X()
    {
        super(DATA);

        Initialize(1, 0, 3, 1);
        SetUpgrade(2, 0, 1);

        SetAffinity_Red(1);
        SetAffinity_Green(1, 0, 5);
        SetAffinity_Light(1);

        SetHitCount(3);

        SetEthereal(true);
        SetHaste(true);
        SetPurge(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.VFX(new BorderLongFlashEffect(Color.GREEN));
        PCLActions.Bottom.VFX(VFX.ShockWave(p.hb, Color.GREEN), 0.4f).SetRealtime(true);
        PCLActions.Bottom.DealCardDamageToAll(this, AttackEffects.NONE).forEach(d -> d
        .SetDamageEffect((c, __) -> PCLGameEffects.List.Add(new DieDieDieEffect())));

        PCLActions.Bottom.GainBlur(secondaryValue);
        PCLActions.Bottom.StackPower(new Saber_XPower(p, magicNumber));
    }

    public static class Saber_XPower extends PCLPower
    {
        public Saber_XPower(AbstractCreature owner, int amount)
        {
            super(owner, Saber_X.DATA);

            Initialize(amount);
        }

        @Override
        public void onPlayCard(AbstractCard card, AbstractMonster m)
        {
            super.onPlayCard(card, m);
            PCLActions.Bottom.Draw(1);
            if (card.costForTurn > 0) {
                ResetAmount();
            }
            else {
                ReducePower(1);
            }

            this.flashWithoutSound();
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);

            RemovePower();
        }
    }

}