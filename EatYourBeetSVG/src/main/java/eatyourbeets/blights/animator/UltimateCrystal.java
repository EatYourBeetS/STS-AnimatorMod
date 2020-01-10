package eatyourbeets.blights.animator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import eatyourbeets.actions.special.RefreshHandLayout;
import eatyourbeets.blights.AnimatorBlight;
import eatyourbeets.resources.GR;
import eatyourbeets.utilities.GameActions;

public class UltimateCrystal extends AnimatorBlight
{
    public static final String ID = CreateFullID(UltimateCrystal.class.getSimpleName());

    public UltimateCrystal()
    {
        super(ID, 3);

        this.counter = -1;
    }

    @Override
    public void atBattleStart()
    {
        super.atBattleStart();

        GameActions.Bottom.SelectFromHand(name, initialAmount, false)
        .SetMessage(GR.Common.Strings.HandSelection.MoveToDrawPile)
        .AddCallback(cards ->
        {
            AbstractPlayer p = AbstractDungeon.player;
            for (AbstractCard c : cards)
            {
                GameActions.Top.MoveCard(c, p.drawPile, p.hand);
            }

            GameActions.Bottom.Add(new RefreshHandLayout());
        });

        this.flash();
    }
}