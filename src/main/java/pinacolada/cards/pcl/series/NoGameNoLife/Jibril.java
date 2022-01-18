package pinacolada.cards.pcl.series.NoGameNoLife;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.utilities.ColoredString;
import eatyourbeets.utilities.Colors;
import pinacolada.actions.orbs.ShuffleOrbs;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCard;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.DamageAttribute;
import pinacolada.cards.base.modifiers.CostModifiers;
import pinacolada.effects.AttackEffects;
import pinacolada.effects.VFX;
import pinacolada.interfaces.subscribers.OnPCLClickablePowerUsed;
import pinacolada.orbs.pcl.Air;
import pinacolada.orbs.pcl.Earth;
import pinacolada.powers.PCLClickablePower;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class Jibril extends PCLCard implements OnPCLClickablePowerUsed
{
    public static final PCLCardData DATA = Register(Jibril.class)
            .SetAttack(3, CardRarity.RARE, PCLAttackType.Dark, eatyourbeets.cards.base.EYBCardTarget.ALL)
            .SetSeriesFromClassPackage();
    private int costReduction = 0;

    public Jibril()
    {
        super(DATA);

        Initialize(0, 0, 2);
        SetUpgrade(0, 0, 0);

        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Light(1);
        SetAffinity_Dark(1);
        SetAffinity_Silver(1);

        SetExhaust(true);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();
        costReduction = 0;

        Refresh(null);
    }

    @Override
    public void triggerOnOtherCardPlayed(AbstractCard c)
    {
        super.triggerOnOtherCardPlayed(c);

        if (c.type == CardType.POWER) {
            costReduction -= 1;
        }
        PCLActions.Bottom.Callback(this::RefreshCost);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);
        PCLCombatStats.onPCLClickablePowerUsed.Subscribe(this);
    }

    @Override
    public void Refresh(AbstractMonster enemy)
    {
        super.Refresh(enemy);

        RefreshCost();
    }

    public void RefreshCost()
    {
        CostModifiers.For(this).Set(costReduction);
    }

    @Override
    public AbstractAttribute GetDamageInfo()
    {
        return DamageAttribute.Instance.SetCard(this).SetText(new ColoredString("?", Colors.Cream(1f)));
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (upgraded) {
            PCLActions.Bottom.TriggerOrbPassive(player.orbs.size()).SetSequential(true);
        }
        PCLActions.Bottom.Callback(new ShuffleOrbs(1)).AddCallback(
                () -> {
                    int totalDamage = 0;
                    for (int i = 0; i < Math.min(magicNumber, player.orbs.size()); i++) {
                        AbstractOrb orb = player.orbs.get(i);
                        if (PCLGameUtilities.IsValidOrb(orb)) {
                            if (Air.ORB_ID.equals(orb.ID)) {
                                totalDamage += orb.evokeAmount * Air.EVOKE_DAMAGE_PER_HIT;
                            }
                            else if (Earth.ORB_ID.equals(orb.ID)) {
                                totalDamage += orb.evokeAmount * ((Earth) orb).projectilesCount;
                            }
                            else {
                                totalDamage += orb.evokeAmount;
                            }

                        }
                    }
                    final int[] damageMatrix = DamageInfo.createDamageMatrix(totalDamage, true);
                    PCLActions.Bottom.VFX(VFX.ShockWave(p.hb, Color.VIOLET), 0.3f);
                    PCLActions.Bottom.DealDamageToAll(damageMatrix, DamageInfo.DamageType.NORMAL, AttackEffects.DARKNESS)
                            .SetPiercing(true, false);
                }
        );
    }

    @Override
    public boolean OnClickablePowerUsed(PCLClickablePower power, AbstractMonster target) {
        costReduction -= 1;
        PCLActions.Bottom.Callback(this::RefreshCost);
        return true;
    }
}
