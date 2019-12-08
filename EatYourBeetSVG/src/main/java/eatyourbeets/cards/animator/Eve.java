package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.EvePower;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper2;

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
        GameActionsHelper2.AddToBottom(OrbCore.SelectCoreAction(name, 1)
        .AddCallback(this::OrbChosen));

        if (EffectHistory.TryActivateLimited(cardID))
        {
            GameActionsHelper2.StackPower(new EvePower(p, 1, 1));
        }

        GameActionsHelper2.GainOrbSlots(magicNumber);

        if (secondaryValue > 0)
        {
            GameActionsHelper2.StackPower(new MetallicizePower(p, secondaryValue));
        }
    }

    private void OrbChosen(ArrayList<AbstractCard> chosen)
    {
        if (chosen != null && chosen.size() > 0)
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