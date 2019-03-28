package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.ModifyBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.misc.RandomizedList;

public class TukaLunaMarceau extends AnimatorCard
{
    public static final String ID = CreateFullID(TukaLunaMarceau.class.getSimpleName());

    public TukaLunaMarceau()
    {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0,4,1);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) 
    {
        GameActionsHelper.GainBlock(p, this.block);

        RandomizedList<AbstractCard> cards = new RandomizedList<>();
        for (AbstractCard c : p.hand.group)
        {
            if (c != this && c.baseBlock > 0)
            {
                cards.Add(c);
            }
        }

        if (cards.Count() > 0)
        {
            AbstractCard toBuff = cards.Retrieve(AbstractDungeon.miscRng);
            GameActionsHelper.AddToTop(new ModifyBlockAction(toBuff.uuid, this.magicNumber));
            toBuff.superFlash();
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBlock(1);
            upgradeMagicNumber(1);
        }
    }
}