package pinacolada.cards.pcl.ultrarare;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.interfaces.subscribers.OnOrbPassiveEffectSubscriber;
import pinacolada.cards.base.*;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.PCLProjectile;
import pinacolada.effects.SFX;
import pinacolada.effects.VFX;
import pinacolada.orbs.pcl.Metal;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.powers.PowerTriggerConditionType;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLColors;
import pinacolada.utilities.PCLGameEffects;
import pinacolada.utilities.PCLGameUtilities;

public class Rose extends PCLCard_UltraRare
{
    public static final PCLCardData DATA = Register(Rose.class)
            .SetAttack(3, CardRarity.SPECIAL, PCLAttackType.Ranged)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.Elsword);
    public static final int COST = 4;

    public Rose()
    {
        super(DATA);

        Initialize(10, 0, 2, COST);
        SetUpgrade(0, 0, 1, 0);

        SetAffinity_Silver(1);
        SetAffinity_Orange(1, 0, 1);
        SetAffinity_Light(1);
        SetAffinity_Green(0,0,1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamageToRandomEnemy(this, AttackEffects.GUNSHOT);
        PCLActions.Bottom.ChannelOrb(new Metal());
        PCLActions.Bottom.Draw(magicNumber)
                .AddCallback(() -> {
                    PCLActions.Bottom.Reload(name, m, (enemy, cards) ->
                    {
                        for (int i = 0; i < cards.size(); i++) {
                            PCLActions.Bottom.TriggerOrbPassive(p.orbs.size())
                                    .SetFilter(o -> Metal.ORB_ID.equals(o.ID))
                                    .SetSequential(true);
                        }
                    });
                });
        PCLActions.Bottom.StackPower(p, new RosePower(p));
    }

    public static class RosePower extends PCLClickablePower implements OnOrbPassiveEffectSubscriber {
        boolean isActive;

        public RosePower(AbstractCreature owner) {
            super(owner, Rose.DATA, PowerTriggerConditionType.Affinity, COST, null, null, PCLAffinity.Silver);
            this.triggerCondition.SetUses(1, false, false);
            this.Initialize(-1);
        }

        @Override
        public void onRemove()
        {
            super.onRemove();

            PCLCombatStats.onOrbPassiveEffect.Unsubscribe(this);
        }

        public void atEndOfTurn(boolean isPlayer) {
            super.atEndOfTurn(isPlayer);
            if (isActive) {
                this.RemovePower();
            }
        }

        private void makeMove(AbstractOrb orb, int applyAmount) {
            AbstractCreature target = null;
            int minHealth = Integer.MAX_VALUE;

            if (owner.isPlayer) {
                for (AbstractMonster m : PCLGameUtilities.GetEnemies(true))
                {
                    if (m.currentHealth < minHealth)
                    {
                        minHealth = m.currentHealth;
                        target = m;
                    }
                }
            }

            if (target != null) {
                int actualDamage = AbstractOrb.applyLockOn(target, applyAmount);
                if (actualDamage > 0)
                {
                    PCLActions.Bottom.DealDamage(source, target, actualDamage, DamageInfo.DamageType.NORMAL, AttackEffects.NONE)
                            .SetDamageEffect(c ->
                                    {
                                        SFX.Play(SFX.PCL_SUPPORT_DAMAGE);
                                        return PCLGameEffects.List.Add(VFX.ThrowProjectile(new PCLProjectile(Metal.IMAGES.Metal.Texture(), 96f, 96f)
                                                .SetColor(PCLColors.Random(0.8f, 1f, true))
                                                .SetPosition(orb.cX, orb.cY), c.hb)
                                                .AddCallback(hb -> PCLGameEffects.Queue.Add(VFX.Gunshot(hb, 1.3f))))
                                                .duration * 0.5f;
                                    }
                            );
                }
            }
        }

        @Override
        public void OnOrbPassiveEffect(AbstractOrb orb) {
            if (isActive && Metal.ORB_ID.equals(orb.ID)) {
                makeMove(orb, orb.passiveAmount);
            }
        }

        @Override
        public void OnUse(AbstractMonster m, int cost) {
            isActive = true;
            PCLCombatStats.onOrbPassiveEffect.Subscribe(this);
        }

        @Override
        public String GetUpdatedDescription()
        {
            return FormatDescription(isActive ? 1 : 0, COST, powerStrings.DESCRIPTIONS[1]);
        }
    }
}