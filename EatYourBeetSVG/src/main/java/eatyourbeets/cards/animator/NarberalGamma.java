package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.GameActionsHelper;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.TemporaryElectroPower;

public class NarberalGamma extends AnimatorCard// implements OnStartOfTurnPostDrawSubscriber
{
    public static final String ID = CreateFullID(NarberalGamma.class.getSimpleName());

    public NarberalGamma()
    {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 0, 1);

        SetSynergy(Synergies.Overlord, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        AbstractDungeon.actionManager.addToBottom(new ChannelAction(new Lightning(), true));

        if (upgraded)
        {
            GameActionsHelper.ApplyPower(p, p, new TemporaryElectroPower(p));
        }
    }

    @Override
    public void upgrade()
    {
        TryUpgrade();
    }
}