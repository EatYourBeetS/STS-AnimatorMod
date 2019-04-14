package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Fredrika extends AnimatorCard
{
    public static final String ID = CreateFullID(Fredrika.class.getSimpleName());

    public Fredrika()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 4, 1);

        SetSynergy(Synergies.Chaika, true);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        AbstractPlayer p = AbstractDungeon.player;
        GameActionsHelper.ApplyPower(p, p, new MetallicizePower(p, this.magicNumber), this.magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        for (AbstractMonster m1 : AbstractDungeon.getCurrRoom().monsters.monsters)
        {
            if (!m1.isDying && m1.currentHealth > 0)
            {
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block, true));
            }
        }

//        if (HasActiveSynergy())
//        {
//            if (!AbstractDungeon.player.hand.isEmpty() && !AbstractDungeon.player.hasRelic("Runic Pyramid") && !AbstractDungeon.player.hasPower("Equilibrium"))
//            {
//                AbstractDungeon.actionManager.addToBottom(new RetainCardsAction(p, this.magicNumber));
//            }
//        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBlock(2);
            //upgradeMagicNumber(1);
        }
    }
}