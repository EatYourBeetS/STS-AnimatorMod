package eatyourbeets.cards.animatorClassic.series.OwariNoSeraph;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.cards.base.AnimatorClassicCard;
import eatyourbeets.cards.base.CardUseInfo;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.powers.animator.SupportDamagePower;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class HiiragiShinya extends AnimatorClassicCard
{
    public static final EYBCardData DATA = Register(HiiragiShinya.class).SetSeriesFromClassPackage().SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None);

    public HiiragiShinya()
    {
        super(DATA);

        Initialize(0, 4, 2);
        SetUpgrade(0, 3, 0);

        
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
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

        if (info.IsSynergizing)
        {
            GameActions.Bottom.StackPower(new SupportDamagePower(p, magicNumber));
        }
    }
}