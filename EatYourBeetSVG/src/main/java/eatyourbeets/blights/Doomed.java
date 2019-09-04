package eatyourbeets.blights;

import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import eatyourbeets.actions.animator.KillCharacterAction;
import eatyourbeets.cards.animator.TheHaunt;
import eatyourbeets.utilities.GameActionsHelper;

public class Doomed extends AbstractBlight
{
    public static final String ID = "animator:Doomed";
    private static final RelicStrings blightStrings;
    public static final String NAME;
    public static final String[] DESC;

    public Doomed()
    {
        this(4);
    }

    public Doomed(int turns)
    {
        super(ID, NAME, DESC[0] + turns + DESC[1], "Doomed.png", true);
        this.counter = turns;
    }

    @Override
    public void onPlayerEndTurn()
    {
        super.onPlayerEndTurn();

        flash();

        if (counter <= 1)
        {
            AbstractPlayer p = AbstractDungeon.player;
            GameActionsHelper.AddToBottom(new KillCharacterAction(p, p));
        }
        else
        {
            counter -= 1;

            this.pulse = (counter <= 1);
        }
    }

    static
    {
        blightStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
        NAME = blightStrings.NAME;
        DESC = blightStrings.DESCRIPTIONS;
    }
}