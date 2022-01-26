package pinacolada.cards.pcl.series.Konosuba;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.utility.ShakeScreenAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.ColoredString;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PCLPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class Megumin extends PCLCard
{
    public static final PCLCardData DATA = Register(Megumin.class)
            .SetAttack(2, CardRarity.RARE, PCLAttackType.Fire, PCLCardTarget.AoE)
            .SetSeriesFromClassPackage();
    public static final int ATTACK_TURNS = 2;
    public static final int SYNERGY_REQUIREMENT = 6;

    public Megumin()
    {
        super(DATA);

        Initialize(14, 0, 4, ATTACK_TURNS);
        SetUpgrade( 2, 0);

        SetAffinity_Blue(1, 0, 4);
        SetAffinity_Red(1, 0, 0);
        SetAffinity_Light(0,0,1);

        SetExhaust(true);
        SetUnique(true, -1);
    }

    @Override
    protected String GetRawDescription(Object... args)
    {
        return super.GetRawDescription(SYNERGY_REQUIREMENT);
    }

    @Override
    protected void OnUpgrade()
    {
        if (timesUpgraded % 4 == 0)
        {
            upgradeMagicNumber(1);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new MeguminPower(player, this));
    }

    public static class MeguminPower extends PCLPower implements OnSynergySubscriber
    {
        private final PCLCard sourceCard;
        private final int secondaryAmount;
        private int synergies;
        private int turns;

        public MeguminPower(AbstractPlayer owner, PCLCard sourceCard)
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
            PCLCombatStats.onSynergy.Subscribe(this);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onSynergy.Unsubscribe(this);
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
                PCLActions.Bottom.SFX(SFX.ORB_LIGHTNING_PASSIVE, 0.9f, 1.1f);
                PCLActions.Bottom.Wait(0.35f);
                PCLActions.Bottom.SFX(SFX.ORB_LIGHTNING_PASSIVE, 0.8f, 1.2f);
                PCLActions.Bottom.BorderFlash(Color.ORANGE);
                PCLActions.Bottom.Wait(0.35f);
                PCLActions.Bottom.SFX(SFX.ORB_LIGHTNING_PASSIVE, 0.7f, 1.3f);
                PCLActions.Bottom.Wait(0.35f);
                PCLActions.Bottom.BorderFlash(Color.RED);
                PCLActions.Bottom.SFX(SFX.ORB_LIGHTNING_PASSIVE, 0.5f, 1.5f);
                PCLActions.Bottom.Add(new ShakeScreenAction(0.5f, ScreenShake.ShakeDur.LONG, ScreenShake.ShakeIntensity.HIGH));

                for (AbstractCreature m1 : PCLGameUtilities.GetEnemies(true))
                {
                    PCLActions.Bottom.VFX(VFX.FlameBarrier(m1.hb));
                    PCLActions.Bottom.VFX(VFX.SmallExplosion(m1.hb));
                }

                final int[] damageMatrix = DamageInfo.createDamageMatrix(amount, true);
                PCLActions.Bottom.DealDamageToAll(damageMatrix, DamageInfo.DamageType.NORMAL, AttackEffects.NONE)
                        .SetPiercing(true, false)
                        .SetPCLAttackType(PCLAttackType.Fire, true);

                if (synergies >= SYNERGY_REQUIREMENT && CombatStats.TryActivateLimited(sourceCard.cardID))
                {
                    PCLActions.Bottom.ModifyAllInstances(sourceCard.uuid, AbstractCard::upgrade)
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
            description = FormatDescription(0, turns, amount, secondaryAmount, synergies < SYNERGY_REQUIREMENT ? PCLJUtils.Format(powerStrings.DESCRIPTIONS[1],SYNERGY_REQUIREMENT - synergies) : "");
        }
    }
}