package eatyourbeets.cards.animator.beta.special;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.stances.NeutralStance;
import eatyourbeets.cards.base.*;
import eatyourbeets.misc.CardMods.AfterLifeMod;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.stances.AgilityStance;
import eatyourbeets.stances.ForceStance;
import eatyourbeets.stances.IntellectStance;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.RandomizedList;

public class Shirosaki_Laplace extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Shirosaki_Laplace.class)
    		.SetSkill(1, CardRarity.SPECIAL, EYBCardTarget.None).SetSeries(CardSeries.RozenMaiden);

    public Shirosaki_Laplace()
    {
        super(DATA);

        Initialize(0, 0, 0);
        SetUpgrade(0, 0, 0);
        SetAffinity_Water(1, 1, 0);
        SetAffinity_Dark(2, 0, 0);

        AfterLifeMod.Add(this);
    }

    @Override
    protected void OnUpgrade()
    {
        SetRetain(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        if (CombatStats.ControlPile.Contains(this))
        {
            GameActions.Top.FetchFromPile(name,1, p.discardPile)
                    .SetMessage(MoveCardsAction.TEXT[0])
                    .SetOptions(false, true);
        }
        else
        {
            GameActions.Bottom.DiscardFromHand(name, 1, false)
                    .SetMessage(cardData.Strings.EXTENDED_DESCRIPTION[0])
                    .SetFilter(c -> c.type == CardType.CURSE)
                    .SetOptions(false, false, false)
                    .AddCallback(this::HanduseSuccess);
        }
    }

    private void HanduseSuccess()
    {
        RandomizedList<String> stances = new RandomizedList<>();

        if (!player.stance.ID.equals(ForceStance.STANCE_ID))
            stances.Add(ForceStance.STANCE_ID);

        if (!player.stance.ID.equals(AgilityStance.STANCE_ID))
            stances.Add(AgilityStance.STANCE_ID);

        if (!player.stance.ID.equals(IntellectStance.STANCE_ID))
            stances.Add(IntellectStance.STANCE_ID);

        if (!player.stance.ID.equals(NeutralStance.STANCE_ID))
            stances.Add(NeutralStance.STANCE_ID);

        GameActions.Bottom.ChangeStance(stances.Retrieve(rng));
        GameActions.Bottom.Exhaust(this);
    }
}
