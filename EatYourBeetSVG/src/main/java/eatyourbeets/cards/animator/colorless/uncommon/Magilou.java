package eatyourbeets.cards.animator.colorless.uncommon;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.orbs.AbstractOrb;
import com.megacrit.cardcrawl.orbs.Lightning;
import eatyourbeets.actions.orbs.ShuffleOrbs;
import eatyourbeets.cards.animator.special.Magilou_Bienfu;
import eatyourbeets.cards.base.*;
import eatyourbeets.powers.CombatStats;
import eatyourbeets.utilities.GameActions;
import eatyourbeets.utilities.GameUtilities;

public class Magilou extends AnimatorCard
{
    public static final EYBCardData DATA = Register(Magilou.class)
            .SetSkill(1, CardRarity.UNCOMMON, EYBCardTarget.None)
            .SetMaxCopies(1)
            .SetColor(CardColor.COLORLESS)
            .SetSeries(CardSeries.TalesOfBerseria)
            .PostInitialize(data -> data.AddPreview(new Magilou_Bienfu(), false));

    public Magilou()
    {
        super(DATA);

        Initialize(0, 0, 0);

        SetAffinity_Blue(2);

        SetExhaust(true);
    }

    @Override
    public void triggerWhenDrawn()
    {
        if (CombatStats.TryActivateLimited(cardID))
        {
            GameActions.Top.Discard(this, player.hand).ShowEffect(true, true)
            .AddCallback(() -> GameActions.Top.MakeCardInHand(new Magilou_Bienfu()))
            .SetDuration(0.5f, true);
        }
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        AbstractOrb firstOrb = GameUtilities.GetFirstOrb(null);
        AbstractOrb newOrb = firstOrb != null ? firstOrb.makeCopy() : new Lightning();
        GameActions.Bottom.ChannelOrb(newOrb);
        GameActions.Bottom.Callback(new ShuffleOrbs(1)).AddCallback(() -> {
            if (upgraded) {
                GameActions.Bottom.TriggerOrbPassive(player.orbs.size(), false, true);
            }
        });
    }
}