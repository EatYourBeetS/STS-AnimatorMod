package eatyourbeets.cards.animator.beta.TouhouProject;

import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.Dark;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;

public class Clownpiece extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Clownpiece.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Clownpiece()
    {
        super(DATA);

        Initialize(0, 0, 1, 0);
        SetUpgrade(0, 0, 0, 0);
        SetScaling(0, 0, 0);

        SetSynergy(Synergies.TouhouProject);
    }

    @Override
    protected void OnUpgrade()
    {
        SetHaste(true);
    }


    @Override
    public void use(AbstractPlayer p, AbstractMonster m)
    {
        if (HasSynergy())
        {
            if (CombatStats.TryActivateSemiLimited(cardID))
            {
                GameActions.Top.ChannelOrb(new Dark(), true);
            }
        }

        GameActions.Top.Callback(() ->
        {
            if (player.drawPile.size() > 0)
            {
                AbstractCard card = player.drawPile.getTopCard();
                GameActions.Top.PlayCard(card, player.drawPile, null)
                        .SpendEnergy(false);
            }
        });

        if (player.drawPile.isEmpty())
        {
            if (player.discardPile.size() > 0)
            {
                GameActions.Top.Add(new EmptyDeckShuffleAction());
            }
        }

    }
}

