package eatyourbeets.cards.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.random.Random;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.common.ChooseFromPileAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;
import eatyourbeets.powers.animator.EvePower;
import eatyourbeets.powers.animator.OrbCore_AbstractPower;
import eatyourbeets.utilities.RandomizedList;

import java.util.ArrayList;

public class Eve extends AnimatorCard
{
    public static final String ID = CreateFullID(Eve.class.getSimpleName());

    public Eve()
    {
        super(ID, 3, CardType.POWER, CardRarity.RARE, CardTarget.SELF);

        Initialize(0,0, 1, 1);

        SetSynergy(Synergies.Elsword);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.ApplyPower(p, p, new EvePower(p, this.magicNumber, 1), 1);

        Random rng = AbstractDungeon.miscRng;
        RandomizedList<AbstractCard> cores = new RandomizedList<>(OrbCore_AbstractPower.GetAllCores());

        CardGroup group1 = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        group1.group.add(cores.Retrieve(rng));
        group1.group.add(cores.Retrieve(rng));
        group1.group.add(cores.Retrieve(rng));

        GameActionsHelper.AddToBottom(new ChooseFromPileAction(secondaryValue, false, group1, this::OrbChosen, this, ""));
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
            upgradeSecondaryValue(1);
        }
    }
}