package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.unique.PoisonLoseHpAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.Synergies;
import eatyourbeets.utilities.GameUtilities;

public class Tyuule extends AnimatorCard
{
    public static final String ID = Register(Tyuule.class.getSimpleName(), EYBCardBadge.Exhaust);

    public Tyuule()
    {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ALL_ENEMY);

        Initialize(0, 0, 3);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        for (AbstractMonster m : GameUtilities.GetCurrentEnemies(true))
        {
            AbstractPower poison = m.getPower(PoisonPower.POWER_ID);
            if (poison != null)
            {
                GameActionsHelper.AddToBottom(new PoisonLoseHpAction(m, AbstractDungeon.player, poison.amount, AbstractGameAction.AttackEffect.POISON));
            }
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (AbstractMonster m1 : GameUtilities.GetCurrentEnemies(true))
        {
            GameActionsHelper.ApplyPower(p, m1, new PoisonPower(m1, p, this.magicNumber), this.magicNumber);
            GameActionsHelper.ApplyPower(p, m1, new VulnerablePower(m1, 1, false), 1);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }
}