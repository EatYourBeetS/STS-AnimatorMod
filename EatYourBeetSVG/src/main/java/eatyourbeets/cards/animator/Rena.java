package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

public class Rena extends AnimatorCard
{
    public static final String ID = CreateFullID(Rena.class.getSimpleName());

    public Rena()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 4, 1);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        AbstractPlayer p =  AbstractDungeon.player;
        GameActionsHelper.ApplyPower(p, p, new NextTurnBlockPower(p, this.block), this.block);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.GainBlock(p, this.block);
        GameActionsHelper.ApplyPower(p, p, new BlurPower(p, this.magicNumber), this.magicNumber);

        if (HasActiveSynergy())
        {
            GameActionsHelper.ApplyPower(p, p, new NextTurnBlockPower(p, this.block), this.block);
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBlock(2);
        }
    }
}