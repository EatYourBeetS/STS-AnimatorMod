package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.common.ChooseFromPileAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.EvePower;
import eatyourbeets.powers.animator.OrbCore_AbstractPower;

import java.util.ArrayList;

public class Eve extends AnimatorCard
{
    public static final String ID = Register(Eve.class.getSimpleName(), EYBCardBadge.Special);

    public Eve()
    {
        super(ID, 3, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0, 1, 0);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.AddToBottom(new ChooseFromPileAction(1, false,
                OrbCore_AbstractPower.CreateCoresGroup(true), this::OrbChosen, this, ""));

        if (EffectHistory.TryActivateLimited(cardID))
        {
            GameActionsHelper.ApplyPower(p, p, new EvePower(p, 1, 1), 1);
        }

        GameActionsHelper.AddToBottom(new IncreaseMaxOrbAction(magicNumber));

        if (secondaryValue > 0)
        {
            GameActionsHelper.ApplyPower(p, p, new MetallicizePower(p, secondaryValue), secondaryValue);
        }
    }

    private void OrbChosen(Object state, ArrayList<AbstractCard> chosen)
    {
        if (state == this && chosen != null && chosen.size() > 0)
        {
            for (AbstractCard c : chosen)
            {
                c.applyPowers();
                c.use(AbstractDungeon.player, null);
            }
        }
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeSecondaryValue(2);
        }
    }
}