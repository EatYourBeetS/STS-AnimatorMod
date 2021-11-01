package eatyourbeets.powers.animator;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;
import eatyourbeets.utilities.JUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ProvokedPower extends AnimatorPower
{
    public static final int ATTACK_MULTIPLIER = 50;
    public static final String POWER_ID = CreateFullID(ProvokedPower.class);
    private byte moveByte;
    private AbstractMonster.Intent moveIntent;
    private EnemyMoveInfo move;

    public ProvokedPower(AbstractCreature owner)
    {
        super(owner, POWER_ID);

        Initialize(1, PowerType.DEBUFF, true);
    }

    @Override
    public void onRemove()
    {
        super.onRemove();

        AbstractMonster m = (AbstractMonster)this.owner;
        if (this.move != null) {
            m.setMove(this.moveByte, this.moveIntent, this.move.baseDamage, this.move.multiplier, this.move.isMultiDamage);
        } else {
            m.setMove(this.moveByte, this.moveIntent);
        }

        m.createIntent();
        m.applyPowers();
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        final AbstractMonster monster = JUtils.SafeCast(owner, AbstractMonster.class);
        if (monster != null && !GameUtilities.IsAttacking(monster.intent))
        {
            this.moveByte = monster.nextMove;
            this.moveIntent = monster.intent;
            GameActions.Last.Callback(() -> {
                try {
                    Field f = AbstractMonster.class.getDeclaredField("move");
                    f.setAccessible(true);
                    this.move = (EnemyMoveInfo)f.get(monster);
                    this.move.intent = AbstractMonster.Intent.ATTACK;
                    ArrayList<DamageInfo> damages = monster.damage;
                    if (damages == null || damages.isEmpty()) {
                        this.move.baseDamage = 1;
                    }
                    else {
                        this.move.baseDamage = damages.get(0).base;
                    }
                    monster.createIntent();
                } catch (NoSuchFieldException | IllegalAccessException var2) {
                    JUtils.LogWarning(this, "Monster could not be provoked");
                }
            });
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        ReducePower(1);
    }

    @Override
    public void updateDescription() {
        this.description =  FormatDescription(0, ATTACK_MULTIPLIER);
    }

    @Override
    public float atDamageReceive(float damage, DamageInfo.DamageType type) {
        return type == DamageInfo.DamageType.NORMAL ? damage * (1 + (ATTACK_MULTIPLIER / 100f)) : damage;
    }
}