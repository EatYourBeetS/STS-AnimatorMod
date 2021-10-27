package eatyourbeets.cards.animator.ultrarare;

import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.WrathStance;
import com.megacrit.cardcrawl.vfx.combat.FlameBarrierEffect;
import com.megacrit.cardcrawl.vfx.combat.VerticalImpactEffect;
import eatyourbeets.cards.animator.series.HitsugiNoChaika.ChaikaKamaz;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.interfaces.subscribers.OnAfterCardPlayedSubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Giselle extends AnimatorCard_UltraRare
{
    public static final EYBCardData DATA = Register(Giselle.class)
            .SetAttack(2, CardRarity.SPECIAL, EYBAttackType.Elemental)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.GATE);

    public Giselle()
    {
        super(DATA);

        Initialize(18, 0, 10);
        SetUpgrade(0, 0, 10);

        SetAffinity_Star(2);
        SetHaste(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.VFX(new VerticalImpactEffect(m.hb.cX + m.hb.width / 4f, m.hb.cY - m.hb.height / 4f));
        GameActions.Bottom.VFX(new FlameBarrierEffect(m.hb.cX, m.hb.cY), 0.5f);
        GameActions.Bottom.DealDamage(this, m, AttackEffects.NONE);
        GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.MED, ScreenShake.ShakeIntensity.MED));

        GameActions.Bottom.ChangeStance(WrathStance.STANCE_ID);
        GameActions.Bottom.StackPower(new GisellePower(p, 1));
    }

    @Override
    public void triggerWhenDrawn() {
        super.triggerWhenDrawn();

        GameActions.Bottom.RaiseFireLevel(magicNumber);
        GameActions.Bottom.RaiseEarthLevel(magicNumber);
    }

    public static class GisellePower extends AnimatorPower implements OnAfterCardPlayedSubscriber {
        public GisellePower(AbstractPlayer owner, int amount) {
            super(owner, ChaikaKamaz.DATA);

            this.amount = amount;

            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();

            CombatStats.onAfterCardPlayed.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onAfterCardPlayed.Unsubscribe(this);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            RemovePower();

            super.atEndOfTurn(isPlayer);
        }

        @Override
        public void OnAfterCardPlayed(AbstractCard card) {
            if (card.type == CardType.ATTACK && card.damage > 0)
            {
                GameActions.Top.GainSupportDamage(card.damage);
            }
        }

        @Override
        public void updateDescription() {
            description = FormatDescription(0, amount);
        }
    }
}