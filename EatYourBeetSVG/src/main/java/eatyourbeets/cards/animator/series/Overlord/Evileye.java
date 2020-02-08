package eatyourbeets.cards.animator.series.Overlord;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import eatyourbeets.cards.base.AnimatorCard;
import eatyourbeets.cards.base.EYBCardData;
import eatyourbeets.cards.base.EYBCardTarget;
import eatyourbeets.cards.base.Synergies;
import eatyourbeets.interfaces.markers.Spellcaster;
import eatyourbeets.ui.EffectHistory;
import eatyourbeets.utilities.GameActions;

import java.util.ArrayList;

public class Evileye extends AnimatorCard implements Spellcaster
{
    public static final EYBCardData DATA = Register(Evileye.class).SetSkill(2, CardRarity.UNCOMMON, EYBCardTarget.None);

    public Evileye()
    {
        super(DATA);

        Initialize(0,0, 1);
        SetCostUpgrade(-1);

        SetSynergy(Synergies.Overlord);
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

        if (HasSynergy() && EffectHistory.TryActivateLimited(this.cardID))
        {
            GameActions.Bottom.GainIntellect(2);
            GameActions.Bottom.GainOrbSlots(1);
        }
    }
}