package eatyourbeets.cards.animator;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.EYBCardBadge;
import eatyourbeets.interfaces.metadata.Spellcaster;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActionsHelper_Legacy; import eatyourbeets.utilities.GameActions;
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
        GameActions.Bottom.Draw(magicNumber);
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

        if (HasActiveSynergy() && EffectHistory.TryActivateLimited(this.cardID))
        {
            GameActions.Bottom.GainIntellect(2);
            GameActionsHelper_Legacy.AddToBottom(new IncreaseMaxOrbAction(1));
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
}