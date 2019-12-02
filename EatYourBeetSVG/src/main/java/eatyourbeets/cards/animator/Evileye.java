package eatyourbeets.cards.animator;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.interfaces.metadata.Spellcaster;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper;
import eatyourbeets.actions.common.VariableDiscardAction;
import eatyourbeets.cards.AnimatorCard;
import eatyourbeets.cards.Synergies;

import java.util.ArrayList;

public class Evileye extends AnimatorCard implements Spellcaster
{
    public static final String ID = Register(Evileye.class.getSimpleName(), EYBCardBadge.Synergy);

    public Evileye()
    {
        super(ID, 2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);

        Initialize(0,0, 1);

        SetSynergy(Synergies.Overlord);
    }

    @Override
    public void applyPowers()
    {
        super.applyPowers();

        Spellcaster.ApplyScaling(this, 3);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        GameActionsHelper.DrawCard(p, magicNumber);
        GameActionsHelper.AddToBottom(new VariableDiscardAction(this, p, BaseMod.MAX_HAND_SIZE, this, this::OnDiscard, true));

        if (HasActiveSynergy() && EffectHistory.TryActivateLimited(this.cardID))
        {
            GameActionsHelper.GainIntellect(2);
            GameActionsHelper.AddToBottom(new IncreaseMaxOrbAction(1));
        }
    }

    @Override
    public void upgrade() 
    {
        if (TryUpgrade())
        {
            upgradeBaseCost(1);
        }
    }

    private void OnDiscard(Object state, ArrayList<AbstractCard> discarded)
    {
        if (state == null || discarded == null)
        {
            return;
        }

        AbstractPlayer p = AbstractDungeon.player;
        if (p.orbs.size() > 0)
        {
            for (int i = 0; i < discarded.size(); i++)
            {
                p.orbs.get(0).onStartOfTurn();
                p.orbs.get(0).onEndOfTurn();
            }
        }
    }
}