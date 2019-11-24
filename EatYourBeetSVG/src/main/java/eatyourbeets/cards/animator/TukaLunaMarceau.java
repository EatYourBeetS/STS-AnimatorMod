package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper;

public class TukaLunaMarceau extends AnimatorCard
{
    public static final String ID = Register(TukaLunaMarceau.class.getSimpleName(), EYBCardBadge.Discard);

    public TukaLunaMarceau()
    {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);

        Initialize(0, 2, 3);

        SetSynergy(Synergies.Gate);
    }

    @Override
    public void triggerOnManualDiscard()
    {
        super.triggerOnManualDiscard();

        GameActionsHelper.GainBlock(AbstractDungeon.player, magicNumber);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (p.currentBlock <= 0)
        {
            GameActionsHelper.DrawCard(p, 1);
        }

        GameActionsHelper.GainBlock(p, this.block);
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