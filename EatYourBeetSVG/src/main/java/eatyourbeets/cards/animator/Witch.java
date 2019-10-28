package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.powers.PlayerStatistics;

public class Witch extends AnimatorCard
{
    public static final String ID = Register(Witch.class.getSimpleName(), EYBCardBadge.Special);

    public Witch()
    {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL);

        Initialize(0, 12,3);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();

        GameActionsHelper.AddToBottom(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(true)));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.GainBlock(p, this.block);

        for (AbstractMonster m1 : PlayerStatistics.GetCurrentEnemies(true))
        {
            GameActionsHelper.ApplyPower(p, m1, new BurningPower(m1, p, this.magicNumber), this.magicNumber);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(3);
        }
    }
}