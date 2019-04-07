package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.LoseDexterityPower;
import com.megacrit.cardcrawl.vfx.combat.DieDieDieEffect;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class Hakurou extends AnimatorCard //implements OnEndOfTurnSubscriber
{
    public static final String ID = CreateFullID(Hakurou.class.getSimpleName());

    public Hakurou()
    {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(1,0, 3);

        baseSecondaryValue = secondaryValue = 1;

        AddExtendedDescription();

        SetSynergy(Synergies.TenSura);
    }

    @Override
    public float calculateModifiedCardDamage(AbstractPlayer player, AbstractMonster mo, float tmp)
    {
        return super.calculateModifiedCardDamage(player, mo, tmp + GetDexterity(player));
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        AbstractPlayer p = AbstractDungeon.player;

        LoseDexterityPower power = (LoseDexterityPower) p.getPower(LoseDexterityPower.POWER_ID);
        if (power != null)
        {
            power.amount += 1;
        }
        else
        {
            p.powers.add(new LoseDexterityPower(p, this.secondaryValue));
        }

        ApplyPowerAction dexAction = new ApplyPowerAction(p, p, new DexterityPower(p, this.secondaryValue), 1, true);
        GameActionsHelper.AddToTop(dexAction);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new DieDieDieEffect()));
        for (int i = 0; i < this.magicNumber; i++)
        {
            GameActionsHelper.DamageTarget(p, m, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.NONE);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeDamage(1);
        }
    }

    private int GetDexterity(AbstractPlayer player)
    {
        DexterityPower dexterity = (DexterityPower) player.getPower(DexterityPower.POWER_ID);
        if (dexterity != null)
        {
            return dexterity.amount;
        }

        return 0;
    }
//
//    @Override
//    public void OnEndOfTurn(boolean isPlayer)
//    {
//        int loseDex = 0;
//        AbstractPlayer p = AbstractDungeon.player;
//        for (int i = dexterityCounters.size() - 1; i >= 0; i--)
//        {
//            int value = dexterityCounters.get(i) - 1;
//            if (value <= 0)
//            {
//                loseDex += 1;
//                dexterityCounters.remove(i);
//            }
//            else
//            {
//                dexterityCounters.set(i, value - 1);
//            }
//        }
//
//        if (loseDex > 0)
//        {
//            GameActionsHelper.AddToBottom(new ReducePowerAction(p, p, DexterityPower.POWER_ID, loseDex));
//        }
//
//        if (dexterityCounters.size() == 0)
//        {
//            PlayerStatistics.onEndOfTurn.Unsubscribe(this);
//        }
//    }
}