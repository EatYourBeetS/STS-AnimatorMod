package eatyourbeets.powers.animator;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.actions.defect.EvokeSpecificOrbAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.effects.AttackEffects;
import eatyourbeets.effects.SFX;
import eatyourbeets.effects.VFX;
import eatyourbeets.interfaces.subscribers.OnOrbApplyFocusSubscriber;
import eatyourbeets.orbs.animator.Water;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

import java.util.ArrayList;

public class KuragesOathPower extends AnimatorPower implements OnOrbApplyFocusSubscriber
{
    public static final String POWER_ID = CreateFullID(KuragesOathPower.class);
    protected static final Color PARTICLE_COLOR = Color.TEAL.cpy();
    public int secondaryAmount;

    public KuragesOathPower(AbstractPlayer owner, int amount, int secondaryAmount)
    {
        super(owner, POWER_ID);

        this.priority = -99;
        this.secondaryAmount = secondaryAmount;

        Initialize(amount);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        GameActions.Bottom.VFX(VFX.WaterDome(owner.hb.cX,(owner.hb.y+owner.hb.cY)/2));
        GameActions.Bottom.SFX(SFX.ANIMATOR_WATER_DOME);
        CombatStats.onOrbApplyFocus.Subscribe(this);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        CombatStats.onOrbApplyFocus.Unsubscribe(this);
    }


    @Override
    public void updateDescription()
    {
        description = FormatDescription(0, amount, secondaryAmount);
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        if (GameUtilities.GetOrbCount(Water.ORB_ID) == 0) {
            GameActions.Bottom.TakeDamage(secondaryAmount, AbstractGameAction.AttackEffect.NONE).AddCallback(() -> {
                Water water = new Water();
                GameActions.Bottom.TriggerOrbPassive(water, 1);
                GameActions.Bottom.ChannelOrb(water);
            });
        }
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount)
    {
        int newDamage = damageAmount;
        final ArrayList<AbstractOrb> waterOrbs = new ArrayList<>();
        for (AbstractOrb orb : player.orbs)
        {
            if (Water.ORB_ID.equals(orb.ID) && orb.evokeAmount > 0)
            {
                waterOrbs.add(orb);
            }
        }

        if (waterOrbs.size() > 0)
        {
            if (damageAmount > 0)
            {
                newDamage = AbsorbDamage(damageAmount, waterOrbs);
            }

            if (info.owner != null && info.owner.isPlayer != owner.isPlayer)
            {
                GameActions.Bottom.DealDamage(owner, info.owner, damageAmount - newDamage, DamageInfo.DamageType.THORNS, AttackEffects.WATER);
                flashWithoutSound();
            }
            else if (owner.isPlayer) {
                int[] damageMatrix = DamageInfo.createDamageMatrix(damageAmount - newDamage, false);
                GameActions.Bottom.DealDamageToAll(damageMatrix, DamageInfo.DamageType.THORNS, AttackEffects.WATER);
                flashWithoutSound();
            }
        }

        return super.onAttackedToChangeDamage(info, newDamage);
    }

    private int AbsorbDamage(int damage, ArrayList<AbstractOrb> waterOrbs)
    {
        final AbstractOrb next = waterOrbs.get(0);
        final float temp = damage;

        damage = Math.max(0, damage - next.evokeAmount);
        next.evokeAmount -= temp;

        if (next.evokeAmount <= 0)
        {
            waterOrbs.remove(next);
            next.evokeAmount = 0;
            GameActions.Top.Add(new EvokeSpecificOrbAction(next));
        }

        return (damage > 0 && waterOrbs.size() > 0) ? AbsorbDamage(damage, waterOrbs) : damage;
    }

    @Override
    public float modifyBlock(float blockAmount) {
        return 0f;
    }

    @Override
    public float modifyBlock(float blockAmount, AbstractCard card) {
        return 0f;
    }

    @Override
    public float modifyBlockLast(float blockAmount) {
        return 0f;
    }

    @Override
    public void OnApplyFocus(AbstractOrb orb) {
        if (Water.ORB_ID.equals(orb.ID)) {
            orb.passiveAmount += this.amount;
        }
    }
}