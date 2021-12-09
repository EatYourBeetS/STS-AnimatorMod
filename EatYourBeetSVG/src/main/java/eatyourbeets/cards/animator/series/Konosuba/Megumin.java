package eatyourbeets.cards.animator.series.Konosuba;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.*;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

public class Megumin extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Megumin.class)
            .SetAttack(2, CardRarity.RARE, EYBAttackType.Elemental, EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();
    public static final int ATTACK_TURNS = 2;
    public static final int SYNERGY_REQUIREMENT = 5;

    public Megumin()
    {
        super(DATA);

        Initialize(15, 0, 5, ATTACK_TURNS);
        SetUpgrade( 2, 0);

        SetAffinity_Blue(1, 0, 4);
        SetAffinity_Red(1, 0, 0);
        SetAffinity_Light(0,0,1);

        SetExhaust(true);
        SetUnique(true, true);
    }

    @Override
    protected void OnUpgrade()
    {
        if (timesUpgraded % 3 == 0)
        {
            upgradeMagicNumber(1);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        GameActions.Bottom.StackPower(new MeguminPower(player, this));
    }

    public static class MeguminPower extends AnimatorPower implements OnSynergySubscriber
    {
        private final EYBCard sourceCard;
        private final int secondaryAmount;
        private int synergies;
        private int turns;

        public MeguminPower(AbstractPlayer owner, EYBCard sourceCard)
        {
            super(owner, Megumin.DATA);

            this.sourceCard = sourceCard;
            this.amount = sourceCard.damage;
            this.secondaryAmount = sourceCard.magicNumber;
            updateDescription();
        }

        @Override
        public void onInitialApplication()
        {
            super.onInitialApplication();
            turns = ATTACK_TURNS;
            CombatStats.onSynergy.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            CombatStats.onSynergy.Unsubscribe(this);
        }

        @Override
        protected ColoredString GetSecondaryAmount(Color c)
        {
            return new ColoredString(turns, Color.WHITE, c.a);
        }

        @Override
        public void atStartOfTurnPostDraw()
        {
            super.atStartOfTurnPostDraw();

            turns -= 1;

            if (turns == 0) {
                GameActions.Bottom.SFX(SFX.ORB_LIGHTNING_PASSIVE, 0.9f, 1.1f);
                GameActions.Bottom.Wait(0.35f);
                GameActions.Bottom.SFX(SFX.ORB_LIGHTNING_PASSIVE, 0.8f, 1.2f);
                GameActions.Bottom.BorderFlash(Color.ORANGE);
                GameActions.Bottom.Wait(0.35f);
                GameActions.Bottom.SFX(SFX.ORB_LIGHTNING_PASSIVE, 0.7f, 1.3f);
                GameActions.Bottom.Wait(0.35f);
                GameActions.Bottom.BorderFlash(Color.RED);
                GameActions.Bottom.SFX(SFX.ORB_LIGHTNING_PASSIVE, 0.5f, 1.5f);
                GameActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.LONG, ScreenShake.ShakeIntensity.HIGH));

                for (AbstractCreature m1 : GameUtilities.GetEnemies(true))
                {
                    GameActions.Bottom.VFX(VFX.FlameBarrier(m1.hb));
                    GameActions.Bottom.VFX(VFX.SmallExplosion(m1.hb));
                }

                final int[] damageMatrix = DamageInfo.createDamageMatrix(amount, true);
                GameActions.Bottom.DealDamageToAll(damageMatrix, DamageInfo.DamageType.NORMAL, AttackEffects.NONE)
                        .SetPiercing(true, false);

                if (synergies >= SYNERGY_REQUIREMENT && CombatStats.TryActivateLimited(sourceCard.cardID))
                {
                    GameActions.Bottom.ModifyAllInstances(sourceCard.uuid, AbstractCard::upgrade)
                            .IncludeMasterDeck(true)
                            .IsCancellable(false);
                }

                RemovePower();
            }
        }

        @Override
        public void OnSynergy(AbstractCard card) {
            amount += secondaryAmount;
            synergies += 1;
            flash();
        }

        @Override
        public void updateDescription()
        {
            description = FormatDescription(0, turns, amount, secondaryAmount, synergies < SYNERGY_REQUIREMENT ? JUtils.Format(powerStrings.DESCRIPTIONS[1],SYNERGY_REQUIREMENT - synergies) : "");
        }
    }
}