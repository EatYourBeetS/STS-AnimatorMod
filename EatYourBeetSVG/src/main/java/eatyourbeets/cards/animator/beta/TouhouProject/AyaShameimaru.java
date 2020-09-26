package eatyourbeets.cards.animator.beta.TouhouProject;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.utilities.GameActions;

public class AyaShameimaru extends AnimatorCard
{
    public static final EYBCardData DATA = Register(AyaShameimaru.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.Self);

    private int bonusBlock = 0;

    public AyaShameimaru()
    {
        super(DATA);

        Initialize(0, 2, 7, 13);
        SetUpgrade(0, 1, 2);

        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    public void triggerWhenDrawn()
    {
        super.triggerWhenDrawn();

        this.AddBlockBonus(magicNumber);
        GameActions.Bottom.Flash(this);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(block);

        if (HasSynergy() && this.block >= secondaryValue)
        {
            GameActions.Bottom.ChangeStance(AgilityStance.STANCE_ID);
        }

        this.AddBlockBonus(-bonusBlock);
    }

    private void AddBlockBonus(int amount)
    {
        bonusBlock += amount;
        baseBlock += amount;
    }
}

