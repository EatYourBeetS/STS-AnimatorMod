package eatyourbeets.cards.animator;

import basemod.BaseMod;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameActionsHelper_Legacy;
import eatyourbeets.actions._legacy.common.VariableDiscardAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class LimeBell extends AnimatorCard
{
    public static final String ID = Register(LimeBell.class.getSimpleName());

    public LimeBell()
    {
        super(ID, 2, CardType.SKILL, CardColor.COLORLESS, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0, 7, 2);

        SetExhaust(true);
        SetSynergy(Synergies.AccelWorld);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActions.Bottom.GainBlock(this.block);
        GameActions.Bottom.Reload(name, cards ->
        {
            ArrayList<AbstractOrb> orbs = AbstractDungeon.player.orbs;
            if (orbs.size() > 0)
            {
                for (int i = 0; i < cards.size(); i++)
                {
                    orbs.get(0).onStartOfTurn();
                    orbs.get(0).onEndOfTurn();
                }
            }
        });

        GameActionsHelper_Legacy.AddToBottom(new VariableDiscardAction(this, p, BaseMod.MAX_HAND_SIZE, this, this::OnDiscard));
    }

    @Override
    public void upgrade()
    {
        if (TryUpgrade())
        {
            upgradeBlock(4);
        }
    }

    private void OnDiscard(Object state, ArrayList<AbstractCard> discarded)
    {
        if (state == this && discarded != null && discarded.size() > 0)
        {
            AbstractPlayer p = AbstractDungeon.player;
            int amount = discarded.size() * this.magicNumber;
            GameActions.Bottom.GainTemporaryHP(amount);
        }
    }
}