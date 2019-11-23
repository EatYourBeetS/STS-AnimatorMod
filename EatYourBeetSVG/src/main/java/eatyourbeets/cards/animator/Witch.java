package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.interfaces.metadata.Spellcaster;
import eatyourbeets.powers.animator.BurningPower;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.utilities.GameUtilities;

public class Witch extends AnimatorCard implements Spellcaster, StartupCard
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

        Spellcaster.ApplyScaling(this, 2);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.GainBlock(p, this.block);

        for (AbstractMonster m1 : GameUtilities.GetCurrentEnemies(true))
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
    public boolean atBattleStartPreDraw()
    {
        if (AbstractDungeon.currMapNode != null && AbstractDungeon.currMapNode.room != null && AbstractDungeon.currMapNode.room.eliteTrigger)
        {
            GameActionsHelper.AddToBottom(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(false)));
            return true;
        }

        return false;
    }
}