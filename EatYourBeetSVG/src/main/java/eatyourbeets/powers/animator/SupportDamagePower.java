package eatyourbeets.powers.animator;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.DaggerSprayEffect;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.cards.animator.status.Status_Void;
import eatyourbeets.cards.base.EYBAttackType;
import eatyourbeets.powers.AnimatorPower;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.*;

public class SupportDamagePower extends AnimatorPower
{
    public static final String POWER_ID = CreateFullID(SupportDamagePower.class);

    public static AbstractMonster FindBestTarget()
    {
        return JUtils.FindMin(GameUtilities.GetEnemies(true), m -> m.currentHealth);
    }

    private AbstractMonster target;
    private Status_Void fakeCard;

    public SupportDamagePower(AbstractCreature owner, int amount)
    {
        super(owner, POWER_ID);

        fakeCard = new Status_Void();
        fakeCard.SetAttackType(EYBAttackType.Ranged);
        fakeCard.rarity = AbstractCard.CardRarity.SPECIAL;
        fakeCard.tags.clear();
        fakeCard.tags.add(GR.Enums.CardTags.VOLATILE);
        fakeCard.cost = fakeCard.costForTurn = -2;
        fakeCard.purgeOnUse = true;

        Initialize(amount);
    }

    @Override
    public void updateDescription()
    {
        UpdateDamage();

        this.description = FormatDescription(0, fakeCard.damage);
    }

    @Override
    public void onInitialApplication()
    {
        super.onInitialApplication();

        UpdateDamage();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer)
    {
        super.atEndOfTurn(isPlayer);

        GameActions.Bottom.Callback(() ->
        {
            UpdateDamage();
            GameUtilities.RemoveDamagePowers();
            GameActions.Top.DealDamage(owner, target, fakeCard.damage, DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.NONE);

            if (fakeCard.damage < 10)
            {
                GameEffects.Queue.Add(new DaggerSprayEffect(AbstractDungeon.getMonsters().shouldFlipVfx()));
            }
            else
            {
                GameEffects.Queue.Add(new DieDieDieEffect());
            }
        });
        this.flashWithoutSound();
    }

    @Override
    protected ColoredString GetSecondaryAmount(Color c)
    {
        if (fakeCard.damage == amount)
        {
            return null;
        }

        return new ColoredString(fakeCard.damage, fakeCard.damage > amount ? Settings.GREEN_TEXT_COLOR : Settings.RED_TEXT_COLOR, c.a);
    }

    private void UpdateDamage()
    {
        if (GameUtilities.InBattle(false))
        {
            target = FindBestTarget();
            GameUtilities.ModifyDamage(fakeCard, amount, false);
            fakeCard.calculateCardDamage(target);
        }
        else
        {
            fakeCard.damage = fakeCard.baseDamage = amount;
        }
    }
}