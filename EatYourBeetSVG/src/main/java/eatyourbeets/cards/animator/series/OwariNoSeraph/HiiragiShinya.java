package eatyourbeets.cards.animator.series.OwariNoSeraph;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class HiiragiShinya extends AnimatorCard
{
    public static final EYBCardData DATA = Register(HiiragiShinya.class).SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public HiiragiShinya()
    {
        super(DATA);

        Initialize(0, 4, 2);
        SetUpgrade(0, 3, 0);

        SetSynergy(Synergies.OwariNoSeraph);
        SetAffinity(0, 1, 1, 1, 0);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, boolean isSynergizing)
    {
        GameActions.Bottom.GainBlock(block);
        GameActions.Bottom.FetchFromPile(name, 1, p.discardPile)
        .SetMessage(MoveCardsAction.TEXT[0])
        .SetOptions(false, false)
        .AddCallback(cards ->
        {
            if (cards.size() > 0)
            {
                AbstractCard card = cards.get(0);
                GameUtilities.ModifyCostForTurn(card, 1, true);
                GameUtilities.Retain(card);
                GameActions.Bottom.Add(new RefreshHandLayout());
            }
        });

        if (isSynergizing)
        {
            GameActions.Bottom.StackPower(new SupportDamagePower(p, magicNumber));
        }
    }
}