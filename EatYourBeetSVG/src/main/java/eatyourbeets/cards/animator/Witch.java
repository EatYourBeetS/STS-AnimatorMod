package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.interfaces.OnAddedToDeckSubscriber;
import eatyourbeets.interfaces.metadata.Spellcaster;
import eatyourbeets.powers.PlayerStatistics;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.utilities.GameActionsHelper;

public class Witch extends AnimatorCard implements Spellcaster, OnAddedToDeckSubscriber
{
    public static final String ID = Register(Witch.class.getSimpleName(), EYBCardBadge.Special);

    public Witch()
    {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL);

        Initialize(0, 9,2);

        SetSynergy(Synergies.GoblinSlayer);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        Spellcaster.ApplyScaling(this);
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
            upgradeMagicNumber(1);
        }
    }

    @Override
    public void OnAddedToDeck()
    {
        AbstractDungeon.player.obtainPotion(AbstractDungeon.returnRandomPotion(AbstractPotion.PotionRarity.UNCOMMON, false));
    }
}