package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.BurningPower;
import eatyourbeets.powers.PlayerStatistics;

public class Witch extends AnimatorCard
{
    public static final String ID = CreateFullID(Witch.class.getSimpleName());

    public Witch()
    {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL);

        Initialize(0, 9,5);

        this.tags.add(CardTags.HEALING);

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
            upgradeBlock(3);
        }
    }
}