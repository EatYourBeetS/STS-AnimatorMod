package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ModifyDamageAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.actions.common.ModifyBlockActionWhichActuallyWorks;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.PlayerStatistics;

public class Sloth extends AnimatorCard
{
    public static final String ID = Register(Sloth.class.getSimpleName());

    public Sloth()
    {
        super(ID, 3, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);

        Initialize(3,3, 2);

        AddExtendedDescription();

        SetSynergy(Synergies.FullmetalAlchemist);
    }

    @Override
    public void atTurnStart()
    {
        super.atTurnStart();

        if (PlayerStatistics.getTurnCount() > 0)
        {
            int bonusAmount = AbstractDungeon.player.orbs.size();
            //int bonusAmount = PlayerStatistics.GetUniqueOrbsCount() + 2;

            GameActionsHelper.AddToBottom(new ModifyDamageAction(this.uuid, bonusAmount));
            GameActionsHelper.AddToBottom(new ModifyBlockActionWhichActuallyWorks(this.uuid, bonusAmount));
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (this.block > 0)
        {
            GameActionsHelper.GainBlock(p, this.block);
        }
        if (this.damage > 0)
        {
            GameActionsHelper.DamageTarget(p, m, this, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        }

        GameActionsHelper.ApplyPower(p, m, new VulnerablePower(m, this.magicNumber, false), this.magicNumber);
        //GameActionsHelper.ApplyPower(p, m, new WeakPower(m, this.magicNumber, false), this.magicNumber);

        GameActionsHelper.AddToBottom(new ModifyDamageAction(this.uuid, -(int)Math.ceil(this.baseDamage/2.0)));
        GameActionsHelper.AddToBottom(new ModifyBlockActionWhichActuallyWorks(this.uuid, -(int)Math.ceil(this.baseBlock/2.0)));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
            upgradeDamage(1);
            upgradeBlock(1);
        }
    }
}