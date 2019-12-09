package eatyourbeets.cards.animator;

import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.utilities.GameActions;
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
        GameActions.Bottom.GainBlock(this.block);

        for (AbstractMonster enemy : GameUtilities.GetCurrentEnemies(true))
        {
            GameActions.Bottom.ApplyBurning(p, enemy, magicNumber);
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
            GameActions.Bottom.Add(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(false)));

            return true;
        }

        return false;
    }
}