package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.GameActionsHelper; import eatyourbeets.utilities.GameActionsHelper2;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.orbs.Fire;

public class MisaKurobane extends AnimatorCard
{
    public static final String ID = Register(MisaKurobane.class.getSimpleName());

    public MisaKurobane()
    {
        super(ID, 0, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0,1);

        SetEvokeOrbCount(1);
        SetExhaust(true);
        SetSynergy(Synergies.Charlotte);

        if (InitializingPreview())
        {
            cardData.InitializePreview(new Yusarin(), true);
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ChannelOrb(new Fire(), true);
        GameActionsHelper2.Draw(this.magicNumber);
        GameActionsHelper.AddToBottom(new MakeTempCardInDiscardAction(new Yusarin(), 1));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeMagicNumber(1);
        }
    }
}